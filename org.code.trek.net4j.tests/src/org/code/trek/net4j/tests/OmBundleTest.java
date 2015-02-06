/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.AbstractLogHandler;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.OMTraceHandler;
import org.eclipse.net4j.util.om.trace.OMTraceHandlerEvent;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * A collection of unit tests used as point of entry "hooks" into the net4j OMBundle framework.
 */
public class OmBundleTest extends TestCase {

    private static class LogHandler extends AbstractLogHandler {
        private String lastMessage;
        private Level lastLevel;
        private Throwable lastThrowable;

        @Override
        protected void writeLog(OMLogger logger, Level level, String msg, Throwable t) throws Throwable {
            lastLevel = level;
            lastMessage = msg;
            lastThrowable = t;
        }

        Level getLastLevel() {
            return lastLevel;
        }

        String getLastMessage() {
            return lastMessage;
        }

        Throwable getLastThrowable() {
            return lastThrowable;
        }
    }

    private static class TraceHandler implements OMTraceHandler {
        private String lastMessage;

        @Override
        public void traced(OMTraceHandlerEvent event) {
            lastMessage = event.getMessage();
        }

        String getLastMessage() {
            return lastMessage;
        }
    }

    private OMBundle bundle;

    private OMTracer levelTracer;

    public void testOmBundleConsoleLogger() {
        OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
        OMLogger logger = bundle.logger();
        logger.warn("hello, world");
        OMPlatform.INSTANCE.removeLogHandler(PrintLogHandler.CONSOLE);
    }

    public void testOmBundleCreation() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.test.bundle.new", OmBundleTest.class);
        assertNotNull(bundle);
    }

    public void testOmBundleLogger() {
        LogHandler logHandler = new LogHandler();
        OMPlatform.INSTANCE.addLogHandler(logHandler);

        OMLogger logger = bundle.logger();

        logger.debug("debug message");

        assertTrue(logHandler.getLastMessage().equals("debug message"));
        assertTrue(logHandler.getLastLevel() == Level.DEBUG);
        assertNull(logHandler.getLastThrowable());

        OMPlatform.INSTANCE.removeLogHandler(logHandler);
    }

    public void testOmBundleTracer() {
        TraceHandler traceHandler = new TraceHandler();
        OMPlatform.INSTANCE.addTraceHandler(traceHandler);

        OMTracer debugTracer = bundle.tracer("debug");
        levelTracer = debugTracer.tracer("level1");
        debugTracer.tracer("level2");

        levelTracer.trace(getClass(), "trace message 1");

        System.out.println(traceHandler.getLastMessage());
        OMPlatform.INSTANCE.removeTraceHandler(traceHandler);
    }

    public void testOmBundleTracerEnablement() {

        // Create a tracer
        OMTracer t1Tracer = bundle.tracer("t1");

        // Tracer's are disabled by default, so enable this one
        t1Tracer.setEnabled(true);

        // Platform and bundle global debugging switches must be turned on before this tracer reports that it's enabled
        assertFalse(t1Tracer.isEnabled());

        // Turn on the global platform debugging switch
        OMPlatform.INSTANCE.setDebugging(true);

        // Not enabled yet...
        assertFalse(t1Tracer.isEnabled());

        // Turn on the bundle's global debugging switch
        bundle.getDebugSupport().setDebugging(true);

        // Enabled now
        assertTrue(t1Tracer.isEnabled());
    }

    public void testOmBundleTracerEnablementParentChild() {
        // Create a parent tracer
        OMTracer p1Tracer = bundle.tracer("p1");

        // Create a child tracer
        OMTracer p1c1Tracer = p1Tracer.tracer("c1");

        // Turn on the global platform and bundle debugging
        OMPlatform.INSTANCE.setDebugging(true);
        bundle.getDebugSupport().setDebugOption("debug", true);
        bundle.getDebugSupport().setDebugging(true);

        // Enabled now
        assertFalse(p1Tracer.isEnabled());
        assertFalse(p1c1Tracer.isEnabled());

        p1Tracer.setEnabled(true);

        assertTrue(p1Tracer.isEnabled());
        assertFalse(p1c1Tracer.isEnabled());

        p1c1Tracer.setEnabled(true);

        assertTrue(p1Tracer.isEnabled());
        assertTrue(p1c1Tracer.isEnabled());

        p1Tracer.setEnabled(false);

        assertFalse(p1Tracer.isEnabled());
        assertTrue(p1c1Tracer.isEnabled());

        System.err.println(p1c1Tracer.getFullName());
    }

    @Override
    protected void setUp() throws Exception {
        // Turn debugging off at the platform level
        OMPlatform.INSTANCE.setDebugging(false);

        bundle = OMPlatform.INSTANCE.bundle("org.code.trek.test.bundle", OmBundleTest.class);
        bundle.getDebugSupport().setDebugOption("debug", false);
        bundle.getDebugSupport().setDebugging(false);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
