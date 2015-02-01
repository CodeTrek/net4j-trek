/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.jvmx;

import org.code.trek.net4j.internal.jvmx.JvmxAcceptorFactory;
import org.code.trek.net4j.internal.jvmx.JvmxConnectorFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * A collection of utility functions.
 * 
 * @author jgraham
 *
 */
public final class JvmxUtil {
    public static IJvmxAcceptor getAcceptor(IManagedContainer container, String description) {
        return (IJvmxAcceptor) container.getElement(JvmxAcceptorFactory.PRODUCT_GROUP, JvmxAcceptorFactory.TYPE,
                description);
    }

    public static IJvmxConnector getConnector(IManagedContainer container, String description) {
        return (IJvmxConnector) container.getElement(JvmxConnectorFactory.PRODUCT_GROUP, JvmxConnectorFactory.TYPE,
                description);
    }

    /**
     * Prepare the net4j container to support this transport.
     * <p>
     * 
     * @param container
     *            a net4j runtime container
     */
    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new JvmxAcceptorFactory());
        container.registerFactory(new JvmxConnectorFactory());
    }

    private JvmxUtil() {
    }
}
