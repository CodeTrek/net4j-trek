/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */

package org.code.trek.net4j.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.net4j.util.om.log.AbstractLogHandler;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;

/**
 * A simple log handler that tracks log messages in a list.
 */
class LogHandler extends AbstractLogHandler {
    private List<String> messages = new ArrayList<String>();
    private List<Level> levels = new ArrayList<Level>();
    private Throwable lastThrowable;
    private OMLogger lastLogger;

    @Override
    protected void writeLog(OMLogger logger, Level level, String msg, Throwable t) throws Throwable {
        lastLogger = logger;
        levels.add(level);
        messages.add(msg);
        lastThrowable = t;
    }

    OMLogger getLastLogger() {
        return lastLogger;
    }

    List<Level> getLevels() {
        return levels;
    }

    List<String> getMessages() {
        return messages;
    }

    Throwable getLastThrowable() {
        return lastThrowable;
    }
}
