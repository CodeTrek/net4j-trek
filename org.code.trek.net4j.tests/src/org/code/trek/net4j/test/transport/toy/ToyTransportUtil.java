/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.transport.toy;

import org.code.trek.net4j.test.internal.transport.toy.ToyTransportAcceptorFactory;
import org.code.trek.net4j.test.internal.transport.toy.ToyTransportConnectorFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * Registers the test transport acceptor and connector factories with a managed container.
 */
public final class ToyTransportUtil {
    public static IToyTransportAcceptor getAcceptor(IManagedContainer container, String description) {
        return (IToyTransportAcceptor) container.getElement(ToyTransportAcceptorFactory.PRODUCT_GROUP,
                ToyTransportAcceptorFactory.TYPE, description);
    }

    public static IToyTransportConnector getConnector(IManagedContainer container, String description) {
        return (IToyTransportConnector) container.getElement(ToyTransportConnectorFactory.PRODUCT_GROUP,
                ToyTransportConnectorFactory.TYPE, description);
    }

    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new ToyTransportAcceptorFactory());
        container.registerFactory(new ToyTransportConnectorFactory());
    }

    private ToyTransportUtil() {
    }
}
