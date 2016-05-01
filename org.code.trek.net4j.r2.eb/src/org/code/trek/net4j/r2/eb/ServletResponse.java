/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb;

/**
 * Represents a way for an EventBus based servlet to send a response to a client.
 * 
 * @author jgraham
 *
 */
public interface ServletResponse {
    public void setResponse(byte[] response);
}
