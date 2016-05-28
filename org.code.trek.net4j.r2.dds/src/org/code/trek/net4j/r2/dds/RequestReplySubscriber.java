
package org.code.trek.net4j.r2.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

// ===========================================================================

public class RequestReplySubscriber {
    // -----------------------------------------------------------------------
    // Public Methods
    // -----------------------------------------------------------------------

    public static void main(String[] args) {
        // --- Get domain ID --- //
        int domainId = 0;
        if (args.length >= 1) {
            domainId = Integer.valueOf(args[0]).intValue();
        }

        // -- Get max loop count; 0 means infinite loop --- //
        int sampleCount = 0;
        if (args.length >= 2) {
            sampleCount = Integer.valueOf(args[1]).intValue();
        }

        /*
         * Uncomment this to turn on additional logging Logger.get_instance().set_verbosity_by_category(
         * LogCategory.NDDS_CONFIG_LOG_CATEGORY_API, LogVerbosity.NDDS_CONFIG_LOG_VERBOSITY_STATUS_ALL);
         */

        // --- Run --- //
        subscriberMain(domainId, sampleCount);
    }

    // -----------------------------------------------------------------------
    // Private Methods
    // -----------------------------------------------------------------------

    // --- Constructors: -----------------------------------------------------

    private RequestReplySubscriber() {
        super();
    }

    // -----------------------------------------------------------------------

    private static void subscriberMain(int domainId, int sampleCount) {

        DomainParticipant participant = null;
        Subscriber subscriber = null;
        Topic topic = null;
        DataReaderListener listener = null;
        RequestReplyDataReader reader = null;

        try {

            // --- Create participant --- //

            /*
             * To customize participant QoS, use the configuration file USER_QOS_PROFILES.xml
             */

            participant = DomainParticipantFactory.TheParticipantFactory.create_participant(domainId,
                    DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (participant == null) {
                System.err.println("create_participant error\n");
                return;
            }

            // --- Create subscriber --- //

            /*
             * To customize subscriber QoS, use the configuration file USER_QOS_PROFILES.xml
             */

            subscriber = participant.create_subscriber(DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null /* listener */,
                    StatusKind.STATUS_MASK_NONE);
            if (subscriber == null) {
                System.err.println("create_subscriber error\n");
                return;
            }

            // --- Create topic --- //

            /* Register type before creating topic */
            String typeName = RequestReplyTypeSupport.get_type_name();
            RequestReplyTypeSupport.register_type(participant, typeName);

            /*
             * To customize topic QoS, use the configuration file USER_QOS_PROFILES.xml
             */

            topic = participant.create_topic("Example RequestReply", typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
                    null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (topic == null) {
                System.err.println("create_topic error\n");
                return;
            }

            // --- Create reader --- //

            listener = new RequestReplyListener();

            /*
             * To customize data reader QoS, use the configuration file USER_QOS_PROFILES.xml
             */

            reader = (RequestReplyDataReader) subscriber.create_datareader(topic, Subscriber.DATAREADER_QOS_DEFAULT,
                    listener, StatusKind.STATUS_MASK_ALL);
            if (reader == null) {
                System.err.println("create_datareader error\n");
                return;
            }

            // --- Wait for data --- //

            final long receivePeriodSec = 4;

            for (int count = 0; (sampleCount == 0) || (count < sampleCount); ++count) {
                System.out.println("RequestReply subscriber sleeping for " + receivePeriodSec + " sec...");
                try {
                    Thread.sleep(receivePeriodSec * 1000); // in millisec
                } catch (InterruptedException ix) {
                    System.err.println("INTERRUPTED");
                    break;
                }
            }
        } finally {

            // --- Shutdown --- //

            if (participant != null) {
                participant.delete_contained_entities();

                DomainParticipantFactory.TheParticipantFactory.delete_participant(participant);
            }
            /*
             * RTI Data Distribution Service provides the finalize_instance() method for users who want to release
             * memory used by the participant factory singleton. Uncomment the following block of code for clean
             * destruction of the participant factory singleton.
             */
            // DomainParticipantFactory.finalize_instance();
        }
    }

    // -----------------------------------------------------------------------
    // Private Types
    // -----------------------------------------------------------------------

    // =======================================================================

    private static class RequestReplyListener extends DataReaderAdapter {

        RequestReplySeq _dataSeq = new RequestReplySeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
            RequestReplyDataReader RequestReplyReader = (RequestReplyDataReader) reader;

            try {
                RequestReplyReader.take(_dataSeq, _infoSeq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                        SampleStateKind.ANY_SAMPLE_STATE, ViewStateKind.ANY_VIEW_STATE,
                        InstanceStateKind.ANY_INSTANCE_STATE);

                for (int i = 0; i < _dataSeq.size(); ++i) {
                    SampleInfo info = (SampleInfo) _infoSeq.get(i);

                    if (info.valid_data) {
                        System.out.println(((RequestReply) _dataSeq.get(i)).toString("Received", 0));

                    }
                }
            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
                RequestReplyReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
}
