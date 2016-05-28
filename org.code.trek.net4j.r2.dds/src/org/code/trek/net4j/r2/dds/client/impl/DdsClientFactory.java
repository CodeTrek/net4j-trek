/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.client.impl;

import org.code.trek.net4j.r2.client.R2Client;
import org.eclipse.net4j.util.factory.Factory;

/**
 * {@link DdsClient} factory.
 * 
 * @author jgraham
 */
public class DdsClientFactory extends Factory {
    public DdsClientFactory() {
        super(R2Client.PRODUCT_GROUP, R2Client.TYPE);
    }

    @Override
    public R2Client create(String description) {
        return new DdsClient(description);
    }

    @Override
    public String getDescriptionFor(Object object) {
        return null;
    }
}
