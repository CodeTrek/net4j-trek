/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.topic.Topic;

public class ReplyTopicFactory {
    private static final String TOPIC_NAME = "Reply";

    public static Topic create(DomainParticipant participant) {

        String typeName = RequestReplyTypeSupport.get_type_name();
        RequestReplyTypeSupport.register_type(participant, typeName);

        // @formatter:off
        Topic topic = participant.create_topic(
            TOPIC_NAME,
            typeName,
            DomainParticipant.TOPIC_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (topic == null) {
            OM.LOG.error("failed to create topic: " + TOPIC_NAME);
            return null;
        }

        return topic;
    }

    private ReplyTopicFactory() {
    }
}
