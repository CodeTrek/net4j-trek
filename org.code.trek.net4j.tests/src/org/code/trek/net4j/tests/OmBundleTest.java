/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map.Entry;
import java.util.Properties;

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
 * <b>Bundles</b>
 * <p>
 * 
 * The OMPlatform is net4j's runtime container. It abstracts the platform used to manage and execute bundles, where a
 * bundle is net4j's unit of deployment and is similar to an OSGi bundle. The OMPlatform supports installing and running
 * bundles both within an OSGi runtime container and a plain-old-java (POJ) runtime container. Consequently, the
 * OMPlatform abstraction allows net4j bundles to be developed without concern for which runtime is targeted (i.e., OSGi
 * or POJ).
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

    /**
     * <b>Bundle Creation</b>
     * <p>
     * 
     * The <code>OMPlatform.INSTANCE</code> singleton provides a factory method for creating bundles.
     */
    public void testOmBundleCreation() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleCreation", OmBundleTest.class);
        assertNotNull(bundle);

        // The platform contains this bundle
        assertTrue(bundle.getPlatform() == OMPlatform.INSTANCE);
        assertTrue(bundle.getBundleID().equals("org.code.trek.testOmBundleCreation"));
    }

    /**
     * <b>Bundle Base URL</b>
     * <p>
     * 
     * A bundle's resources such as its configuration property file and state resources are contained in a resource at
     * the location returned by the <code>getBaseURL()</code> method.
     */
    public void testOmBundleUrl() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleUrl", OmBundleTest.class);
        System.out.println(bundle.getBaseURL());
        System.out.println(bundle.getConfigFile().getPath());
        System.out.println(bundle.getStateLocation());

        // Get the bundle's configuration properties
        Properties properties = bundle.getConfigProperties();

        for (Entry<Object, Object> property : properties.entrySet()) {
            System.out.println(property.getKey() + " = " + property.getValue());
        }

        // Custom bundle resources can be stored in a location relative to the bundle's base URL
        try {
            InputStream istream = bundle.getInputStream("/resources/org.code.trek.testOmBundleUrl.txt");
            InputStreamReader streamReader = new InputStreamReader(istream);
            BufferedReader reader = new BufferedReader(streamReader);
            System.out.println(reader.readLine());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void testOmBundleConsoleLogger() {
        OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
        OMLogger logger = bundle.logger();
        logger.warn("hello, world");
        OMPlatform.INSTANCE.removeLogHandler(PrintLogHandler.CONSOLE);
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
