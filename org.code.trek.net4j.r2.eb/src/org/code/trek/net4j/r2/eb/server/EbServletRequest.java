/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.code.trek.net4j.r2.eb.client.EbClient;
import org.code.trek.net4j.r2.servlet.R2ServletRequest;

/**
 * Represents an {@link EbClient} server request.
 * 
 * @author jgraham
 */
public class EbServletRequest implements R2ServletRequest {
    private final ByteArrayInputStream inputStream;

    public EbServletRequest(byte[] data) {
        inputStream = new ByteArrayInputStream(data);
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
