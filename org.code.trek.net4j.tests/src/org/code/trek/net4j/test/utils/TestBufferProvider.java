/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import org.eclipse.internal.net4j.buffer.Buffer;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.util.om.trace.ContextTracer;

/**
 * {@link IBuffer}s are created by a {@link IBufferProvider}. Here is a simple {@link IBufferProvider}.
 */
@SuppressWarnings("restriction")
public class TestBufferProvider implements IBufferProvider {

    private static final ContextTracer TRACER = new ContextTracer(BufferTest.DEBUG_BUFFER, TestBufferProvider.class);
    private final short capacity;

    /**
     * @param capacity
     *            the size in bytes of the provided {@link IBuffer}s.
     */
    TestBufferProvider(short capacity) {
        this.capacity = capacity;
    }

    @Override
    public short getBufferCapacity() {
        return capacity;
    }

    @Override
    public IBuffer provideBuffer() {
        IBuffer buffer = new Buffer(this, getBufferCapacity());
        if (TRACER.isEnabled()) {
            TRACER.trace("Created " + buffer);
        }

        return buffer;
    }

    @Override
    public void retainBuffer(IBuffer buffer) {
        Buffer internalBuffer = (Buffer) buffer;
        internalBuffer.dispose();
    }
}