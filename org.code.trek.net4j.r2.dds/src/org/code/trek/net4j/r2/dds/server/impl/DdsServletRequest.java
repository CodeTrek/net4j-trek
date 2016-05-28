/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.server.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.code.trek.net4j.r2.servlet.R2ServletRequest;

/**
 * DDS client request.
 * 
 * @author jgraham
 *
 */
public class DdsServletRequest implements R2ServletRequest {
    private final ByteArrayInputStream inputStream;

    public DdsServletRequest(byte[] data) {
        inputStream = new ByteArrayInputStream(data);
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }
}
