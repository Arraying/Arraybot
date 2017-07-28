package de.arraying.arraybot.scheduler

import java.util.*
import kotlin.concurrent.timer

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
abstract class SchedulerTask(val delay: Long, val period: Long, val once: Boolean) {

    private lateinit var task: Timer

    /**
     * Schedules the task.
     */
    fun schedule() {
        task = timer(initialDelay = delay, period = period) {
            onTask()
            if(once) {
                this.cancel()
            }
        }
    }

    /**
     * Stops the task.
     */
    fun stop() {
        task.cancel()
    }

    /**
     * What happens when the task is invoked.
     */
    abstract fun onTask()

}