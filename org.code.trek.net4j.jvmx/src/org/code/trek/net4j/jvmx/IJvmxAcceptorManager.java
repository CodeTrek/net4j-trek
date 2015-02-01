package org.code.trek.net4j.jvmx;

import org.code.trek.net4j.internal.jvmx.JvmxAcceptorManager;
import org.eclipse.net4j.util.registry.IRegistry;

/**
 * A singleton that manages all {@link IJVMAcceptor JVM acceptors} in the current JVM.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IJvmxAcceptorManager {
    public static final IJvmxAcceptorManager INSTANCE = JvmxAcceptorManager.INSTANCE;

    public IRegistry<String, IJvmxAcceptor> getAcceptorRegistry();

    public IJvmxAcceptor getAcceptor(String name);
}
