package org.code.trek.net4j.jvmx;

import org.eclipse.net4j.channel.IChannel;

/**
 * A {@link IChannel channel} of a {@link IJVMConnector JVM connector}.
 */
public interface IJvmxChannel extends IChannel {
    public IJvmxChannel getPeer();
}
