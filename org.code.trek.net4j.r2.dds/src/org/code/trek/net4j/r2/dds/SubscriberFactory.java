package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.Subscriber;

public class SubscriberFactory {

    public static Subscriber create(DomainParticipant participant) {
        // @formatter:off
        Subscriber subscriber = participant.create_subscriber(
            DomainParticipant.SUBSCRIBER_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (subscriber == null) {
            System.err.println("create_subscriber error\n");
        }

        return subscriber;
    }

    private SubscriberFactory() {
    }
}
