/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.code.trek.net4j.test.transport.ITransportAcceptor;
import org.eclipse.net4j.util.registry.HashMapRegistry;
import org.eclipse.net4j.util.registry.IRegistry;

/**
 * A singleton that tracks all Transport acceptors.
 */
public class TransportAcceptorRegistry {
    // @Singleton
    public static final TransportAcceptorRegistry INSTANCE = new TransportAcceptorRegistry();

    private IRegistry<String, ITransportAcceptor> acceptorRegistry = new HashMapRegistry<String, ITransportAcceptor>();

    public TransportAcceptor getAcceptor(String name) {
        return (TransportAcceptor) acceptorRegistry.get(name);
    }

    public boolean registerAcceptor(TransportAcceptor acceptor) {
        String name = acceptor.getName();
        if (!acceptorRegistry.containsKey(name)) {
            acceptorRegistry.put(name, acceptor);
            System.out.println("debug: acceptor registry size: " + acceptorRegistry.size());
            return true;
        }

        return false;
    }

    public boolean deregisterAcceptor(TransportAcceptor acceptor) {
        return acceptorRegistry.remove(acceptor.getName()) != null;
    }
}
