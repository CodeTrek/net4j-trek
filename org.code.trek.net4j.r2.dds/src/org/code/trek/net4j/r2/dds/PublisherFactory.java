package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;

public class PublisherFactory {

    public static Publisher create(DomainParticipant participant) {
        // @formatter:off
        Publisher publisher = participant.create_publisher(
            DomainParticipant.PUBLISHER_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (publisher == null) {
            System.err.println("create_publisher error\n");
        }

        return publisher;
    }

    private PublisherFactory() {
    }
}
