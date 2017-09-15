package de.arraying.arraybot.threadding;

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
public abstract class AbstractTask implements Runnable {

    private final String name;
    protected final Logger logger;
    Thread current;

    /**
     * Creates a new abstract task
     * @param name The name of the task.
     */
    public AbstractTask(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(name);
    }

    /**
     * Creates the task.
     */
    public void create() {
        Thread current = new Thread(this);
        current.setName(name);
        current.start();
        this.current = current;
    }

    /**
     * Runs the task.
     */
    @Override
    public void run() {
        onExecution();
    }

    /**
     * The method that is invoked when the task is executed.
     */
    public abstract void onExecution();

}
