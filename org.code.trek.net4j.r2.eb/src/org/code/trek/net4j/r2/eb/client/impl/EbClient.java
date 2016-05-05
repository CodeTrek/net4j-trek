/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.client.impl;

import org.code.trek.net4j.r2.client.R2Client;
import org.code.trek.net4j.r2.client.R2Method;

/**
 * An EventBus based request/response client.
 */
public class EbClient implements R2Client {

    public EbClient(String description) {
    }

    @Override
    public int execute(R2Method method) {
        return method.execute();
    }

    @Override
    public R2Method newMethod(String method) {
        return new EbMethod(method);
    }
}
