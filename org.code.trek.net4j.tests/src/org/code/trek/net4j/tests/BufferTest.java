/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.nio.ByteBuffer;

import org.eclipse.net4j.buffer.BufferState;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
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
 * These tests explore the net4j {@link IBuffer} concept.
 * <p>
 * 
 * {@link IBuffer} appears similar to the {@link java.nio.Buffer}. The most basic purpose of an {@link IBuffer} is to
 * represent a fixed array of bytes. An important attribute of a {@link IBuffer} is that it's associated with an
 * {@link IChannel}, which is used to carry a buffer's data between consumer and supplier.
 * <p>
 * 
 */
public class BufferTest extends TestCase {

    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.buffertest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, BufferTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");
    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, BufferTest.class);

    /**
     * Buffer providers determines a buffer's capacity. Buffer capacities are fixed.
     */
    public void testBufferCapacity() {
        // Buffers are created via a buffer provider that sets the buffer's
        // capacity.
        IBufferProvider bufferProvider = new TestBufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        // Buffers have a finite capacity
        assertEquals(16, buffer.getCapacity());
    }

    /**
     * Assign a buffer to a channel when you put the buffer in a state to receive data.
     */
    public void testBufferChannelAssignment() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 3;

        // Put the buffer in a state to receive data and associate a channel id
        // with the buffer.
        buffer.startPutting(CHANNEL_ID);

        assertEquals(CHANNEL_ID, buffer.getChannelID());
    }

    public void testBufferGetData() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 64);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 11;
        ByteBuffer payload = buffer.startPutting(CHANNEL_ID);

        int initPosition = payload.position();

        payload.putShort((short) 3);
        payload.putShort((short) 5);
        payload.putShort((short) 7);

        payload.position(initPosition);

        assertEquals(3, payload.getShort());
        assertEquals(5, payload.getShort());
        assertEquals(7, payload.getShort());
    }

    public void testBufferGetFlip() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 64);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 11;
        ByteBuffer payload = buffer.startPutting(CHANNEL_ID);

        payload.putShort((short) 3);
        payload.putShort((short) 5);
        payload.putShort((short) 7);

        TEST_TRACER.trace("position: " + payload.position());
        assertEquals(10, payload.position());

        buffer.flip();

        TEST_TRACER.trace("position: " + payload.position());
        assertEquals(IBuffer.HEADER_SIZE, payload.position());
    }

    /**
     * The byte buffer used to hold a buffer's data has various properties.
     */
    public void testBufferPayloadProperties() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 7;

        // Putting the buffer in a state to receive data returns the byte buffer
        // used to manage a buffer's data payload.
        ByteBuffer payload = buffer.startPutting(CHANNEL_ID);

        TEST_TRACER.trace("hasRemaining: " + payload.hasRemaining());
        TEST_TRACER.trace("hasArray: " + payload.hasArray());

        TEST_TRACER.trace("isDirect: " + payload.isDirect());
        TEST_TRACER.trace("isReadOnly: " + payload.isReadOnly());

        // The buffer is positioned past the header?
        assertEquals(IBuffer.HEADER_SIZE, payload.position());

        TEST_TRACER.trace("position: " + payload.position());
        TEST_TRACER.trace("capacity: " + payload.capacity());
        TEST_TRACER.trace("limit: " + payload.limit());
        TEST_TRACER.trace("remaining: " + payload.remaining());
    }

    public void testBufferPutData() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 64);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 11;
        ByteBuffer payload = buffer.startPutting(CHANNEL_ID);

        TEST_TRACER.trace("position: " + payload.position());

        payload.putShort((short) 3);
        TEST_TRACER.trace("position: " + payload.position());
        payload.putShort((short) 5);
        TEST_TRACER.trace("position: " + payload.position());
        payload.putShort((short) 7);
        TEST_TRACER.trace("position: " + payload.position());
    }

    public void testBufferStates() {
        IBufferProvider bufferProvider = new TestBufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        TEST_TRACER.trace(buffer.getState().toString());
        assertEquals(BufferState.INITIAL, buffer.getState());

        final short CHANNEL_ID = 11;
        buffer.startPutting(CHANNEL_ID);

        TEST_TRACER.trace(buffer.getState().toString());
        assertEquals(BufferState.PUTTING, buffer.getState());

        buffer.flip();

        TEST_TRACER.trace(buffer.getState().toString());
        assertEquals(BufferState.GETTING, buffer.getState());

        buffer.release();

        TEST_TRACER.trace(buffer.getState().toString());
        assertEquals(BufferState.DISPOSED, buffer.getState());
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