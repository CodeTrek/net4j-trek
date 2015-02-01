/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.tests;

import junit.framework.TestCase;

import org.eclipse.net4j.util.om.OMBundle;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.AbstractLogHandler;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;
import org.eclipse.net4j.util.om.trace.OMTraceHandler;
import org.eclipse.net4j.util.om.trace.OMTraceHandlerEvent;

/**
 * A collection of unit tests used as point of entry "hooks" into the net4j OMBundle framework.
 */
public class OmBundleTest extends TestCase {

    private static class TraceHandler implements OMTraceHandler {
        @Override
        public void traced(OMTraceHandlerEvent event) {
        }
    }

    private static class LogHandler extends AbstractLogHandler {
        private String lastMessage;
        private Level lastLevel;
        private Throwable lastThrowable;

        @Override
        protected void writeLog(OMLogger logger, Level level, String msg, Throwable t) throws Throwable {
            lastLevel = level;
            lastMessage = msg;
            lastThrowable = t;
        }

        String getLastMessage() {
            return lastMessage;
        }

        Level getLastLevel() {
            return lastLevel;
        }

        Throwable getLastThrowable() {
            return lastThrowable;
        }
    }

    public void testOmBundleCreation() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.test.bundle", OmBundleTest.class);
        assertNotNull(bundle);
    }

    public void testOmBundleLogger() {
        OMBundle bundle = OMPlatform.INSTANCE.bundle("org.code.trek.test.bundle", OmBundleTest.class);

        LogHandler logHandler = new LogHandler();
        OMPlatform.INSTANCE.addLogHandler(logHandler);

        OMLogger logger = bundle.logger();

        logger.debug("debug message");

        assertTrue(logHandler.getLastMessage().equals("debug message"));
        assertTrue(logHandler.getLastLevel() == Level.DEBUG);
        assertNull(logHandler.getLastThrowable());
    }
}
