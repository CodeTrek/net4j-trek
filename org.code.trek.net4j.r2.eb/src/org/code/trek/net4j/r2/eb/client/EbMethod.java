/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.code.trek.net4j.r2.client.R2Method;
import org.code.trek.net4j.r2.eb.PubSubEventBus;
import org.code.trek.net4j.r2.eb.RL;

/**
 * EventBus based request/response method.
 * 
 * @author jgraham
 */
public class EbMethod implements R2Method {
    private final int domain;
    private EbClientRequest request;
    private final String partition;

    public EbMethod(int domain) {
        this.domain = domain;
        this.partition = "/";
    }

    public EbMethod(String drlString) {
        RL rl = new RL(drlString);
        this.domain = rl.getDomain();
        this.partition = rl.getPartition();
    }

    @Override
    public int execute() {
        PubSubEventBus.getInstance().getDomain(domain).getPartition(partition).post(request);
        return 0;
    }

    @Override
    public InputStream getResponse(int timeOutMs) {
        byte[] responseData = request.getReponse(timeOutMs);
        return new ByteArrayInputStream(responseData);
    }

    @Override
    public void setPayload(byte[] data) {
        request = new EbClientRequest(data);
    }
}