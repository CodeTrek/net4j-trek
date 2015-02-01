/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.internal.jvmx.bundle;

import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.OSGiActivator;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * The <em>Operations and Management (OM)</em> class contains the <em>bundle</em> abstraction.
 * <p>
 * A net4j bundle is the unit of deployment into the net4j <code>OMPlatform</code> runtime - bundles execute within the
 * OMPlatform.
 * <p>
 * Like the OMPlatform, a net4j bundle is an abstraction that accommodates both running a bundle within a legacy
 * platform (plain-old-java execution) or as an OSGi bundle within an OSGi platform/runtime.
 * 
 * @author jgraham
 */
public abstract class OM {

    public static final class Activator extends OSGiActivator {

        public Activator() {
            super(BUNDLE);
        }
    }

    // Matches the OSGi Bundle-SymbolicName
    public static final String BUNDLE_ID = "org.code.trek.net4j.jvmx";

    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, OM.class);

    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMLogger LOG = BUNDLE.logger();
}
