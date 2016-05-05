/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.r2.client;

/**
 * A request/response client capable of executing request methods.
 * 
 * @author jgraham
 */
public interface R2Client {
    public final String PRODUCT_GROUP = "org.code.trek.net4j.r2.client";
    public final String TYPE = "client";

    /**
     * Make a request by executing the given {@link R2Method}.
     * 
     * @param method
     *            the method to execute.
     * @return the result of executing the method.
     */
    public int execute(R2Method method);

    /**
     * Constructs a client request method.
     * 
     * @param method
     *            the description used to construct the method.
     * @return a new client request method.
     */
    public R2Method newMethod(String method);
}
