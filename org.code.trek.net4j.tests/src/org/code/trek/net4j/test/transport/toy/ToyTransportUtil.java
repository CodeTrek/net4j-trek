/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.transport;

import org.code.trek.net4j.test.internal.transport.TransportAcceptorFactory;
import org.code.trek.net4j.test.internal.transport.TransportConnectorFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * Registers the test transport acceptor and connector factories with a managed container.
 */
public final class TransportUtil {
    private TransportUtil() {
    }

    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new TransportAcceptorFactory());
        container.registerFactory(new TransportConnectorFactory());
    }

    public static ITransportAcceptor getAcceptor(IManagedContainer container, String description) {
        return (ITransportAcceptor) container.getElement(TransportAcceptorFactory.PRODUCT_GROUP,
                TransportAcceptorFactory.TYPE, description);
    }

    public static ITransportConnector getConnector(IManagedContainer container, String description) {
        return (ITransportConnector) container.getElement(TransportConnectorFactory.PRODUCT_GROUP,
                TransportConnectorFactory.TYPE, description);
    }
}
