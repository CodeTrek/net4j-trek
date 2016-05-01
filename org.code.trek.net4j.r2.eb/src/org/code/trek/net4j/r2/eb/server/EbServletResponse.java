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

package org.code.trek.net4j.r2.eb.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.code.trek.net4j.r2.eb.ServletResponse;
import org.code.trek.net4j.r2.eb.client.EbClient;

/**
 * Servlet response object used to deliver a response to an {@link EbClient} request.
 * 
 * @author jgraham
 */
public class EbServletResponse {
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

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
