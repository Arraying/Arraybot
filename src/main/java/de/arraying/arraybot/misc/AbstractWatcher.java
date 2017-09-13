package de.arraying.arraybot.misc;

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
public abstract class AbstractWatcher implements Runnable {

    private final String name;
    private final int waitDuration;
    protected final Logger logger;
    private Thread current;

    /**
     * Creates a new abstract watcher.
     * @param name The name of the watcher. Spaces should be replaced by hyphens.
     * @param waitDuration The duration, in milliseconds, between each task.
     */
    public AbstractWatcher(String name, int waitDuration) {
        this.name = name;
        this.waitDuration = waitDuration;
        this.logger = LoggerFactory.getLogger(name);
    }

    /**
     * Creates the watcher.
     */
    public void create() {
        Thread current = new Thread(this);
        current.setName(name);
        current.start();
        this.current = current;
        logger.info("Starting the watching process. I got my eyes on you.");
    }

    /**
     * When the task is run.
     */
    @Override
    public void run() {
        while(!current.isInterrupted()) {
            try {
                Thread.sleep(waitDuration);
                onTask();
            } catch(InterruptedException exception) {
                logger.error("Fatally hit an interrupted execption.", exception);
            }
        }
    }

    /**
     * The task itself.
     */
    public abstract void onTask();

}
