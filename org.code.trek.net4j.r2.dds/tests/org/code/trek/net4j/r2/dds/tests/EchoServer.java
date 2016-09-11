/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.tests;

import org.code.trek.net4j.r2.dds.server.impl.DdsServlet;

public class EchoServer {
    private static volatile boolean stopped = false;
    private static Thread serverThread;

    private static synchronized boolean isStopped() {
        return stopped;
    }

    private static synchronized void stopThread() {
        stopped = true;
    }

    public static void startServer(final String description) {
        stopped = false;

        serverThread = new Thread(new Runnable() {

            @Override
            public void run() {
                DdsServlet servlet = new DdsServlet(description);
                servlet.activate();
                servlet.setRequestHandler(new EchoHandler());

                while (!isStopped()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }

                servlet.deactivate();
            }
        });

        serverThread.start();
        System.out.println("[echo server] started");

    }

    public static void stopServer() throws InterruptedException {
        stopThread();
        serverThread.join();
        System.out.println("[echo server] stopped");
    }
}
