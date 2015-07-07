/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport.toy;

import org.code.trek.net4j.test.transport.toy.IToyTransportChannel;
import org.eclipse.spi.net4j.Channel;

public class ToyTransportChannel extends Channel implements IToyTransportChannel {

    private ToyTransportChannel peer;

    private Location location;
    @Override
    public Location getLocation() {
        return location;
    }

    public IToyTransportChannel getPeer() {
        return peer;
    }

    public void setPeer(ToyTransportChannel peer) {
        this.peer = peer;
    }
}
