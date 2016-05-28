/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.server.impl;

import org.code.trek.net4j.r2.servlet.R2Servlet;
import org.eclipse.net4j.util.factory.Factory;

/**
 * {@link DdsServlet} factory.
 * 
 * @author jgraham
 */
public class DdsServletFactory extends Factory {
    public DdsServletFactory() {
        super(R2Servlet.PRODUCT_GROUP, R2Servlet.TYPE);
    }

    @Override
    public R2Servlet create(String description) {
        return new DdsServlet(description);
    }

    @Override
    public String getDescriptionFor(Object object) {
        return null;
    }
}
