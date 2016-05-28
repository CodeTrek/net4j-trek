package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;

public class ParticipantFactory {

    public static DomainParticipant create(int domainId) {
        // @formatter:off
        DomainParticipant participant = DomainParticipantFactory.TheParticipantFactory.create_participant(
            domainId,
            DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
            null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        // @formatter:on

        if (participant == null) {
            System.err.println("create participant error\n");
        }

        return participant;
    }

    private ParticipantFactory() {
    }
}
