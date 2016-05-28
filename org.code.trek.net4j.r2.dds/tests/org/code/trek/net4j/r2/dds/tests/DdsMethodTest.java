/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.tests;

import java.io.IOException;
import java.io.InputStream;

import org.code.trek.net4j.r2.client.R2Method;
import org.code.trek.net4j.r2.dds.client.impl.DdsClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DdsMethodTest {
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDdsMethod() throws IOException {
        EchoServer.startServer("5:/");

        DdsClient client = new DdsClient("5:/");
        client.activate();

        R2Method method = client.newMethod("5:/");

        byte[] payLoad = new byte[] { 8, 7, 6, 5, 4, 3, 2, 1, 127 };
        method.setPayload(payLoad);

        method.execute();

        InputStream response = method.getResponse(20000);

        for (int i = 0; i < payLoad.length; ++i) {
            Assert.assertEquals(payLoad[i], response.read());
        }

        client.deactivate();

        EchoServer.stopServer();
    }
}
