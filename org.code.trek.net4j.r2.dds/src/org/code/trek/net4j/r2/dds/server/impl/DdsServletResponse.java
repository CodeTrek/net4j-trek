/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.server.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.code.trek.net4j.r2.dds.RequestReply;
import org.code.trek.net4j.r2.dds.RequestReplyDataWriter;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

import com.rti.dds.infrastructure.InstanceHandle_t;

public class DdsServletResponse implements R2ServletResponse {
    private static class ServletOutputStream extends ByteArrayOutputStream {

        private final String clientId;
        private final RequestReplyDataWriter writer;

        public ServletOutputStream(String clientId, RequestReplyDataWriter writer) {
            this.clientId = clientId;
            this.writer = writer;
        }

        @Override
        public void flush() throws IOException {
            RequestReply instance = new RequestReply();
            InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
            instance.clientId = clientId;
            instance.payload.addAllByte(buf);
            writer.write(instance, instance_handle);
            // writer.wait_for_asynchronous_publishing(Duration_t.DURATION_INFINITE);
            // System.out.println("[server] write response: " + instance);
        }
    }

    private final OutputStream outputStream;

    public DdsServletResponse(String clientId, RequestReplyDataWriter writer) {
        outputStream = new ServletOutputStream(clientId, writer);
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
