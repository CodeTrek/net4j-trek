/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */
/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.server.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.code.trek.net4j.r2.eb.ServletResponse;
import org.code.trek.net4j.r2.eb.client.EbClient;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

/**
 * Servlet response object used to deliver a response to an {@link EbClient} request.
 * 
 * @author jgraham
 */
public class EbServletResponse implements R2ServletResponse {
    private static class ServletOutputStream extends ByteArrayOutputStream {

        private ServletResponse response;

        public ServletOutputStream(ServletResponse response) {
            this.response = response;
        }

        @Override
        public void flush() throws IOException {
            response.setResponse(toByteArray());
        }
    }

    private final OutputStream outputStream;

    public EbServletResponse(ServletResponse response) {
        outputStream = new ServletOutputStream(response);
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }
}
