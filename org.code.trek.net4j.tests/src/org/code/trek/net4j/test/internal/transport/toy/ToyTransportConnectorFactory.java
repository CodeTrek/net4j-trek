/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.ConnectorFactory;

/**
 * A transport connector factory.
 */
public class TransportConnectorFactory extends ConnectorFactory {
    public static final String TYPE = "org.code.trek.transport";

    public TransportConnectorFactory() {
        super(TYPE);
    }

    @Override
    public TransportClientConnector create(String description) throws ProductCreationException {
        TransportClientConnector connector = new TransportClientConnector();
        connector.setName(description);
        return connector;
    }

    @Override
    public String getDescriptionFor(Object connector) {
        if (connector instanceof TransportClientConnector) {
            return ((TransportClientConnector) connector).getName();
        }

        return null;
    }

    public static TransportClientConnector get(IManagedContainer container, String description) {
        return (TransportClientConnector) container.getElement(PRODUCT_GROUP, TYPE, description);
    }
}
