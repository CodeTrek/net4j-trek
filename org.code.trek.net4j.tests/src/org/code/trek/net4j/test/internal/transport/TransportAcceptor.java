/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import java.text.MessageFormat;

import org.code.trek.net4j.test.transport.ITransportAcceptor;
import org.eclipse.net4j.util.StringUtil;
import org.eclipse.spi.net4j.Acceptor;

public class TransportAcceptor extends Acceptor implements ITransportAcceptor {

    private String name;

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        TransportAcceptorRegistry.INSTANCE.registerAcceptor(this);
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();
        if (StringUtil.isEmpty(name)) {
            throw new IllegalStateException("No name");
        }
    }

    @Override
    protected void doDeactivate() throws Exception {
        super.doDeactivate();
        TransportAcceptorRegistry.INSTANCE.deregisterAcceptor(this);
    }

    public String getName() {
        return name;
    }

    /**
     * Accepts a new client connection request.
     * 
     * @param client
     *            the new client
     * 
     * @return the client's server-side peer connector
     */
    TransportServerConnector handleAccept(TransportClientConnector client) {
        // Create client's server-side peer connector
        TransportServerConnector connector = new TransportServerConnector(client);

        // Synchronize the server-side peer connector's name with the client
        connector.setName(client.getName());

        // Configure the server-side peer connector
        connector.setConfig(getConfig());

        // Activate the new server connector and track it
        connector.activate();
        addConnector(connector);

        return connector;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MessageFormat.format("TransporAcceptor[{0}]", name);
    }
}
