/*
 *  Copyright (c) 2016 Jay Graham
 *  All rights reserved. This program and the accompanying
 *  materials are made available under the terms of the MIT License
 *  (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.r2.dds.client;

import org.code.trek.net4j.r2.dds.client.impl.DdsClientFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * A utility class with static convenience methods.
 */
public final class DdsClientUtil {
    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new DdsClientFactory());
    }

    private DdsClientUtil() {
    }
}
