/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.internal.transport;

import org.code.trek.net4j.test.transport.ITransportAcceptor;
import org.eclipse.spi.net4j.Acceptor;

public class TransportAcceptor extends Acceptor implements ITransportAcceptor {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
