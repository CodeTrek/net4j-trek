/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.tests;

import org.code.trek.net4j.r2.eb.PubSubEventBus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.eventbus.EventBus;

public class PubSubEventsBusTests {
    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @AfterClass
    public static void tearDownAfterClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateDomainPartition() {
        PubSubEventBus.getInstance().reset();
        EventBus domain = PubSubEventBus.getInstance().createDomain(1).createPartition("/");
        Assert.assertNotNull(domain);
    }

    @Test
    public void testGetDomainPartition() {
        PubSubEventBus.getInstance().reset();
        EventBus domain = PubSubEventBus.getInstance().createDomain(1).createPartition("/");
        Assert.assertNotNull(domain);

        EventBus domainPartition = PubSubEventBus.getInstance().getDomain(1).getPartition("/");
        Assert.assertNotNull(domainPartition);

        Assert.assertTrue(domain == domainPartition);
    }

    @Test
    public void testGetDomainPartitionException() {
        PubSubEventBus.getInstance().reset();
        boolean exceptionThrown = false;
        EventBus domainPartition = null;

        try {
            domainPartition = PubSubEventBus.getInstance().getDomain(1).getPartition("/");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        Assert.assertNull(domainPartition);
        Assert.assertTrue(exceptionThrown);

        exceptionThrown = false;

        domainPartition = PubSubEventBus.getInstance().createDomain(1).createPartition("/a/b/c");

        try {
            domainPartition = PubSubEventBus.getInstance().getDomain(1).getPartition("/a/b");
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        Assert.assertNotNull(domainPartition);
        Assert.assertTrue(exceptionThrown);
    }
}
