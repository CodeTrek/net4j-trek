package org.code.trek.net4j.internal.jvmx;

import org.code.trek.net4j.internal.jvmx.bundle.OM;
import org.code.trek.net4j.internal.jvmx.messages.Messages;
import org.code.trek.net4j.jvmx.IJvmxConnector;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.channel.ChannelException;
import org.eclipse.net4j.protocol.IProtocol;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.security.INegotiationContext;
import org.eclipse.spi.net4j.Connector;
import org.eclipse.spi.net4j.InternalChannel;

public abstract class JvmxConnector extends Connector implements IJvmxConnector {
    @SuppressWarnings("unused")
    private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG, JvmxConnector.class);

    private JvmxConnector peer;

    private String name;

    public JvmxConnector() {
    }

    public String getName() {
        return name;
    }

    public JvmxConnector getPeer() {
        return peer;
    }

    @Override
    public String getURL() {
        return "jvmx://" + name; //$NON-NLS-1$
    }

    public void multiplexChannel(InternalChannel localChannel) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeer(JvmxConnector peer) {
        this.peer = peer;
    }

    @Override
    protected InternalChannel createChannel() {
        return new JvmxChannel();
    }

    @Override
    protected INegotiationContext createNegotiationContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void deregisterChannelFromPeer(InternalChannel channel) throws ChannelException {
    }

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        leaveConnecting();
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();
        checkState(name, "name"); //$NON-NLS-1$
    }

    @Override
    protected void registerChannelWithPeer(short channelID, long timeoutIgnored, IProtocol<?> protocol)
            throws ChannelException {
        try {
            String protocolID = Net4jUtil.getProtocolID(protocol);
            int protocolVersion = Net4jUtil.getProtocolVersion(protocol);

            JvmxChannel peerChannel = (JvmxChannel) peer.inverseOpenChannel(channelID, protocolID, protocolVersion);
            if (peerChannel == null) {
                throw new ChannelException(Messages.getString("JvmxConnector.2")); //$NON-NLS-1$
            }

            JvmxChannel c = (JvmxChannel) getChannel(channelID);
            c.setPeer(peerChannel);
            peerChannel.setPeer(c);
        } catch (ChannelException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ChannelException(ex);
        }
    }
}
