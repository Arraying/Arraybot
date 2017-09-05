package de.arraying.arraybot.core.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class StartupError implements Thread.UncaughtExceptionHandler {

    /**
     * When an uncaught exception occurs.
     * @param thread The thread where the exception will occur.
     * @param exception The exception.
     */
    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Logger logger = LoggerFactory.getLogger("Startup-Error");
        logger.error("A fatal startup error occurred, shutting down.", exception);
    }

}
