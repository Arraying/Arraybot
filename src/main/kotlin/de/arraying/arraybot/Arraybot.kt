package de.arraying.arraybot

import de.arraying.arraybot.cache.Configuration
import de.arraying.arraybot.language.Language
import de.arraying.arraybot.managers.*
import de.arraying.arraybot.misc.ArraybotException
import de.arraying.arraybot.utils.UtilsStartup
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import net.dv8tion.jda.core.utils.SimpleLog

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
class Arraybot {

    lateinit var logger: SimpleLog
    lateinit var configuration: Configuration
    lateinit var managerBot: ManagerBot
    lateinit var language: Language
    lateinit var managerSql: ManagerSQL
    lateinit var managerRegistry: ManagerRegistry
    lateinit var managerScheduler: ManagerScheduler
    lateinit var managerPunish: ManagerPunish

    /**
     * Once the bot is intialized, you cannot initialize it again.
     */
    var initialised: Boolean = false
        set(value) {
            if(!initialised) {
                field = value
            }
        }

    /**
     * The private instance container which contains the instance.
     */
    private object InstanceContainer {
        val instance = Arraybot()
    }

    /**
     * Te "static" instance getter which returns the private instance.
     */
    companion object {
        val instance: Arraybot by lazy {
            InstanceContainer.instance
        }
    }

    /**
     * Initializes the bot.
     */
    internal fun init() {
        if(initialised) {
            throw ArraybotException("The bot has already been initialised, thus it cannot be initialised again.")
        }
        val before = System.currentTimeMillis()
        logger = SimpleLog.getLog("Arraybot")
        logger.info("Loading up Arraybot...")
        logger.info("Setting up the bot configuration...")
        try {
            configuration = Configuration.setupConfiguration()
        } catch(exception: ArraybotException) {
            exception.printStackTrace()
            return
        }
        logger.info("The configuration has been set up, starting bot version "+configuration.botVersion)
        val procedures = listOf(UtilsStartup::startLanguages,
                UtilsStartup::startCommands,
                UtilsStartup::startData,
                UtilsStartup::startMisc)
                .map {
                    launch(CommonPool) {
                        try {
                            it.invoke()
                        } catch(exception: ArraybotException) {
                            exception.printStackTrace()
                        }
                    }
                }
        runBlocking {
            procedures.forEach {
                it.join()
            }
            managerBot = ManagerBot()
            managerBot.start()
            if(UtilsStartup.areIdsInvalid()) {
                logger.fatal("One of the provided IDs is invalid.")
                managerBot.kill()
                System.exit(0)
            }
        }
        managerScheduler.scheduleStatisticsLogging()
        managerScheduler.schedulePunishmentRevocations()
        logger.info("Finished loading; it took ${System.currentTimeMillis()-before} milliseconds.")
        initialised = true
    }

}