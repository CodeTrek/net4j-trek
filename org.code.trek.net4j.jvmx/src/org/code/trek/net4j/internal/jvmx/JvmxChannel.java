package org.code.trek.net4j.internal.jvmx;

import org.code.trek.net4j.jvmx.IJvmxChannel;
import org.eclipse.spi.net4j.Channel;

public class JvmxChannel extends Channel implements IJvmxChannel {
    private JvmxChannel peer;

    public JvmxChannel() {
    }

    public final IJvmxChannel getPeer() {
        return peer;
    }

    public final void setPeer(JvmxChannel peer) {
        this.peer = peer;
    }
}
