/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.util.List;

import org.code.trek.net4j.test.transport.TransportUtil;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.util.ReflectUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IElementProcessor;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.IFactory;
import org.eclipse.net4j.util.factory.IFactoryKey;
import org.eclipse.net4j.util.lifecycle.LifecycleException;
import org.eclipse.net4j.util.lifecycle.LifecycleState;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.om.trace.OMTracer;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;
import org.eclipse.net4j.util.registry.IRegistry;

import junit.framework.TestCase;

/**
 * <b>Test Transport Utilities</b>
 * <p>
 * An {@link IManagedContainer} is the part of net4j's runtime infrastructure that is responsible for managing a set of
 * factories and post-processors.
 * 
 * <p>
 * An {@link IManagedContainer} maintains three main registries:
 * <ul>
 * <li>Factory Registry</li>
 * <li>Post Processor Registry</li>
 * <li>Element Registry</li>
 * </ul>
 * 
 * <p>
 * The net4j runtime consults an {@link IManagedContainer} to obtain the factories used to construct
 * {@link org.eclipse.net4j.acceptor.IAcceptor IAcceptor}s and {@link org.eclipse.net4j.connector.IConnector IConnector}
 * s.
 * 
 */
public class TransportUtilTest extends TestCase {
    public static final String BUNDLE_ID = "org.code.trek.net4j.tests.transportutiltest";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, TransportUtilTest.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMTracer DEBUG_BUFFER = DEBUG.tracer("buffer");

    private static final ContextTracer TEST_TRACER = new ContextTracer(DEBUG, TransportUtilTest.class);

    public void testDefaultManagedContainerState() {
        IManagedContainer container = ContainerUtil.createContainer();
        assertEquals(LifecycleState.INACTIVE, container.getLifecycleState());
        System.out.println("container default lifecycle state: " + container.getLifecycleState().name());
    }

    public void testDefaultManagedContainerContent() {
        IManagedContainer container = ContainerUtil.createContainer();
        ReflectUtil.dump(container);

        IRegistry<IFactoryKey, IFactory> factoryRegistry = container.getFactoryRegistry();
        assertTrue(factoryRegistry.isEmpty());

        List<IElementProcessor> postProcessorRegistry = container.getPostProcessors();
        assertTrue(postProcessorRegistry.isEmpty());

        // Accessing the element registry prior to container activation results in a life cycle exception
        try {
            @SuppressWarnings("unused")
            Object[] elementRegistry = container.getElements();
            fail();
        } catch (LifecycleException e) {
            System.out.println(e);
        }
    }

    public void testDefaultManagedContainerContentAfterActivation() {
        IManagedContainer container = ContainerUtil.createContainer();
        container.activate();
        ReflectUtil.dump(container);

        IRegistry<IFactoryKey, IFactory> factoryRegistry = container.getFactoryRegistry();
        assertTrue(factoryRegistry.isEmpty());

        List<IElementProcessor> postProcessorRegistry = container.getPostProcessors();
        assertTrue(postProcessorRegistry.isEmpty());

        Object[] elementRegistry = container.getElements();
        assertTrue(elementRegistry.length == 0);
    }

    public void testNet4jUtilPrepareContainer() {
        IManagedContainer container = ContainerUtil.createContainer();

        // Initialize the container for use with default contents
        Net4jUtil.prepareContainer(container);
        container.activate();

        assertFalse(container.getFactoryRegistry().isEmpty());
        assertFalse(container.getPostProcessors().isEmpty());
        assertTrue(container.getElements().length == 0);
    }

    public void testTransportUtilPrepareContainer() {
        IManagedContainer container = ContainerUtil.createContainer();

        // Initialize the container for use with the test transport and activate it.
        Net4jUtil.prepareContainer(container);
        TransportUtil.prepareContainer(container);

        container.activate();

        IFactory acceptorFactory = container.getFactory("org.eclipse.net4j.acceptors", "org.code.trek.transport");
        assertNotNull(acceptorFactory);

        IFactory connectorFactory = container.getFactory("org.eclipse.net4j.connectors", "org.code.trek.transport");
        assertNotNull(connectorFactory);
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