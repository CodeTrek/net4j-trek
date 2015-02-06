/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.net4j.util.om.LegacyUtil;
import org.eclipse.net4j.util.om.OMPlatform;

/**
 * A collection of unit tests used as point of entry "hooks" into the net4j OMPlatform framework.
 */
public class OmPlatformTest extends TestCase {

    public void testOmPlatformSingleton() {
        OMPlatform omPlatform1 = OMPlatform.INSTANCE;
        OMPlatform omPlatform2 = OMPlatform.INSTANCE;

        assertTrue(omPlatform1 == omPlatform2);
        assertTrue(omPlatform1.equals(omPlatform2));
    }

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

    public void testOmPlatformConfigFolder() {
        OMPlatform omPlatform = OMPlatform.INSTANCE;
        System.out.println("Config folder: " + omPlatform.getConfigFolder().getAbsolutePath());
    }

    public void testOmPlatformCommandLineArgs() {
        OMPlatform omPlatform = OMPlatform.INSTANCE;
        LegacyUtil.setCommandLineArgs(new String[] { "arg1", "arg2", "arg3" });

        for (String arg : omPlatform.getCommandLineArgs()) {
            System.out.println("Command line arg: " + arg);
        }
    }
}