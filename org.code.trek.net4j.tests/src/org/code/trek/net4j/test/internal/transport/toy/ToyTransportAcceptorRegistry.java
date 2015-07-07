/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport.toy;

import org.code.trek.net4j.test.transport.toy.IToyTransportAcceptor;
import org.eclipse.net4j.util.registry.HashMapRegistry;
import org.eclipse.net4j.util.registry.IRegistry;

/**
 * A singleton that tracks all Transport acceptors.
 */
public class ToyTransportAcceptorRegistry {
    // @Singleton
    public static final ToyTransportAcceptorRegistry INSTANCE = new ToyTransportAcceptorRegistry();

    private IRegistry<String, IToyTransportAcceptor> acceptorRegistry = new HashMapRegistry<String, IToyTransportAcceptor>();

    public boolean deregisterAcceptor(ToyTransportAcceptor acceptor) {
        return acceptorRegistry.remove(acceptor.getName()) != null;
    }

    public ToyTransportAcceptor getAcceptor(String name) {
        return (ToyTransportAcceptor) acceptorRegistry.get(name);
    }

    public boolean registerAcceptor(ToyTransportAcceptor acceptor) {
        String name = acceptor.getName();
        if (!acceptorRegistry.containsKey(name)) {
            acceptorRegistry.put(name, acceptor);
            System.out.println("debug: acceptor registry size: " + acceptorRegistry.size());
            return true;
        }

        return false;
    }
}
