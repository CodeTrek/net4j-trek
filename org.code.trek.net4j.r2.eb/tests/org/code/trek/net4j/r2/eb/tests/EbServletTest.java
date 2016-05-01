
package org.code.trek.net4j.r2.eb.tests;

import org.code.trek.net4j.r2.eb.PubSubEventBus;
import org.code.trek.net4j.r2.eb.client.EbClientRequest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EbServletTest {
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
    public void testServlet() {
        PubSubEventBus.getInstance().reset();
        PubSubEventBus.getInstance().createDomain(5).createPartition("/");
        PubSubEventBus.getInstance().getDomain(5).getPartition("/").register(new EchoServlet());

        final byte[] payLoad = new byte[] { 5, 4, 3, 2, 1 };

        EbClientRequest clientRequest = new EbClientRequest(payLoad);

        PubSubEventBus.getInstance().getDomain(5).getPartition("/").post(clientRequest);

        final byte[] response = clientRequest.getReponse(50);

        Assert.assertEquals(payLoad.length, response.length);

        for (int i = 0; i < payLoad.length; ++i) {
            Assert.assertEquals(payLoad[i], response[i]);
        }
    }
}
