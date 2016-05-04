
package org.code.trek.net4j.r2.eb.tests;

import java.io.IOException;
import java.io.InputStream;

import org.code.trek.net4j.r2.eb.PubSubEventBus;
import org.code.trek.net4j.r2.eb.client.EbMethod;
import org.code.trek.net4j.r2.eb.server.impl.EbServlet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EbMethodTest {
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
    public void testDdsMethodDrl() throws IOException {
        PubSubEventBus.getInstance().reset();
        EbServlet ebServlet = new EbServlet("5:/a/b/c");
        ebServlet.setRequestHandler(new EchoServlet());

        byte[] payLoad = new byte[] { 5, 4, 3, 2, 1 };

        EbMethod method = new EbMethod("5:/a/b/c");
        method.setPayload(payLoad);
        method.execute();
        InputStream response = method.getResponse(500);

        Assert.assertEquals(payLoad.length, response.available());

        for (int i = 0; i < payLoad.length; ++i) {
            Assert.assertEquals(payLoad[i], response.read());
        }
    }

    @Test
    public void testEbMethod() throws IOException {
        PubSubEventBus.getInstance().reset();
        EbServlet ebServlet = new EbServlet("5:/");
        ebServlet.setRequestHandler(new EchoServlet());

        // PubSubEventBus.getInstance().createDomain(5).createPartition("/").register(new EchoServlet());

        byte[] payLoad = new byte[] { 5, 4, 3, 2, 1 };

        EbMethod method = new EbMethod(5);
        method.setPayload(payLoad);
        method.execute();
        InputStream response = method.getResponse(500);

        Assert.assertEquals(payLoad.length, response.available());

        for (int i = 0; i < payLoad.length; ++i) {
            Assert.assertEquals(payLoad[i], response.read());
        }
    }
}
