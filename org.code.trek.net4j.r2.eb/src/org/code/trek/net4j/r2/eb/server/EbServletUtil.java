package org.code.trek.net4j.r2.eb.server;

import org.code.trek.net4j.r2.eb.server.impl.EbServletFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * A utility class with static convenience methods.
 */
public final class EbServletUtil {
    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new EbServletFactory());
    }

    private EbServletUtil() {
    }
}
