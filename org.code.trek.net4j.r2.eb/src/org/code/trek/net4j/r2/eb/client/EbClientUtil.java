package org.code.trek.net4j.r2.eb.client;

import org.code.trek.net4j.r2.eb.client.impl.EbClientFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * A utility class with static convenience methods.
 */
public final class EbClientUtil {
    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new EbClientFactory());
    }

    private EbClientUtil() {
    }
}
