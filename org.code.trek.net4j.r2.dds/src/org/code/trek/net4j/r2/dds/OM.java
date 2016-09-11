/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds;

import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.OSGiActivator;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.trace.OMTracer;

/**
 * The <em>Operations & Maintenance</em> class of this bundle.
 */
public abstract class OM {
    public static final class Activator extends OSGiActivator {
        public Activator() {
            super(BUNDLE);
        }
    }

    public static final String BUNDLE_ID = "org.code.trek.net4j.r2.dds";
    public static final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, OM.class);
    public static final OMTracer DEBUG = BUNDLE.tracer("debug");

    public static final OMLogger LOG = BUNDLE.logger();
}
