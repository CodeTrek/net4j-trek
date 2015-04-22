/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.AcceptorFactory;

/**
 * A transport acceptor factory.
 */
public class TransportAcceptorFactory extends AcceptorFactory {
    public static final String TYPE = "transport";

    public TransportAcceptorFactory() {
        super(TYPE);
    }

    @Override
    public TransportAcceptor create(String description) throws ProductCreationException {
        TransportAcceptor acceptor = new TransportAcceptor();
        acceptor.setName(description);
        return acceptor;
    }

    @Override
    public String getDescriptionFor(Object acceptor) {
        if (acceptor instanceof TransportAcceptor) {
            return ((TransportAcceptor) acceptor).getName();
        }

        return null;
    }

    public static TransportAcceptor get(IManagedContainer container, String description) {
        return (TransportAcceptor) container.getElement(PRODUCT_GROUP, TYPE, description);
    }
}
