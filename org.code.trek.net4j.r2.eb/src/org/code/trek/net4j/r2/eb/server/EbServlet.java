/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.server;

import java.io.IOException;

import org.code.trek.net4j.r2.eb.client.EbClientRequest;
import org.code.trek.net4j.r2.servlet.R2Servlet;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public abstract class EbServlet implements R2Servlet {
    @Subscribe
    @AllowConcurrentEvents
    public void handleClientRequest(EbClientRequest requestResponse) throws IOException {
        EbServletRequest request = new EbServletRequest(requestResponse.getPayLoad());
        EbServletResponse response = new EbServletResponse(requestResponse);

        doRequest(request, response);
    }
}
