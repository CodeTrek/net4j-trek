/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.client.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.code.trek.net4j.r2.client.R2Method;
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

    public DdsMethod(String clientId, RequestReplyDataReader reader, RequestReplyDataWriter writer) {
        this.clientId = clientId;
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public int execute() {
        RequestReply instance = new RequestReply();
        InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;

        instance.clientId = clientId;
        instance.payload.addAllByte(payload);
        writer.write(instance, instance_handle);

        return 0;
    }

    @Override
    public InputStream getResponse(int timeOutMs) {
        ByteArrayInputStream inputStream = null;

        /* Create query condition */
        StringSeq query_parameters = new StringSeq(1);
        query_parameters.add("'" + this.clientId + "'");
        String query_expression = new String("clientId MATCH %0");

        // @formatter:off
        QueryCondition query_condition = reader.create_querycondition(
            SampleStateKind.NOT_READ_SAMPLE_STATE,
            ViewStateKind.ANY_VIEW_STATE,
            InstanceStateKind.ANY_INSTANCE_STATE,
            query_expression,
            query_parameters);
        // @formatter:on

        WaitSet waitset = new WaitSet();
        final Duration_t wait_timeout = new Duration_t(1, timeOutMs);

        /* Attach Query Conditions */
        waitset.attach_condition(query_condition);

        // --- Wait for data --- //
        ConditionSeq active_conditions_seq = new ConditionSeq();

        /*
         * wait() blocks execution of the thread until one or more attached Conditions become true, or until a
         * user-specified timeout expires.
         */
        try {
            waitset.wait(active_conditions_seq, wait_timeout);
        } catch (RETCODE_TIMEOUT to) {
            System.out.println("Wait timed out!! No conditions were triggered.");
            return null;
        }

        RequestReplySeq data_seq = new RequestReplySeq();
        SampleInfoSeq info_seq = new SampleInfoSeq();

        try {
            reader.take_w_condition(data_seq, info_seq, ResourceLimitsQosPolicy.LENGTH_UNLIMITED, query_condition);

            // Read the data
            for (int i = 0; i < data_seq.size(); ++i) {
                if (!((SampleInfo) info_seq.get(i)).valid_data) {
                    System.out.println("Got metadata");
                    continue;
                }

                RequestReply reply = (RequestReply) data_seq.get(i);
                inputStream = new ByteArrayInputStream(reply.payload.toArrayByte(new byte[0]));
            }
        } catch (RETCODE_NO_DATA noData) {
            System.out.println("reply complete");
        } finally {
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