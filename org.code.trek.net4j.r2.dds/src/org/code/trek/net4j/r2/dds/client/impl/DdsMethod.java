/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.client.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.code.trek.net4j.r2.client.R2Method;
import org.code.trek.net4j.r2.dds.OM;
import org.code.trek.net4j.r2.dds.RequestReply;
import org.code.trek.net4j.r2.dds.RequestReplyDataReader;
import org.code.trek.net4j.r2.dds.RequestReplyDataWriter;
import org.code.trek.net4j.r2.dds.RequestReplySeq;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.infrastructure.ConditionSeq;
import com.rti.dds.infrastructure.Duration_t;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.RETCODE_TIMEOUT;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StringSeq;
import com.rti.dds.infrastructure.WaitSet;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.QueryCondition;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.ViewStateKind;

public class DdsMethod implements R2Method {
    private final String clientId;
    @SuppressWarnings("unused")
    private DomainParticipant participant;
    private byte[] payload;
    private RequestReplyDataReader reader;
    private RequestReplyDataWriter writer;
    private RequestReply instance;
    private InstanceHandle_t instanceHandle;

    public DdsMethod(String clientId, RequestReplyDataReader reader, RequestReplyDataWriter writer) {
        this.clientId = clientId;
        this.reader = reader;
        this.writer = writer;
        this.instance = new RequestReply();
        this.instance.clientId = clientId;
        this.instanceHandle = InstanceHandle_t.HANDLE_NIL;
        // this.instanceHandle = writer.register_instance(instance);
    }

    @Override
    public int execute() {

        instance.clientId = clientId;
        instance.payload.clear();
        instance.payload.addAllByte(payload);
        writer.write(instance, instanceHandle);
        writer.wait_for_asynchronous_publishing(Duration_t.DURATION_INFINITE);

        return 0;
    }

    private QueryCondition createQueryCondition() {
        /* Create query condition */
        StringSeq query_parameters = new StringSeq(1);
        query_parameters.add("'" + clientId + "'");
        String query_expression = new String("clientId MATCH %0");

        // @formatter:off
        QueryCondition query_condition = reader.create_querycondition(
            SampleStateKind.NOT_READ_SAMPLE_STATE,
            ViewStateKind.ANY_VIEW_STATE,
            InstanceStateKind.ANY_INSTANCE_STATE,
            query_expression,
            query_parameters);
        // @formatter:on

        return query_condition;
    }

    @Override
    public InputStream getResponse(int timeOutMs) {
        ByteArrayInputStream inputStream = null;

        QueryCondition query_condition = createQueryCondition();

        WaitSet waitset = new WaitSet();

        /* Attach Query Conditions */
        waitset.attach_condition(query_condition);

        // --- Wait for data --- //
        ConditionSeq active_conditions_seq = new ConditionSeq();

        /*
         * wait() blocks execution of the thread until one or more attached Conditions become true, or until a
         * user-specified timeout expires.
         */
        try {
            waitset.wait(active_conditions_seq, Duration_t.from_millis(timeOutMs));
        } catch (RETCODE_TIMEOUT to) {
            StringSeq params = new StringSeq();
            query_condition.get_query_parameters(params);
            OM.LOG.info("Wait timed out. Condition: " + query_condition.get_query_expression()
                    + " not triggered. Parameters: " + params);
            waitset.detach_condition(query_condition);
            waitset.delete();
            return null;
        }

        RequestReplySeq data_seq = new RequestReplySeq();
        SampleInfoSeq info_seq = new SampleInfoSeq();

        try {
            reader.take_w_condition(data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED, query_condition);

            // Read the data
            for (int i = 0; i < data_seq.size(); ++i) {
                if (!((SampleInfo) info_seq.get(i)).valid_data) {
                    OM.LOG.info("Received metatdata.");
                    continue;
                }

                RequestReply reply = (RequestReply) data_seq.get(i);
                inputStream = new ByteArrayInputStream(reply.payload.toArrayByte(new byte[0]));
            }
        } catch (RETCODE_NO_DATA noData) {
            OM.LOG.info("Reply complete.");
        } finally {
            waitset.detach_condition(query_condition);
            waitset.delete();
            reader.delete_readcondition(query_condition);
            /* Return the loaned data */
            reader.return_loan(data_seq, info_seq);
        }

        return inputStream;
    }

    @Override
    public void setPayload(byte[] data) {
        this.payload = data;
    }
}