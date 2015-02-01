package org.code.trek.net4j.jvmx.tests;

import org.code.trek.net4j.jvmx.JvmxUtil;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.tests.AbstractOMTest;

public class JvmxTransportTest extends AbstractOMTest {
    private IManagedContainer container;
    private IAcceptor acceptor;
    private IConnector connector;

    public void testConnect() throws Exception {
        startTransport();
    }

    public void testSendEmptyBuffer() throws Exception {
        startTransport();
        IConnector connecter = getConnector();
        IChannel channel = connecter.openChannel();
        for (int i = 0; i < 3; i++) {
            IBuffer buffer = provideBuffer(connecter);
            buffer.startPutting(channel.getID());
            channel.sendBuffer(buffer);
        }
    }

    private IManagedContainer createContainer() {
        IManagedContainer container = ContainerUtil.createContainer();
        Net4jUtil.prepareContainer(container);
        JvmxUtil.prepareContainer(container);

        return container;
    }

    @Override
    protected void doSetUp() throws Exception {
        super.doSetUp();
        container = createContainer();
        LifecycleUtil.activate(container);
    }

    @Override
    protected void doTearDown() throws Exception {
        LifecycleUtil.deactivate(container);
        container = null;
        super.doTearDown();
    }

    protected IAcceptor getAcceptor() {
        if (acceptor == null) {
            acceptor = JvmxUtil.getAcceptor(container, "default"); //$NON-NLS-1$
        }

        return acceptor;
    }

    protected IConnector getConnector() {
        if (connector == null) {
            connector = JvmxUtil.getConnector(container, "default"); //$NON-NLS-1$
        }

        return connector;
    }

    protected IBuffer provideBuffer(IConnector iConnector) {
        IBufferProvider bufferProvider = Net4jUtil.getBufferProvider(container);
        IBuffer buffer = bufferProvider.provideBuffer();

        return buffer;
    }

    protected void startTransport() throws Exception {
        if (container != null) {
            IAcceptor acceptor = getAcceptor();
            LifecycleUtil.activate(acceptor);

            IConnector connector = getConnector();
            LifecycleUtil.activate(connector);
        }
    }
}
