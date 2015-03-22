/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.internal.net4j.buffer.Buffer;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.om.trace.OMTracer;

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

    /**
     * {@link IBuffer}s are created by a {@link IBufferProvider}. Here is a simple {@link IBufferProvider}.
     */
    public static class BufferProvider implements IBufferProvider {

        private static final ContextTracer TRACER = new ContextTracer(DEBUG_BUFFER, BufferProvider.class);
        private final short capacity;

        /**
         * @param capacity
         *            the size in bytes of the provided {@link IBuffer}s.
         */
        BufferProvider(short capacity) {
            this.capacity = capacity;
        }

        @Override
        public short getBufferCapacity() {
            return capacity;
        }

        @Override
        public IBuffer provideBuffer() {
            @SuppressWarnings("restriction")
            IBuffer buffer = new Buffer(this, getBufferCapacity());
            if (TRACER.isEnabled()) {
                TRACER.trace("Created " + buffer);
            }

            return buffer;
        }

        @SuppressWarnings("restriction")
        @Override
        public void retainBuffer(IBuffer buffer) {
            Buffer internalBuffer = (Buffer) buffer;
            internalBuffer.dispose();
        }
    }

    public void testBufferCapacity() {
        // Buffers are created via a buffer provider that sets the buffer's capacity.
        IBufferProvider bufferProvider = new BufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        // Buffers have a finite capacity
        assertEquals(16, buffer.getCapacity());
    }

    public void testBufferChannelAssignment() {
        IBufferProvider bufferProvider = new BufferProvider((short) 16);
        IBuffer buffer = bufferProvider.provideBuffer();

        final short CHANNEL_ID = 3;

        // Put the buffer in a state to receive data and associate a channel with the buffer.
        buffer.startPutting(CHANNEL_ID);

        assertEquals(CHANNEL_ID, buffer.getChannelID());
    }
}