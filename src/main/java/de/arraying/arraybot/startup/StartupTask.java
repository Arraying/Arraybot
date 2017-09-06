package de.arraying.arraybot.startup;

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
public abstract class StartupTask implements Runnable {

    private final String name;
    protected final Logger logger;

    /**
     * Creates a new startup task.
     * @param name The name of the task.
     */
    public StartupTask(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(name);
    }

    /**
     * Runs the task.
     */
    @Override
    public void run() {
        try {
            onTask();
        } catch(Exception exception) {
            logger.error("An error occurred during the startup task.", exception);
            System.exit(0);
        }
    }

    public abstract void onTask() throws Exception;

    /**
     * Gets the name of the startup task.
     * @return The name.
     */
    String getName() {
        return name;
    }

}
