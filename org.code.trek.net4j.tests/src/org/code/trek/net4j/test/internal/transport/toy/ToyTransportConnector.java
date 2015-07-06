/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.code.trek.net4j.test.transport.ITransportConnector;
import org.eclipse.net4j.channel.ChannelException;
import org.eclipse.net4j.protocol.IProtocol;
import org.eclipse.net4j.util.security.INegotiationContext;
import org.eclipse.spi.net4j.Connector;
import org.eclipse.spi.net4j.InternalChannel;

public abstract class TransportConnector extends Connector implements ITransportConnector {

    private TransportConnector peer;
    private String name;

    @Override
    protected INegotiationContext createNegotiationContext() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        leaveConnecting();
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();
        checkState(name, "name");
    }

    public String getName() {
        return name;
    }

    public TransportConnector getPeer() {
        return peer;
    }

    @Override
    public void multiplexChannel(InternalChannel channel) {
    }

    @Override
    protected void registerChannelWithPeer(short channelID, long timeout, IProtocol<?> protocol)
            throws ChannelException {
        
        System.out.println("register channel with peer: " + channelID + " timout: " + timeout + " protocol: " + protocol);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeer(TransportConnector peer) {
        this.peer = peer;
    }
}
