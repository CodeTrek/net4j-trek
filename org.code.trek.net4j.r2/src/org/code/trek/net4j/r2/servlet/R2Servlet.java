/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.servlet;

import java.io.IOException;

/**
 * Request/response (R2) servlet capable of responding to R2 client requests.
 * 
 * @author jgraham
 * 
 */
public interface R2Servlet {
    public final String PRODUCT_GROUP = "org.code.trek.net4j.r2.servlet";
    public final String TYPE = "servlet";

    /**
     * Give the R2 servlet an opportunity to perform one-time initialization prior to handling requests.
     */
    public void activate();

    /**
     * Give the R2 servlet an opportunity to release resources.
     */
    public void deactivate();

    /**
     * R2 client request handler.
     * 
     * @param request
     *            the R2 client request.
     * @param response
     *            The R2 servlet response.
     * @throws IOException
     *             if the R2 client request can not be handled.
     */
    public void doRequest(R2ServletRequest request, R2ServletResponse response) throws IOException;

    /**
     * Requests are forwarded to the given handler.
     * 
     * @param handler
     *            the receiver of forwarded requests.
     */
    public void setRequestHandler(R2Handler handler);
}
