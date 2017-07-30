package de.arraying.arraybot.core.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.core.scheduler.Scheduler
import de.arraying.arraybot.core.scheduler.SchedulerTask
import de.arraying.arraybot.utils.Utils

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
class ManagerScheduler {

    private val arraybot = Arraybot.instance

    /**
     * Schedules the removed guilds to be purged.
     */
    fun scheduleGuildPurges() {
        val task = object: SchedulerTask(0, 43200000, false) {
            override fun onTask() {
                for(guild in Utils.getRedundantlyCachedGuilds()) {
                    arraybot.managerSql.removeGuild(guild)
                }
            }
        }
        Scheduler.schedule(task)
    }

    /**
     * Schedules statistics to be logged.
     */
    fun scheduleStatisticsLogging() {
        val shutdownThread = object: Thread() {
            override fun run() {
                arraybot.logger.info("Forcefully logging statistics due to shutdown...")
                arraybot.managerSql.logStatistics()
            }
        }
        Runtime.getRuntime().addShutdownHook(shutdownThread)
        val task = object: SchedulerTask(0, 43200000, false) {
            override fun onTask() {
                arraybot.managerSql.logStatistics()
            }
        }
        Scheduler.schedule(task)
    }

    /**
     * Schedules punishment revocations.
     */
    fun schedulePunishmentRevocations() {
        for(guild in Cache.guilds.values) {
            for(punishment in guild.punishments.values) {
                if(punishment.revoked) {
                    continue
                }
                val jdaGuild = Utils.getGuild(guild.id)
                if(jdaGuild != null) {
                    arraybot.managerPunish.handleTimedPunishment(jdaGuild, punishment)
                }
            }
        }
    }

}