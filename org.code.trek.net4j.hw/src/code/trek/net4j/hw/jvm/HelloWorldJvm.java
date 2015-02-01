/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package code.trek.net4j.hw.jvm;

import java.nio.ByteBuffer;

import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.acceptor.IAcceptor;
import org.eclipse.net4j.buffer.IBuffer;
import org.eclipse.net4j.buffer.IBufferHandler;
import org.eclipse.net4j.buffer.IBufferProvider;
import org.eclipse.net4j.channel.IChannel;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.internal.jvm.JVMServerConnector;
import org.eclipse.net4j.jvm.JVMUtil;
import org.eclipse.net4j.util.container.ContainerEventAdapter;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IContainer;
import org.eclipse.net4j.util.container.IContainerDelta.Kind;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.container.SingleDeltaContainerEvent;
import org.eclipse.net4j.util.event.IEvent;
import org.eclipse.net4j.util.event.IListener;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

/**
 * A simple net4j "hello, world" program. It sends and receive buffers of data filled with double values within the same
 * process using the net4j JVM transport.
 * 
 * @author jgraham
 */
public class HelloWorldJvm {

    public static void main(String[] args) {

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Initialize the net4j OMPlatform

        // The "OM" in OMPlatform is short for "Operations & Maintenance". The OMPlatform abstracts net4j's runtime
        // container.

        // The runtime container can be a plain-old-java runtime (i.e., a LegacyPlatform) or an OSGi runtime (i.e.,
        // OSGiPlatform).

        // Configure the OMPlatform to send all traces and logs to the console
        OMPlatform.INSTANCE.setDebugging(false);
        OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);
        OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a net4j bundle

        // A net4j bundle is the unit of deployment into the net4j OMPlatform runtime - bundles execute within the
        // OMPlatform.

        // Like the OMPlatform, a net4j bundle is an abstraction that accommodates both running a bundle within a legacy
        // platform (plain-old-java execution) or as an OSGi bundle within an OSGi platform/runtime.

        // Create a legacy bundle and get its logger
        final String BUNDLE_ID = "org.code.trek.net4j.hw";
        final OMBundle BUNDLE = OMPlatform.INSTANCE.bundle(BUNDLE_ID, HelloWorldJvm.class);
        final OMLogger LOGGER = BUNDLE.logger();

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a net4j managed container

        IManagedContainer container = ContainerUtil.createContainer();

        // Initialize the container for use with the JVM protocol and activate it.
        Net4jUtil.prepareContainer(container);
        JVMUtil.prepareContainer(container);
        container.activate();

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a buffer handler used to receive buffer data from a client

        final IBufferHandler receiveHandler = new IBufferHandler() {

            @Override
            public void handleBuffer(IBuffer buffer) {
                ByteBuffer byteBuffer = buffer.getByteBuffer();
                while (byteBuffer.hasRemaining()) {
                    LOGGER.info("Received buffer with value: " + byteBuffer.getDouble());
                }
                buffer.release();
            }
        };

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Start the "server" side of the JVM connection

        try {
            // Start acceptor that represents the server side of a physical connection
            IAcceptor acceptor = Net4jUtil.getAcceptor(container, "jvm", "default");

            // Listen for connector events
            acceptor.addListener(new IListener() {

                @Override
                public void notifyEvent(IEvent event) {
                    LOGGER.info(event.toString());

                    // Container event?
                    if (event instanceof SingleDeltaContainerEvent<?>) {
                        SingleDeltaContainerEvent<?> containerEvent = (SingleDeltaContainerEvent<?>) event;
                        Object delta = containerEvent.getDeltaElement();

                        // Sever connector event?
                        if (delta instanceof JVMServerConnector) {
                            JVMServerConnector serverConnector = (JVMServerConnector) delta;

                            // New connector?
                            if (containerEvent.getDeltaKind() == Kind.ADDED) {
                                // Listen for new channels that are opened when a client connects
                                serverConnector.addListener(new ContainerEventAdapter<IChannel>() {

                                    @Override
                                    protected void onAdded(IContainer<IChannel> container, IChannel channel) {
                                        LOGGER.info("onAdded: container: " + container + " channel: " + channel);
                                        // Set the receive handler on the new channel
                                        channel.setReceiveHandler(receiveHandler);
                                    }
                                });
                            }
                        }
                    }
                }
            });

            // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // /////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // Start a connector that represents the client side of a connection

            IConnector connector = Net4jUtil.getConnector(container, "jvm", "default");

            // Open a channel to the server.
            IChannel channel = connector.openChannel();
            short channelID = channel.getID();

            // Create a buffer provider and create a byte buffer for the channel.
            IBufferProvider bufferProvider = Net4jUtil.createBufferFactory();

            IBuffer buffer = bufferProvider.provideBuffer();
            ByteBuffer byteBuffer = buffer.startPutting(channelID);

            // Fill in the byte buffer with some doubles
            for (int i = 0; i < 37; ++i) {
                byteBuffer.putDouble((i + 1) + (i + 1) / 20.0);
            }

            // Send the buffer (via the channel) without blocking and then clean up
            channel.sendBuffer(buffer);
            channel.close();

            // /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        } finally {
            LifecycleUtil.deactivate(container);
        }
    }
}
