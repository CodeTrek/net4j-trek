/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.nio.ByteBuffer;

import org.code.trek.net4j.test.internal.transport.toy.ToyTransportAcceptor;
import org.code.trek.net4j.test.internal.transport.toy.ToyTransportChannel;
import org.code.trek.net4j.test.internal.transport.toy.ToyTransportClientConnector;
import org.code.trek.net4j.test.transport.toy.ToyTransportUtil;
import org.code.trek.net4j.test.utils.TestBufferProvider;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferHandler;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.om.trace.OMTracer;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.junit.Assert;

import junit.framework.TestCase;

/**
 * <b>Buffers</b>
 * <p>
 * 
 * These tests explore the net4j {@link IChannel} concept.
 * <p>
 * 
 * A channel is used to transfer buffer content between two peers.
 * 
 * 
 */
public class ChannelTest extends TestCase {

    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.channeltest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, ChannelTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    @SuppressWarnings("unused")
    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, ChannelTest.class);

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

    /**
     * 
     */
    public void testChannelSendReceiveData() {
        IManagedContainer container = ContainerUtil.createContainer();
        Net4jUtil.prepareContainer(container);
        ToyTransportUtil.prepareContainer(container);
        container.activate();

        IAcceptor acceptor = Net4jUtil.getAcceptor(container, "org.code.trek.transport", "test.default.1");
        assertTrue(acceptor instanceof ToyTransportAcceptor);

        assertEquals(0, acceptor.getAcceptedConnectors().length);
        
        acceptor.addListener(new IListener() {

            @Override
            public void notifyEvent(IEvent event) {
                System.out.println(event);
            }});

        IConnector connector = Net4jUtil.getConnector(container, "org.code.trek.transport", "test.default.1");
        assertTrue(connector instanceof ToyTransportClientConnector);

        System.out.println(acceptor.getAcceptedConnectors().length);

        assertEquals(1, acceptor.getAcceptedConnectors().length);
        
        IChannel clientChannel = connector.openChannel();
        
        IBufferProvider bufferProvider = new TestBufferProvider((short) 64);
        IBuffer buffer = bufferProvider.provideBuffer();

        ByteBuffer payload = buffer.startPutting(clientChannel.getID());

        payload.putShort((short) 3);
        payload.putShort((short) 5);
        payload.putShort((short) 7);
        
        clientChannel.sendBuffer(buffer);

        connector.close();
        acceptor.close();
    }

    /**
     * Buffer providers determines a buffer's capacity. Buffer capacities are fixed.
     */
    public void testChannel() {
        // Create a l
        final short CHANNEL_ID = 11;

        ToyTransportChannel peer1 = new ToyTransportChannel();
        peer1.setID(CHANNEL_ID);

        ToyTransportChannel peer2 = new ToyTransportChannel();
        peer2.setID(CHANNEL_ID);

        peer1.setPeer(peer2);
        peer2.setPeer(peer1);

        peer2.setReceiveHandler(new IBufferHandler() {

            @Override
            public void handleBuffer(IBuffer buffer) {
                System.out.println("buffer: channel: " + buffer.getChannelID());
                System.out.println("buffer: capacity: " + buffer.getCapacity());
            }
        });

        IBufferProvider bufferProvider = new TestBufferProvider((short) 64);
        IBuffer buffer = bufferProvider.provideBuffer();

        ByteBuffer payload = buffer.startPutting(CHANNEL_ID);

        int initPosition = payload.position();

        payload.putShort((short) 3);
        payload.putShort((short) 5);
        payload.putShort((short) 7);

        peer1.sendBuffer(buffer);
        
        Assert.assertEquals(6, (payload.position() - initPosition));
    }
}