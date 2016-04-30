/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport.toy;

import java.nio.ByteBuffer;
import java.util.Queue;

import org.code.trek.net4j.test.transport.toy.IToyTransportConnector;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.channel.ChannelException;
import org.eclipse.net4j.protocol.IProtocol;
import org.eclipse.net4j.util.security.INegotiationContext;
import org.eclipse.spi.net4j.Connector;
import org.eclipse.spi.net4j.InternalChannel;

public abstract class ToyTransportConnector extends Connector implements IToyTransportConnector {

    private ToyTransportConnector peer;

    private String name;
    @Override
    protected InternalChannel createChannel() {
        return new ToyTransportChannel();
    }

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

    public ToyTransportConnector getPeer() {
        return peer;
    }

    @Override
    public void multiplexChannel(InternalChannel localChannel) {
        System.out.println("Multiplex channel: " + localChannel);
        short channelID = localChannel.getID();
        InternalChannel peerChannel = peer.getChannel(channelID);
        if (peerChannel == null) {
            throw new IllegalStateException("peerChannel == null");
        }

        Queue<IBuffer> localQueue = localChannel.getSendQueue();
        IBuffer buffer = localQueue.poll();

        ByteBuffer byteBuffer = buffer.getByteBuffer();
        if (byteBuffer.position() == IBuffer.HEADER_SIZE) {
            // Just release this empty buffer has been written
            buffer.release();
            return;
        }

        buffer.flip();
        peerChannel.handleBufferFromMultiplexer(buffer);
    }

    @Override
    protected void registerChannelWithPeer(short channelID, long timeout, IProtocol<?> protocol)
            throws ChannelException {

        System.out
                .println("register channel with peer: " + channelID + " timout: " + timeout + " protocol: " + protocol);

        try {
            String protocolID = Net4jUtil.getProtocolID(protocol);
            int protocolVersion = Net4jUtil.getProtocolVersion(protocol);

            ToyTransportChannel peerChannel = (ToyTransportChannel) peer.inverseOpenChannel(channelID, protocolID,
                    protocolVersion);
            if (peerChannel == null) {
                throw new ChannelException("Failed to open invers channel: id: " + channelID);
            }

            ToyTransportChannel c = (ToyTransportChannel) getChannel(channelID);
            c.setPeer(peerChannel);
            peerChannel.setPeer(c);
        } catch (ChannelException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ChannelException(ex);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPeer(ToyTransportConnector peer) {
        this.peer = peer;
    }
}
