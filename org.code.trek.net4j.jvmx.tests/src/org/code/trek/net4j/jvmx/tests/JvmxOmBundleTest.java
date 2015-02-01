package org.code.trek.net4j.jvmx.tests;

import org.code.trek.net4j.internal.jvmx.bundle.OM;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.tests.AbstractOMTest;

/**
 * 
 */
public class JvmxOmBundleTest extends AbstractOMTest {

    public void testOmBundlePlatform() {
        OMBundle omBundle = OM.BUNDLE;
        assertNotNull(omBundle);
        assertEquals(false, omBundle.getPlatform().isOSGiRunning());
    }
}
