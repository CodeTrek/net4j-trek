/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.server.impl;

import java.io.IOException;

import org.code.trek.net4j.r2.dds.OM;
import org.code.trek.net4j.r2.dds.ParticipantFactory;
import org.code.trek.net4j.r2.dds.PublisherFactory;
import org.code.trek.net4j.r2.dds.RL;
import org.code.trek.net4j.r2.dds.ReplyTopicFactory;
import org.code.trek.net4j.r2.dds.RequestReply;
import org.code.trek.net4j.r2.dds.RequestReplyDataReader;
import org.code.trek.net4j.r2.dds.RequestReplyDataWriter;
import org.code.trek.net4j.r2.dds.RequestReplySeq;
import org.code.trek.net4j.r2.dds.RequestTopicFactory;
import org.code.trek.net4j.r2.dds.SubscriberFactory;
import org.code.trek.net4j.r2.servlet.R2Handler;
import org.code.trek.net4j.r2.servlet.R2Servlet;
import org.code.trek.net4j.r2.servlet.R2ServletRequest;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

/**
 * Object Management Group (OMG) data distribution services (DDS) based servlet.
 * 
 * @author jgraham
 *
 */
public class DdsServlet extends DataReaderAdapter implements R2Servlet {

    private final RL resourceLocator;
    private RequestReplyDataWriter writer;
    private R2Handler handler;
    private DomainParticipant participant;

    public DdsServlet(String description) {
        resourceLocator = new RL(description);
    }

    @Override
    public void on_data_available(DataReader reader) {
        RequestReplyDataReader requestReplyReader = (RequestReplyDataReader) reader;
        SampleInfoSeq infoSeq = new SampleInfoSeq();
        RequestReplySeq dataSeq = new RequestReplySeq();
        try {
            // @formatter:off
            requestReplyReader.take(
                    dataSeq, infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE
            );
            // @formatter:on

            OM.LOG.debug("[server] data sequence size: " + dataSeq.size());

            for (int i = 0; i < dataSeq.size(); ++i) {
                SampleInfo info = (SampleInfo) infoSeq.get(i);
                System.out.println("valid data: " + info.valid_data);
                if (info.valid_data) {
                    RequestReply sample = (RequestReply) dataSeq.get(i);
                    if (info.view_state == ViewStateKind.NEW_VIEW_STATE) {
                        OM.LOG.debug("[server] new instance: client id = " + sample.clientId + "\n");
                    }
                    handleClientRequest(sample.payload.toArrayByte(null), sample.clientId, writer);
                } else {
                    /* Since there is not valid data, it may include metadata */
                    RequestReply dummy = new RequestReply();
                    requestReplyReader.get_key_value(dummy, info.instance_handle);

                    /* Here we print a message if the instance state is ALIVE_NO_WRITERS or ALIVE_DISPOSED */
                    if (info.instance_state == InstanceStateKind.NOT_ALIVE_NO_WRITERS_INSTANCE_STATE) {
                        OM.LOG.info("[server] instance " + dummy.clientId + " has no writers\n");
                    } else if (info.instance_state == InstanceStateKind.NOT_ALIVE_DISPOSED_INSTANCE_STATE) {
                        OM.LOG.info("[server] instance " + dummy.clientId + " disposed\n");
                    }
                }
            }

        } catch (RETCODE_NO_DATA noData) {
            OM.LOG.error("[server] RETCODE_NO_DATA");
        } catch (IOException e) {
            OM.LOG.error("[server] error: " + e.getMessage());
        } finally {
            requestReplyReader.return_loan(dataSeq, infoSeq);
        }
    }

    void handleClientRequest(byte[] payload, String clientId, RequestReplyDataWriter writer) throws IOException {
        final R2ServletRequest request = new DdsServletRequest(payload);
        final R2ServletResponse response = new DdsServletResponse(clientId, writer);
        doRequest(request, response);
    }

    @Override
    public void doRequest(R2ServletRequest request, R2ServletResponse response) throws IOException {
        try {
            handler.doRequest(request, response);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void setRequestHandler(R2Handler handler) {
        this.handler = handler;
    }

    @Override
    public void activate() {
        participant = null;
        Subscriber subscriber = null;
        Publisher publisher = null;
        Topic requestTopic = null;
        Topic replyTopic = null;
        RequestReplyDataReader reader = null;

        // Create a domain participant
        participant = ParticipantFactory.create(resourceLocator.getDomain());

        if (participant == null) {
            return;
        }

        // Create a request subscriber
        subscriber = SubscriberFactory.create(participant);

        if (subscriber == null) {
            return;
        }

        // Create a reply publisher
        publisher = PublisherFactory.create(participant);

        if (publisher == null) {
            return;
        }

        // Create request/reply topics
        requestTopic = RequestTopicFactory.create(participant);
        replyTopic = ReplyTopicFactory.create(participant);

        // Create reply writer
        // @formatter:off
        writer = (RequestReplyDataWriter) publisher.create_datawriter(
            replyTopic,
            Publisher.DATAWRITER_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (writer == null) {
            OM.LOG.error("create_datawriter error\n");
            return;
        }

        // Create request reader
        // @formatter:off
        reader = (RequestReplyDataReader) subscriber.create_datareader(
            requestTopic,
            Subscriber.DATAREADER_QOS_DEFAULT,
            this,
            StatusKind.DATA_AVAILABLE_STATUS
        );
        // @formatter:on

        if (reader == null) {
            System.err.println("create_datareader error\n");
            return;
        }

        OM.LOG.info("[server] activated");
    }

    @Override
    public void deactivate() {
        if (participant != null) {
            participant.delete_contained_entities();
            DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
        }

        OM.LOG.info("[server] deactivated");
    }
}
