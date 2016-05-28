/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.server.impl;

import java.io.IOException;

import org.code.trek.net4j.r2.dds.ParticipantFactory;
import org.code.trek.net4j.r2.dds.PublisherFactory;
import org.code.trek.net4j.r2.dds.RL;
import org.code.trek.net4j.r2.dds.ReplyTopicFactory;
import org.code.trek.net4j.r2.dds.RequestReply;
import org.code.trek.net4j.r2.dds.RequestReplyDataReader;
import org.code.trek.net4j.r2.dds.RequestReplyDataWriter;
import org.code.trek.net4j.r2.dds.RequestTopicFactory;
import org.code.trek.net4j.r2.dds.SubscriberFactory;
import org.code.trek.net4j.r2.servlet.R2Handler;
import org.code.trek.net4j.r2.servlet.R2Servlet;
import org.code.trek.net4j.r2.servlet.R2ServletRequest;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
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
        SampleInfo info = new SampleInfo();
        RequestReply receivedData = new RequestReply();
        for (;;) {
            try {
                requestReplyReader.take_next_sample(receivedData, info);
                if ((info.valid_data) && (info.sample_state == SampleStateKind.NOT_READ_SAMPLE_STATE)) {
                    handleClientRequest(receivedData.payload.toArrayByte(null), receivedData.clientId, writer);
                }
            } catch (RETCODE_NO_DATA noData) {
                break;
            } catch (RETCODE_ERROR e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void handleClientRequest(byte[] payload, String clientId, RequestReplyDataWriter writer) throws IOException {
        R2ServletRequest request = new DdsServletRequest(payload);
        R2ServletResponse response = new DdsServletResponse(clientId, writer);

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
            System.err.println("create_datawriter error\n");
            return;
        }

        // Create request reader
        // @formatter:off
        reader = (RequestReplyDataReader) subscriber.create_datareader(
            requestTopic,
            Subscriber.DATAREADER_QOS_DEFAULT,
            this,
            StatusKind.DATA_AVAILABLE_STATUS);
        // @formatter:on

        if (reader == null) {
            System.err.println("create_datareader error\n");
            return;
        }
    }

    @Override
    public void deactivate() {
        if (participant != null) {
            participant.delete_contained_entities();
            DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
        }
    }
}
