/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

public class TransportServerConnector extends TransportConnector {

    @Override
    public Location getLocation() {
        return Location.SERVER;
    }
}
