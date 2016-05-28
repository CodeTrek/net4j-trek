/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.code.trek.net4j.r2.servlet.R2Handler;
import org.code.trek.net4j.r2.servlet.R2ServletRequest;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

public class EchoHandler implements R2Handler {

    @Override
    public void doRequest(R2ServletRequest request, R2ServletResponse response) throws IOException {
        InputStream in = request.getInputStream();
        OutputStream out = response.getOutputStream();

        while (true) {
            int nextByte = in.read();
            if (nextByte == -1) {
                break;
            }
            out.write(nextByte);
        }

        out.flush();
    }
}
