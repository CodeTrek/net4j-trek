package org.code.trek.net4j.internal.jvmx;

import java.text.MessageFormat;

import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

public class JvmxClientConnector extends JvmxConnector {
    private JvmxAcceptor acceptor;

    public JvmxClientConnector() {
    }

    public JvmxAcceptor getAcceptor() {
        return acceptor;
    }

    @Override
    public Location getLocation() {
        return Location.CLIENT;
    }

    @Override
    public String toString() {
        if (getUserID() == null) {
            return MessageFormat.format("JvmxClientConnector[{0}]", getName()); //$NON-NLS-1$
        }

        return MessageFormat.format("JvmxClientConnector[{1}@{0}]", getName(), getUserID()); //$NON-NLS-1$
    }

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        JvmxConnector peer = acceptor.handleAccept(this);
        setPeer(peer);
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();
        String name = getName();
        acceptor = JvmxAcceptorManager.INSTANCE.getAcceptor(name);
        if (acceptor == null) {
            throw new IllegalStateException("Jvmx acceptor not found: " + name); //$NON-NLS-1$
        }
    }

    @Override
    protected void doDeactivate() throws Exception {
        LifecycleUtil.deactivateNoisy(getPeer());
        super.doDeactivate();
    }
}
