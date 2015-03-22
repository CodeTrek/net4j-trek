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
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * <b>Bundles</b>
 * <p>
 * 
 * The net4j OMPlatform is a runtime container for bundles, where a bundle is net4j's unit of deployment and is similar
 * to an OSGi bundle. The OMPlatform supports installing and running bundles both within an OSGi runtime container and a
 * plain-old-java (POJ) runtime container. Consequently, the OMPlatform abstraction allows net4j bundles to be developed
 * without concern for which runtime is targeted (i.e., OSGi or POJ).
 * <p>
 * 
 * In addition to providing net4j's unit of deployment, the <code>OMBundle</code> interface provides access to net4j's
 * logging and tracing frameworks.
 */
public class OmBundleTest extends TestCase {

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

    /**
     * <b>OMBundle Console Logger</b>
     * <p>
     * 
     * net4j provides a utility log handler called <code>PrintLogHandler.CONSOLE</code> for logging messages to standard
     * out and standard error.
     */
    public void testOmBundleConsoleLogger() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleConsoleLogger", OmBundleTest.class);

        OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);

        OMLogger logger = bundle.logger();

        logger.debug("transporter buffer set to 99% cache efficiency");
        logger.info("shields are at maximum");
        logger.warn("warning, phaser on overload");
        logger.error("error, command code rejected");

        OMPlatform.INSTANCE.removeLogHandler(PrintLogHandler.CONSOLE);
    }

    /**
     * <b>OMBundle Logger</b>
     * <p>
     * 
     * The <code>OMPlatform</code> and bundles work together to provide a logging framework. Custom logging is injected
     * into the framework by calling <code>OMPlatform.INSTANCE.addLogHandler()</code> with a custom log handler. Access
     * to the system logger is provided by the bundle via a call to a bundle's <code>logger()</code> method.
     */
    public void testOmBundleLogger() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleLogger", OmBundleTest.class);

        // Register a custom handler with the platform
        LogHandler logHandler = new LogHandler();
        OMPlatform.INSTANCE.addLogHandler(logHandler);

        // Get the logger from the bundle
        OMLogger logger = bundle.logger();

        logger.debug("debug message");

        assertTrue(logHandler.getMessages().get(0).equals("debug message"));
        assertTrue(logHandler.getLevels().get(0) == Level.DEBUG);
        assertNull(logHandler.getLastThrowable());

        logger.info("info message");

        assertTrue(logHandler.getMessages().get(1).equals("info message"));
        assertTrue(logHandler.getLevels().get(1) == Level.INFO);
        assertNull(logHandler.getLastThrowable());

        logger.warn("warn message");

        assertTrue(logHandler.getMessages().get(2).equals("warn message"));
        assertTrue(logHandler.getLevels().get(2) == Level.WARN);
        assertNull(logHandler.getLastThrowable());

        logger.error("error message");

        assertTrue(logHandler.getMessages().get(3).equals("error message"));
        assertTrue(logHandler.getLevels().get(3) == Level.ERROR);
        assertNull(logHandler.getLastThrowable());

        OMPlatform.INSTANCE.removeLogHandler(logHandler);
    }

    /**
     * <b>OMBundle Tracing</b>
     * <p>
     * 
     * net4j is instrumented with tracer statements that can be turned on to aid in debugging. You can use the same
     * tracer framework to instrument your net4j bundles with debug statements. Like logging, you inject your custom
     * tracer via the <code>OMPlatform</code>.
     * <p>
     * 
     * Note on OSGi bundles: Tracers are named entities that are stored with a bundle's debug options. Bundle debug
     * options are managed by the platform. So if a bundle is deployed into an OSGi platform, tracing is tied into
     * OSGi's <code>org.eclipse.osgi.service.debug.DebugOptions</code> service.
     */
    public void testOmBundleTracer() {
        // Turn tracing on at the platform level
        OMPlatform.INSTANCE.setDebugging(true);

        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleTracer", OmBundleTest.class);

        // Turn tracing on at the bundle level
        bundle.getDebugSupport().setDebugging(true);
        bundle.getDebugSupport().setDebugOption("debug", true);

        // Install a custom tracer
        TraceHandler traceHandler = new TraceHandler();
        OMPlatform.INSTANCE.addTraceHandler(traceHandler);

        // Create a tracer
        OMTracer tracer = bundle.tracer("t1");
        tracer.setEnabled(true);
        if (tracer.isEnabled()) {
            tracer.trace(getClass(), "photon locked");
        }

        System.out.println(traceHandler.getMessages().get(0));
        assertEquals("photon locked", traceHandler.getMessages().get(0));
        OMPlatform.INSTANCE.removeTraceHandler(traceHandler);
    }

    /**
     * <b>OMBundle Tracing Enablement</b>
     * <p>
     * 
     * Use the <code>setEnabled()</code> method to enable/disable a tracer.
     *
     * <p>
     * <b>Note</b>: The system property <code>org.eclipse.net4j.util.om.trace.Tracer.PROP_DISABLE_TRACING</code>
     * enables/disables tracing globally. The value of this constant is
     * <code>org.eclipse.net4j.util.om.trace.disable</code>.
     */
    public void testOmBundlelTracerEnablement() {
        // Turn tracing on at the platform level
        OMPlatform.INSTANCE.setDebugging(true);

        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundlelTracerEnablement", OmBundleTest.class);

        // Turn tracing on at the bundle level
        bundle.getDebugSupport().setDebugging(true);
        bundle.getDebugSupport().setDebugOption("debug", true);

        // Install a custom tracer
        TraceHandler traceHandler = new TraceHandler();
        OMPlatform.INSTANCE.addTraceHandler(traceHandler);

        OMTracer tracer = bundle.tracer("t1");
        tracer.setEnabled(false);
        assertFalse(tracer.isEnabled());

        tracer.setEnabled(true);
        assertTrue(tracer.isEnabled());

        OMPlatform.INSTANCE.removeTraceHandler(traceHandler);
    }

    /**
     * <b>OMBundle Parent/Child Tracers</b>
     * <p>
     * 
     * The tracer framework lets you create tracers with parent/child relationships. Use this feature to create a
     * hierarchy of logically related tracer groupings.
     */
    public void testOmBundleTracerParentChild() {
        // Turn tracing on at the platform level
        OMPlatform.INSTANCE.setDebugging(true);

        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.testOmBundleTracerParentChild", OmBundleTest.class);

        // Turn tracing on at the bundle level
        bundle.getDebugSupport().setDebugging(true);
        bundle.getDebugSupport().setDebugOption("debug", true);

        // Create a hierarchy of tracers separated into categories.

        // comm
        OMTracer commTracer = bundle.tracer("comm");
        System.out.println(commTracer.getFullName());

        // comm.space
        OMTracer spaceTracer = commTracer.tracer("space");
        System.out.println(spaceTracer.getFullName());

        // comm.space.supspace
        OMTracer subSpaceTracer = spaceTracer.tracer("subspace");
        System.out.println(subSpaceTracer.getFullName());

        // comm.vhf
        OMTracer vhfTracer = commTracer.tracer("vhf");
        System.out.println(vhfTracer.getFullName());

        TraceHandler traceHandler = new TraceHandler();
        OMPlatform.INSTANCE.addTraceHandler(traceHandler);

        subSpaceTracer.setEnabled(true);
        subSpaceTracer.trace(getClass(), "sub-space communication level x");

        System.out.println(traceHandler.getMessages().get(0));
        System.out.println(traceHandler.getLastTracer().getFullName());
        System.out.println(traceHandler.getLastContext());

        OMPlatform.INSTANCE.removeTraceHandler(traceHandler);
    }

    @Override
    protected void setUp() throws Exception {
        // Turn debugging off at the platform level
        OMPlatform.INSTANCE.setDebugging(false);

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
