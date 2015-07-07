/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport.toy;

import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.ProductCreationException;
import org.eclipse.spi.net4j.AcceptorFactory;

/**
 * A transport acceptor factory.
 */
public class ToyTransportAcceptorFactory extends AcceptorFactory {
    public static final String TYPE = "org.code.trek.transport";

    public static ToyTransportAcceptor get(IManagedContainer container, String description) {
        return (ToyTransportAcceptor) container.getElement(PRODUCT_GROUP, TYPE, description);
    }

    public ToyTransportAcceptorFactory() {
        super(TYPE);
    }

    @Override
    public ToyTransportAcceptor create(String description) throws ProductCreationException {
        ToyTransportAcceptor acceptor = new ToyTransportAcceptor();
        acceptor.setName(description);
        return acceptor;
    }

    @Override
    public String getDescriptionFor(Object acceptor) {
        if (acceptor instanceof ToyTransportAcceptor) {
            return ((ToyTransportAcceptor) acceptor).getName();
        }

        return null;
    }
}
