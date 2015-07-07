/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import org.code.trek.net4j.test.internal.transport.toy.ToyTransportAcceptor;
import org.code.trek.net4j.test.internal.transport.toy.ToyTransportClientConnector;
import org.code.trek.net4j.test.transport.toy.ToyTransportUtil;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.channel.IChannelMultiplexer;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
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
 * An {@link IConnector} is an {@link IChannelMultiplexer}, which is an {@link IChannel} container. An
 * {@link IConnector} also wraps the physical transport used to move data between a client an server.
 */
public class ConnectorTest extends TestCase {

    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.connectortest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, ConnectorTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    @SuppressWarnings("unused")
    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, ConnectorTest.class);

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

    public void testConnect() {

        IManagedContainer container = ContainerUtil.createContainer();
        Net4jUtil.prepareContainer(container);
        ToyTransportUtil.prepareContainer(container);
        container.activate();

        IAcceptor acceptor = Net4jUtil.getAcceptor(container, "org.code.trek.transport", "test.default.1");
        assertTrue(acceptor instanceof ToyTransportAcceptor);

        assertEquals(0, acceptor.getAcceptedConnectors().length);

        IConnector connector = Net4jUtil.getConnector(container, "org.code.trek.transport", "test.default.1");
        assertTrue(connector instanceof ToyTransportClientConnector);

        System.out.println(acceptor.getAcceptedConnectors().length);

        assertEquals(1, acceptor.getAcceptedConnectors().length);

        connector.close();
        acceptor.close();
    }

    public void testDisconnect() {
        IManagedContainer container = ContainerUtil.createContainer();
        Net4jUtil.prepareContainer(container);
        ToyTransportUtil.prepareContainer(container);
        container.activate();

        IAcceptor acceptor = Net4jUtil.getAcceptor(container, "org.code.trek.transport", "test.default.2");
        assertTrue(acceptor instanceof ToyTransportAcceptor);

        assertEquals(0, acceptor.getAcceptedConnectors().length);

        IConnector connector = Net4jUtil.getConnector(container, "org.code.trek.transport", "test.default.2");
        assertTrue(connector instanceof ToyTransportClientConnector);

        System.out.println(acceptor.getAcceptedConnectors().length);

        assertEquals(1, acceptor.getAcceptedConnectors().length);

        connector.close();

        assertEquals(0, acceptor.getAcceptedConnectors().length);

        acceptor.close();
    }

    public void testOpenChannel() {
        IManagedContainer container = ContainerUtil.createContainer();
        Net4jUtil.prepareContainer(container);
        ToyTransportUtil.prepareContainer(container);
        container.activate();

        IAcceptor acceptor = Net4jUtil.getAcceptor(container, "org.code.trek.transport", "test.default.3");
        assertTrue(acceptor instanceof ToyTransportAcceptor);

        IConnector connector = Net4jUtil.getConnector(container, "org.code.trek.transport", "test.default.3");
        assertTrue(connector instanceof ToyTransportClientConnector);

        IChannel channel1 = connector.openChannel();
        channel1.close();
    }
}