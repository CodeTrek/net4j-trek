package org.code.trek.net4j.internal.jvmx;

import org.code.trek.net4j.jvmx.IJvmxAcceptor;
import org.code.trek.net4j.jvmx.IJvmxAcceptorManager;
import org.eclipse.net4j.util.registry.HashMapRegistry;
import org.eclipse.net4j.util.registry.IRegistry;

public class JvmxAcceptorManager implements IJvmxAcceptorManager {
    // @Singleton
    public static final JvmxAcceptorManager INSTANCE = new JvmxAcceptorManager();

    private IRegistry<String, IJvmxAcceptor> acceptorRegistry = new HashMapRegistry<String, IJvmxAcceptor>();

    public IRegistry<String, IJvmxAcceptor> getAcceptorRegistry() {
        return acceptorRegistry;
    }

    public JvmxAcceptor getAcceptor(String name) {
        return (JvmxAcceptor) acceptorRegistry.get(name);
    }

    public boolean registerAcceptor(JvmxAcceptor acceptor) {
        String name = acceptor.getName();
        if (!acceptorRegistry.containsKey(name)) {
            acceptorRegistry.put(name, acceptor);
            return true;
        }

        return false;
    }

    public boolean deregisterAcceptor(JvmxAcceptor acceptor) {
        return acceptorRegistry.remove(acceptor.getName()) != null;
    }
}
