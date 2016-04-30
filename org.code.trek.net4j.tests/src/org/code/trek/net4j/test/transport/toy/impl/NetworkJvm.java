package org.code.trek.net4j.test.transport.toy.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * A network of very simple client/server transport connections.
 *
 */
public class NetworkJvm {

    public static NetworkJvm instance() {
        return new NetworkJvm();
    }

    private List<ClientConnection> clientConnections = new ArrayList<>();
    private List<ConnectionEventListener> connectionEventListeners = new ArrayList<>();

    public ServerConnectionListener createServerConnectionListener() {
        return new ServerConnectionListener();
    }

    public ClientConnection openClientConnection() {
        ClientConnection clientConnection = new ClientConnection();
        clientConnections.add(clientConnection);
        fireOpenConnectionEvent();
        return clientConnection;
    }

    private void fireOpenConnectionEvent() {
        for (ConnectionEventListener listener : connectionEventListeners) {
            listener.handleConnectionOpenEvent();
        }
    }

    public List<ClientConnection> getClientConnections() {
        return clientConnections;
    }

    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
        connectionEventListeners.add(connectionEventListener);
    }
}
