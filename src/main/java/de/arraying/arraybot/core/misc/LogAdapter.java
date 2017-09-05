package de.arraying.arraybot.core.misc;

import net.dv8tion.jda.core.utils.SimpleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Copyright 2017 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class LogAdapter implements SimpleLog.LogListener {

    private final Map<SimpleLog, Logger> logs = new WeakHashMap<>();

    /**
     * Initializes the log adapter.
     */
    public static void init() {
        SimpleLog.addListener(new LogAdapter());
        SimpleLog.LEVEL = SimpleLog.Level.OFF;
    }

    /**
     * Converts any SimpleLog log to SLF4J friendly logs.
     * @param log The logger.
     * @param logLevel The level of the log.
     * @param message The log message.
     */
    @Override
    public void onLog(SimpleLog log, SimpleLog.Level logLevel, Object message) {
        Logger logger = getLogger(log);
        switch(logLevel) {
            case TRACE:
                if(logger.isTraceEnabled()) {
                    logger.trace(message.toString());
                }
                break;
            case DEBUG:
                if(logger.isDebugEnabled()) {
                    logger.debug(message.toString());
                }
                break;
            case INFO:
                logger.info(message.toString());
                break;
            case WARNING:
                logger.warn(message.toString());
                break;
            case FATAL:
                logger.error(message.toString());
                break;
        }
    }

    /**
     * Converts a SimpleLog error log to SLF4J friendly logs.
     * @param log The logger.
     * @param error The error.
     */
    @Override
    public void onError(SimpleLog log, Throwable error) {
        getLogger(log).error("An error occurred: ", error);
    }

    /**
     * Gets the logger from a SimpleLog.
     * @param log The SimpleLog.
     * @return A SLF4J Logger.
     */
    private Logger getLogger(SimpleLog log) {
        return logs.computeIfAbsent(log, ignored -> LoggerFactory.getLogger(log.name));
    }

}
