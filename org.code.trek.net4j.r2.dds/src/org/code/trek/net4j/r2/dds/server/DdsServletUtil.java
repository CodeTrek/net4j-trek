
package org.code.trek.net4j.r2.dds.server;

import org.code.trek.net4j.r2.dds.server.impl.DdsServletFactory;
import org.eclipse.net4j.util.container.IManagedContainer;

/**
 * A utility class with static convenience methods.
 */
public final class DdsServletUtil {
    public static void prepareContainer(IManagedContainer container) {
        container.registerFactory(new DdsServletFactory());
    }

    private DdsServletUtil() {
    }
}
