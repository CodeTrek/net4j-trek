/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport.toy;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.ConnectorFactory;

/**
 * A transport connector factory.
 */
public class ToyTransportConnectorFactory extends ConnectorFactory {
    public static final String TYPE = "org.code.trek.transport";

    public static ToyTransportClientConnector get(IManagedContainer container, String description) {
        return (ToyTransportClientConnector) container.getElement(PRODUCT_GROUP, TYPE, description);
    }

    public ToyTransportConnectorFactory() {
        super(TYPE);
    }

    @Override
    public ToyTransportClientConnector create(String description) throws ProductCreationException {
        ToyTransportClientConnector connector = new ToyTransportClientConnector();
        connector.setName(description);
        return connector;
    }

    @Override
    public String getDescriptionFor(Object connector) {
        if (connector instanceof ToyTransportClientConnector) {
            return ((ToyTransportClientConnector) connector).getName();
        }

        return null;
    }
}
