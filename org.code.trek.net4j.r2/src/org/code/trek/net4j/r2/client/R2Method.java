/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.r2.client;

import java.io.InputStream;

/**
 * Represents a request to be sent to a server and a corresponding response.
 * 
 * @author jgraham
 */
public interface R2Method {

    /**
     * Make a request.
     * 
     * @return the result of the execute.
     */
    public int execute();

    /**
     * Returns the response for the most recent execution of this request.
     * 
     * @param timeOutMs
     *            the maximum time to wait for a response in milliseconds.
     * 
     * @return the response from the most recent request, null if no request has been made or the maximum time to wait
     *         expires.
     */
    public InputStream getResponse(int timeOutMs);

    /**
     * Set the data to be delivered with a request.
     * 
     * @param payload
     *            the request's data payload.
     */
    public void setPayload(byte[] payload);
}
