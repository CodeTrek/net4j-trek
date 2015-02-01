package org.code.trek.net4j.jvmx.tests;

import java.util.Map.Entry;

import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.factory.IFactory;
import org.eclipse.net4j.util.factory.IFactoryKey;
import org.eclipse.net4j.util.registry.IRegistry;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    private IManagedContainer pluginContainer;

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;

        // Create a plug-in container and list the factory and element registrations discovered via plug-in extensions

        pluginContainer = ContainerUtil.createPluginContainer();
        pluginContainer.activate();

        listFactores(pluginContainer);
    }

    private void listFactores(IManagedContainer container) {
        IRegistry<IFactoryKey, IFactory> registry = container.getFactoryRegistry();

        for (Entry<IFactoryKey, IFactory> entry : registry.getElements()) {
            System.out.println(entry);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext bundleContext) throws Exception {
        pluginContainer.deactivate();
        pluginContainer = null;
        Activator.context = null;
    }

}
