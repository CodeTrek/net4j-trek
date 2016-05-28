/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.client.impl;

import java.util.UUID;

import org.code.trek.net4j.r2.client.R2Client;
import org.code.trek.net4j.r2.client.R2Method;
import org.code.trek.net4j.r2.dds.ParticipantFactory;
import org.code.trek.net4j.r2.dds.PublisherFactory;
import org.code.trek.net4j.r2.dds.RL;
import org.code.trek.net4j.r2.dds.ReplyTopicFactory;
import org.code.trek.net4j.r2.dds.RequestReplyDataReader;
import org.code.trek.net4j.r2.dds.RequestReplyDataWriter;
import org.code.trek.net4j.r2.dds.RequestTopicFactory;
import org.code.trek.net4j.r2.dds.SubscriberFactory;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;

/**
 * An Object Management Group (OMG) data distribution service (DDS) based request/response client.
 */
public class DdsClient implements R2Client {

    private final String clientId;
    private final int domain;
    @SuppressWarnings("unused")
    private final String partition;
    private DomainParticipant participant;
    private Subscriber subscriber;
    private Topic replyTopic;
    private Publisher publisher;
    private Topic requestTopic;
    private RequestReplyDataReader reader;
    private RequestReplyDataWriter writer;

    public DdsClient(String description) {
        final RL rl = new RL(description);
        domain = rl.getDomain();
        partition = rl.getPartition();
        clientId = UUID.randomUUID().toString();
    }

    @Override
    public int execute(R2Method method) {
        return method.execute();
    }

    @Override
    public R2Method newMethod(final String method) {
        return new DdsMethod(clientId, reader, writer);
    }

    @Override
    public void activate() {
        // Create the DDS objects necessary for request/reply communication with the server.
        participant = ParticipantFactory.create(this.domain);
        subscriber = SubscriberFactory.create(participant);
        replyTopic = ReplyTopicFactory.create(participant);
        publisher = PublisherFactory.create(participant);
        requestTopic = RequestTopicFactory.create(participant);

        // @formatter:off
        reader = (RequestReplyDataReader) subscriber.create_datareader(
            replyTopic,
            Subscriber.DATAREADER_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        // @formatter:off
        writer = (RequestReplyDataWriter) publisher.create_datawriter(
            requestTopic,
            Publisher.DATAWRITER_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on
    }

    @Override
    public void deactivate() {
        if (participant != null) {
            participant.delete_contained_entities();
            DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
        }

        participant = null;
    }
}
