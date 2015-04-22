/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import org.code.trek.net4j.test.internal.transport.TransportAcceptor;
import org.code.trek.net4j.test.internal.transport.TransportClientConnector;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.channel.IChannelMultiplexer;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.om.trace.OMTracer;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

import junit.framework.TestCase;

/**
 * <b>Buffers</b>
 * <p>
 * 
 * These tests explore the net4j {@link IConnector} concept.
 * <p>
 * 
 * An {@link IConnector} is an {@link IChannelMultiplexer}, which an {@link IChannel} container. An {@link IConnector}
 * also wraps the physical transport used to move data between a client an server.
 */
public class ConnectorTest extends TestCase {

    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.connectortest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, ConnectorTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, ConnectorTest.class);

    public void testConnector() {
        // Create a server-side acceptor
        IAcceptor acceptor = new TransportAcceptor();

        // Create a client-side connector
        IConnector connector = new TransportClientConnector();
    }

    @Override
    protected void setUp() throws Exception {
        // Turn tracing on
        OMPlatform.INSTANCE.setDebugging(true);
        BUNDLE.getDebugSupport().setDebugOption("debug", true);
        DEBUG.setEnabled(true);
        DEBUG_BUFFER.setEnabled(true);

        OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        OMPlatform.INSTANCE.removeTraceHandler(PrintTraceHandler.CONSOLE);
        DEBUG.setEnabled(false);
        DEBUG_BUFFER.setEnabled(false);
        BUNDLE.getDebugSupport().setDebugOption("debug", true);
        OMPlatform.INSTANCE.setDebugging(false);

        super.tearDown();
    }
}