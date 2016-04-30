/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.servlet;

import java.io.OutputStream;

/**
 * A request/response (R2) servlet response to an R2 client.
 * 
 * @author jgraham
 */
public interface R2ServletResponse {

    /**
     * Provides an object for an R2 servlet to send a response to an R2 client.
     * 
     * @return an output stream object used to deliver an R2 servlet response to an R2 client.
     */
    public OutputStream getOutputStream();
}
