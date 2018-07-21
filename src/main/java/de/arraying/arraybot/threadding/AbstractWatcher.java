package de.arraying.arraybot.threadding;

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
public abstract class AbstractWatcher extends AbstractTask {

    private final int waitDuration;

    /**
     * Creates a new abstract watcher.
     * @param name The name of the watcher. Spaces should be replaced by hyphens.
     * @param waitDuration The duration, in milliseconds, between each task.
     */
    public AbstractWatcher(String name, int waitDuration) {
        super(name);
        this.waitDuration = waitDuration;
    }

    /**
     * The task itself.
     */
    public abstract void onTask();

    /**
     * When the watcher is executed for the fist time.
     */
    @Override
    public final void onExecution() {
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
     * Interrupts the task.
     */
    public void interrupt() {
        Thread.currentThread().interrupt();
    }

}
