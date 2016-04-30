/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.servlet;

import java.io.InputStream;

/**
 * Provides request/response (R2) client request information to an R2 servlet.
 * 
 * @author jgraham
 */
public interface R2ServletRequest {

    /**
     * Retrieves the R2 client request information.
     * 
     * @return an input stream object containing the R2 client request information.
     */
    public InputStream getInputStream();
}
