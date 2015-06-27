/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

public class TransportClientConnector extends TransportConnector {

    private TransportAcceptor acceptor;

    @Override
    protected void doActivate() throws Exception {
        super.doActivate();
        TransportServerConnector peer = acceptor.handleAccept(this);
        setPeer(peer);
    }

    @Override
    protected void doBeforeActivate() throws Exception {
        super.doBeforeActivate();

        // Use the connector name to lookup a peer acceptor in the Transport acceptor registry
        String name = getName();
        acceptor = TransportAcceptorRegistry.INSTANCE.getAcceptor(name);
        
        // No peer acceptor found?
        if (acceptor == null) {
            throw new IllegalStateException("Transport acceptor not found: " + name);
        }
    }

    @Override
    protected void doDeactivate() throws Exception {
        LifecycleUtil.deactivateNoisy(getPeer());
        super.doDeactivate();
    }

    @Override
    public Location getLocation() {
        return Location.CLIENT;
    }
}
