package org.code.trek.net4j.internal.jvmx;

import org.eclipse.net4j.util.lifecycle.LifecycleUtil;

public class JvmxServerConnector extends JvmxConnector {
    public JvmxServerConnector(JvmxClientConnector clientPeer) {
        setPeer(clientPeer);
    }

    @Override
    public Location getLocation() {
        return Location.SERVER;
    }

    @Override
    protected void doDeactivate() throws Exception {
        LifecycleUtil.deactivateNoisy(getPeer());
        super.doDeactivate();
    }
}
