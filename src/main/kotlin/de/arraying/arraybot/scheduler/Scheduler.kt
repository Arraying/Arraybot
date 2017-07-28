package de.arraying.arraybot.scheduler

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
object Scheduler {

    private val tasks = HashMap<Long, SchedulerTask>()
    private var currentTask = 0L

    /**
     * Schedules a task.
     */
    fun schedule(task: SchedulerTask): Long {
        if(currentTask+1 >= Long.MAX_VALUE) {
            throw IllegalStateException("You cannot schedule more tasks (how did you even schedule ${Long.MAX_VALUE} tasks anyway!?)")
        }
        currentTask++
        task.schedule()
        tasks.put(currentTask, task)
        return currentTask
    }

    /**
     * Gets the IDs of all active tasks.
     */
    fun activeTasks(): Array<Long> {
        return tasks.keys.toLongArray().toTypedArray()
    }

    /**
     * Cancels an active task.
     */
    fun cancelTask(id: Long): Boolean {
        if(!tasks.containsKey(id)) {
            return false
        }
        val task = tasks[id]
        task?.stop()
        tasks.remove(id)
        return true
    }

}