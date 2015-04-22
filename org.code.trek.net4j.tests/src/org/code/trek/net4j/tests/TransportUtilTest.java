/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.code.trek.net4j.test.transport.TransportUtil;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.util.ReflectUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.om.trace.OMTracer;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

/**
 * <b>Transport Utilities</b>
 * <p>
 * 
 * <p>
 * 
 */
public class TransportUtilTest extends TestCase {

    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.transportutiltest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, TransportUtilTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, TransportUtilTest.class);

    public void testUtil() {
        IManagedContainer container = ContainerUtil.createContainer();

        // Initialize the container for use with the JVM protocol and activate it.
        Net4jUtil.prepareContainer(container);
        TransportUtil.prepareContainer(container);

        ReflectUtil.dump(container);

        // container.activate();
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