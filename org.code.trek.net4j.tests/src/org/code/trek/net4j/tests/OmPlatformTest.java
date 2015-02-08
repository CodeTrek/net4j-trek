/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import java.util.Properties;

import junit.framework.TestCase;

import org.eclipse.net4j.util.om.LegacyUtil;
import org.eclipse.net4j.util.om.OMPlatform;

/**
 * These tests explore net4j's "Operations & Maintenance" (OM) framework concepts. This framework includes both
 * foundational (e.g., bundle) and cross-cutting (e.g., logging and tracking) abstractions.
 * <p>
 * 
 * According to the documentation <a href='https://wiki.eclipse.org/Net4j'>here</a>, the OM framework provides the
 * following abstractions:
 *
 * <ul>
 * <li>Platform abstraction (OSGi, Eclipse runtime, stand alone)</li>
 * <li>Bundle</li>
 * <li>Logging</li>
 * <li>Tracing</li>
 * <li>Preferences</li>
 * </ul>
 * 
 * and at least one other sub-framework called the "Progress monitoring framework".
 */
public class OmPlatformTest extends TestCase {

    /**
     * <b>Singleton Pattern</b>
     * <p>
     * The interface to net4j's OM framework is specified here: {@link org.eclipse.net4j.util.om.OMPlatform}.
     * Implementors of this interface are either abstract classes or classes with private constructors, enabling the
     * <code>OMPlatform</code> interface to enforce a singleton pattern via its <code>INSTANCE</code> member variable.
     */
    public void testOmPlatformSingleton() {
        OMPlatform omPlatform1 = OMPlatform.INSTANCE;
        OMPlatform omPlatform2 = OMPlatform.INSTANCE;

        assertTrue(omPlatform1 == omPlatform2);
        assertTrue(omPlatform1.equals(omPlatform2));
    }

    /**
     * <b>OSGi vs Legacy Platform Creation</b>
     * <p>
     * 
     * If net4j is running inside of an OSGi runtime container, then activation of the {@link org.eclipse.net4j.util}
     * bundle injects the bundle context into {@link org.eclipse.net4j.internal.util.bundle.AbstractPlatform}. If a
     * bundle context is present when the <code>OMPlatform</code> is created (by calling
     * <code>OMPlatform.INSTANCE</code>), then an OSGi platform is created, otherwise a legacy platform is created.
     */
    public void testOmPlatformLegacy() {
        OMPlatform omPlatform = OMPlatform.INSTANCE;

        // This unit test is not running inside an OSGi container, therefore the OM platform defaults the "legacy",
        // meaning the plain-old-java (POJ) runtime and no eclipse OSGi platform extension registry is available.
        assertFalse(omPlatform.isOSGiRunning());
        assertFalse(omPlatform.isExtensionRegistryAvailable());
    }

    public void testOmPlatformStateFolder() {
        OMPlatform omPlatform = OMPlatform.INSTANCE;
        System.out.println("State folder: " + omPlatform.getStateFolder().getAbsolutePath());
    }

    /**
     * <b>OM Platform Configuration Folder</b>
     * <p>
     * 
     * The system property <code>net4j.config</code> designates the folder used to hold net4j OMPlatform configuration
     * property files.
     */
    public void testOmPlatformConfigFolder() {
        System.setProperty("net4j.config", ".");
        OMPlatform omPlatform = OMPlatform.INSTANCE;
        Properties pluginProperties = omPlatform.getConfigProperties("omPlatformTest.properties");
        String property1Value = pluginProperties.getProperty("property1");
        String property2Value = pluginProperties.getProperty("property2");
        System.out.println(property1Value + ", " + property2Value);
    }

    public void testOmPlatformCommandLineArgs() {
        OMPlatform omPlatform = OMPlatform.INSTANCE;
        LegacyUtil.setCommandLineArgs(new String[] { "arg1", "arg2", "arg3" });

        for (String arg : omPlatform.getCommandLineArgs()) {
            System.out.println("Command line arg: " + arg);
        }
    }
}