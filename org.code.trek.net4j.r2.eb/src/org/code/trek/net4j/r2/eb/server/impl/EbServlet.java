/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.server.impl;

import java.io.IOException;

import org.code.trek.net4j.r2.eb.PubSubEventBus;
import org.code.trek.net4j.r2.eb.RL;
import org.code.trek.net4j.r2.eb.client.impl.EbClientRequest;
import org.code.trek.net4j.r2.servlet.R2Handler;
import org.code.trek.net4j.r2.servlet.R2Servlet;
import org.code.trek.net4j.r2.servlet.R2ServletRequest;
import org.code.trek.net4j.r2.servlet.R2ServletResponse;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * Event bus (Eb) request/response servlet.
 * 
 * @author jgraham
 *
 */
public final class EbServlet implements R2Servlet {
    private R2Handler handler;

    public EbServlet(String description) {
        RL rl = new RL(description);
        PubSubEventBus.getInstance().createDomain(rl.getDomain()).createPartition(rl.getPartition());
        PubSubEventBus.getInstance().getDomain(rl.getDomain()).getPartition(rl.getPartition()).register(this);
    }

    @Override
    public void doRequest(R2ServletRequest request, R2ServletResponse response) throws IOException {
        try {
            handler.doRequest(request, response);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleClientRequest(EbClientRequest requestResponse) throws IOException {
        EbServletRequest request = new EbServletRequest(requestResponse.getPayLoad());
        EbServletResponse response = new EbServletResponse(requestResponse);

        doRequest(request, response);
    }

    @Override
    public void setRequestHandler(R2Handler handler) {
        this.handler = handler;
    }
}
