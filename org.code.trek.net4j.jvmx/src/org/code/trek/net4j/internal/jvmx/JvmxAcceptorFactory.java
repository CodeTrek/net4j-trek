/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.internal.jvmx;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.AcceptorFactory;

public class JvmxAcceptorFactory extends AcceptorFactory {

    public static JvmxAcceptor get(IManagedContainer container, String description) {
        return (JvmxAcceptor) container.getElement(PRODUCT_GROUP, TYPE, description);
    }

    public static final String TYPE = "jvmx"; //$NON-NLS-1

    public JvmxAcceptorFactory() {
        super(TYPE);
    }

    public JvmxAcceptor create(String description) throws ProductCreationException {
        JvmxAcceptor acceptor = new JvmxAcceptor();
        acceptor.setName(description);
        return acceptor;
    }

    @Override
    public String getDescriptionFor(Object acceptor) {
        if (acceptor instanceof JvmxAcceptor) {
            return ((JvmxAcceptor) acceptor).getName();
        }

        return null;
    }
}
