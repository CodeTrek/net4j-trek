/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.eb.server.impl;

import org.code.trek.net4j.r2.servlet.R2Servlet;
import org.eclipse.net4j.util.factory.Factory;

/**
 * {@link EbServlet} factory.
 * 
 * @author jgraham
 */
public class EbServletFactory extends Factory {
    public EbServletFactory() {
        super(R2Servlet.PRODUCT_GROUP, R2Servlet.TYPE);
    }

    @Override
    public R2Servlet create(String description) {
        return new EbServlet(description);
    }

    @Override
    public String getDescriptionFor(Object object) {
        return null;
    }
}
