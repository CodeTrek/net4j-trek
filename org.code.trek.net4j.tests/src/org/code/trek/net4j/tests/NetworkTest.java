/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import org.code.trek.net4j.test.transport.toy.impl.ClientConnection;
import org.code.trek.net4j.test.transport.toy.impl.ConnectionEventListener;
import org.code.trek.net4j.test.transport.toy.impl.NetworkJvm;
import org.code.trek.net4j.test.transport.toy.impl.ServerConnectionListener;
import org.junit.Assert;

import junit.framework.TestCase;

/**
 * 
 */
public class NetworkTest extends TestCase {

    protected boolean connectionOpenEvent;

    public void testCreateNetwork() {
        NetworkJvm network = NetworkJvm.instance();
        Assert.assertNotNull(network);
    }
    
    public void testCreateServerConnectionListener() {
        NetworkJvm network = NetworkJvm.instance();
        ServerConnectionListener serverConnectionListener = network.createServerConnectionListener();
        Assert.assertNotNull(serverConnectionListener);
    }

    public void testOpenClientConnection() {
        NetworkJvm network = NetworkJvm.instance();
        ClientConnection clientConnection = network.openClientConnection();
        Assert.assertNotNull(clientConnection);
        
        Assert.assertEquals(1, network.getClientConnections().size());
    }

    public void testOpenClientConnections() {
        NetworkJvm network = NetworkJvm.instance();
        ClientConnection clientConnection = network.openClientConnection();
        Assert.assertNotNull(clientConnection);

        clientConnection = network.openClientConnection();
        Assert.assertNotNull(clientConnection);

        clientConnection = network.openClientConnection();
        Assert.assertNotNull(clientConnection);
        
        Assert.assertEquals(3, network.getClientConnections().size());
    }

    public void testOpenClientConnectionEvent() {
        NetworkJvm network = NetworkJvm.instance();
        
        connectionOpenEvent = false;

        network.addConnectionEventListener(new ConnectionEventListener() {

            @Override
            public void handleConnectionOpenEvent() {
                connectionOpenEvent = true; 
            }});

        network.openClientConnection();
        
        Assert.assertTrue(connectionOpenEvent);
    }
}