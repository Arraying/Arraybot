package de.arraying.arraybot.startup;

import de.arraying.arraybot.threadding.AbstractTask;

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
public abstract class StartupTask extends AbstractTask {

    private boolean completed = false;

    /**
     * Creates a new startup task.
     * @param name The name of the task.
     */
    public StartupTask(String name) {
        super(name);
    }

    /**
     * When the startup task is started.
     */
    @Override
    public void onExecution() {
        try {
            onTask();
            completed = true;
        } catch(Exception exception) {
            logger.error("An error occurred during the startup task.", exception);
            System.exit(0);
        }
    }

    public abstract void onTask() throws Exception;

    /**
     * Gets whether the current task is completed.
     * @return True if it is, false otherwise.
     */
    boolean isCompleted() {
        return completed;
    }

}
