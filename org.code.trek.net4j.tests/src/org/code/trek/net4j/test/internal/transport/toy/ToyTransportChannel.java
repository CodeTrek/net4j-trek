/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.code.trek.net4j.test.transport.ITransportChannel;
import org.eclipse.spi.net4j.Channel;

public class TransportChannel extends Channel implements ITransportChannel {

    @Override
    public Location getLocation() {
        return location;
    }

    private TransportChannel peer;
    private Location location;

    public void setPeer(TransportChannel peer) {
        this.peer = peer;
    }

    public ITransportChannel getPeer() {
        return peer;
    }
}
