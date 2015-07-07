/*
    Copyright (C) 2015 Jay Graham
    Distributed under the MIT License (see http://www.opensource.org/licenses/mit-license.php)
 */
package org.code.trek.net4j.test.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.net4j.util.om.log.AbstractLogHandler;
import org.eclipse.net4j.util.om.log.OMLogger;
import org.eclipse.net4j.util.om.log.OMLogger.Level;

/**
 * A simple log handler that tracks log messages in a list.
 */
public class LogHandler extends AbstractLogHandler {
    private List<String> messages = new ArrayList<String>();
    private List<Level> levels = new ArrayList<Level>();
    private Throwable lastThrowable;
    private OMLogger lastLogger;

    OMLogger getLastLogger() {
        return lastLogger;
    }

    public Throwable getLastThrowable() {
        return lastThrowable;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    protected void writeLog(OMLogger logger, Level level, String msg, Throwable t) throws Throwable {
        lastLogger = logger;
        levels.add(level);
        messages.add(msg);
        lastThrowable = t;
    }
}
