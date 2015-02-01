/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.internal.jvmx;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.ConnectorFactory;

public class JvmxConnectorFactory extends ConnectorFactory {

    public static JvmxClientConnector get(IManagedContainer container, String description) {
        return (JvmxClientConnector) container.getElement(PRODUCT_GROUP, TYPE, description);
    }

    public static final String TYPE = "jvmx"; //$NON-NLS-1$

    public JvmxConnectorFactory() {
        super(TYPE);
    }

    public JvmxClientConnector create(String description) throws ProductCreationException {
        JvmxClientConnector connector = new JvmxClientConnector();
        connector.setName(description);
        return connector;
    }

    @Override
    public String getDescriptionFor(Object connector) {
        if (connector instanceof JvmxClientConnector) {
            return ((JvmxClientConnector) connector).getName();
        }

        return null;
    }
}
