/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

public class TransportServerConnector extends TransportConnector {

    public TransportServerConnector(TransportClientConnector peer) {
        setPeer(peer);
    }

    @Override
    protected void doDeactivate() throws Exception {
        LifecycleUtil.deactivateNoisy(getPeer());
        super.doDeactivate();
    }

    @Override
    public Location getLocation() {
        return Location.SERVER;
    }
}
