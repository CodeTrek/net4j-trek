/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.net4j.util.om.trace.OMTraceHandler;
import org.eclipse.net4j.util.om.trace.OMTraceHandlerEvent;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * A simple trace handler that tracks trace messages in a list.
 */
class TraceHandler implements OMTraceHandler {
    private List<String> messages = new ArrayList<String>();
    private OMTracer lastTracer;
    private Class<?> lastContext;

    @Override
    public void traced(OMTraceHandlerEvent event) {
        lastTracer = event.getTracer();
        messages.add(event.getMessage());
        lastContext = event.getContext();
    }

    List<String> getMessages() {
        return messages;
    }

    OMTracer getLastTracer() {
        return lastTracer;
    }

    Class<?> getLastContext() {
        return lastContext;
    }
}
