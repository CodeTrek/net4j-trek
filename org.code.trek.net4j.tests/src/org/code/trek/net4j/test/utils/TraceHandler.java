/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.net4j.util.om.trace.OMTraceHandler;
import org.eclipse.net4j.util.om.trace.OMTraceHandlerEvent;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * A simple trace handler that tracks trace messages in a list.
 */
public class TraceHandler implements OMTraceHandler {
    private List<String> messages = new ArrayList<String>();
    private OMTracer lastTracer;
    private Class<?> lastContext;

    public Class<?> getLastContext() {
        return lastContext;
    }

    public OMTracer getLastTracer() {
        return lastTracer;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public void traced(OMTraceHandlerEvent event) {
        lastTracer = event.getTracer();
        messages.add(event.getMessage());
        lastContext = event.getContext();
    }
}
