package org.code.trek.net4j.internal.jvmx;

import java.text.MessageFormat;

import org.code.trek.net4j.jvmx.IJvmxAcceptor;
import org.eclipse.net4j.util.StringUtil;
import org.eclipse.spi.net4j.Acceptor;

public class JvmxAcceptor extends Acceptor implements IJvmxAcceptor {
    private String name;

    public JvmxAcceptor() {
    }

    public String getName() {
        return name;
    }

    public JvmxServerConnector handleAccept(JvmxClientConnector client) {
        JvmxServerConnector connector = new JvmxServerConnector(client);
        connector.setName(client.getName());
        connector.setConfig(getConfig());
        connector.activate();
        addConnector(connector);
        return connector;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MessageFormat.format("JvmxAcceptor[{0}]", name); //$NON-NLS-1$ 
    }

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        JvmxAcceptorManager.INSTANCE.registerAcceptor(this);
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();
        if (StringUtil.isEmpty(name)) {
            throw new IllegalStateException("No name"); //$NON-NLS-1$
        }
    }

    @Override
    protected void doDeactivate() throws Exception {
        JvmxAcceptorManager.INSTANCE.deregisterAcceptor(this);
        super.doDeactivate();
    }
}
