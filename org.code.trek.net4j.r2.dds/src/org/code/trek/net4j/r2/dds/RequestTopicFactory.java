package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.topic.Topic;

public class RequestTopicFactory {

    public static Topic create(DomainParticipant participant) {
        String typeName = RequestReplyTypeSupport.get_type_name();
        RequestReplyTypeSupport.register_type(participant, typeName);

        // @formatter:off
        Topic topic = participant.create_topic(
            "Request",
            typeName,
            DomainParticipant.TOPIC_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (topic == null) {
            System.err.println("failed to create topic: Request\n");
            return null;
        }

        return topic;
    }

    private RequestTopicFactory() {
    }
}
