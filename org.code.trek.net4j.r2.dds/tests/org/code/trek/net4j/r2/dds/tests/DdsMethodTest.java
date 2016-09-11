/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.tests;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.code.trek.net4j.r2.client.R2Method;
import org.code.trek.net4j.r2.dds.OM;
import org.code.trek.net4j.r2.dds.client.impl.DdsClient;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DdsMethodTest {
    private static DdsClient client;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
        OMPlatform.INSTANCE.setDebugging(true);
        OM.DEBUG.setEnabled(true);

        EchoServer.startServer("7:/");

        client = new DdsClient("7:/");
        client.activate();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        client.deactivate();
        EchoServer.stopServer();
    }

    private static byte[] fullPayload() {

        List<Byte> payloadList = new ArrayList<Byte>();

        for (int i = 0; i < 256; ++i) {
            payloadList.add((byte) i);
        }

        byte[] payload = new byte[payloadList.size()];

        for (int i = 0; i < payloadList.size(); ++i) {
            payload[i] = payloadList.get(i);
        }

        return payload;
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDdsMethod() throws IOException, InterruptedException {
        R2Method method = client.newMethod("7:/");

        byte[] payLoad = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1, 127 };
        method.setPayload(payLoad);

        method.execute();

        InputStream response = method.getResponse(2000);

        for (int i = 0; i < payLoad.length; ++i) {
            Assert.assertEquals(payLoad[i], response.read());
        }
    }

    @Test
    public void testDdsMethodHighVolume1() throws IOException, InterruptedException {
        for (int i = 0; i < 100000; ++i) {
            R2Method method = client.newMethod("7:/");
            byte[] payLoad = fullPayload();
            method.setPayload(payLoad);
            method.execute();
            InputStream response = method.getResponse(2 * 1000);
            for (int j = 0; j < payLoad.length; ++j) {
                Assert.assertEquals(payLoad[j], (byte) response.read());
            }
        }
    }

    @Test
    public void testDdsMethodHighVolume2() throws IOException, InterruptedException {
        for (int i = 0; i < 100000; ++i) {
            R2Method method = client.newMethod("7:/");
            byte[] payLoad = fullPayload();
            method.setPayload(payLoad);
            method.execute();
            InputStream response = method.getResponse(2 * 1000);
            for (int j = 0; j < payLoad.length; ++j) {
                Assert.assertEquals(payLoad[j], (byte) response.read());
            }
        }
    }

    @Test
    public void testDdsMethodRepeatedCallsFullPayload() throws IOException, InterruptedException {
        for (int i = 0; i < 1000; ++i) {
            R2Method method = client.newMethod("7:/");
            byte[] payLoad = fullPayload();
            method.setPayload(payLoad);
            method.execute();
            InputStream response = method.getResponse(2 * 1000);
            for (int j = 0; j < payLoad.length; ++j) {
                Assert.assertEquals(payLoad[j], (byte) response.read());
            }
        }
    }

    @Test
    public void testDdsMethodRepeatedCallsSmallPayload() throws IOException, InterruptedException {
        for (int i = 0; i < 1000; ++i) {
            R2Method method = client.newMethod("7:/");
            byte[] payLoad = new byte[] { 1, 2, 3, 4, 11, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
            method.setPayload(payLoad);
            method.execute();
            InputStream response = method.getResponse(2 * 1000);
            for (int j = 0; j < payLoad.length; ++j) {
                Assert.assertEquals(payLoad[j], response.read());
            }
        }
    }
}
