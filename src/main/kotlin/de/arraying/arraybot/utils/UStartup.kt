package de.arraying.arraybot.utils

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.commands.developer.eval.Evaluator
import de.arraying.arraybot.language.Language
import de.arraying.arraybot.managers.ManagerPunish
import de.arraying.arraybot.managers.ManagerRegistry
import de.arraying.arraybot.managers.ManagerSQL
import de.arraying.arraybot.managers.ManagerScheduler
import de.arraying.arraybot.misc.ArraybotException

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
object UStartup {

    private val arraybot = Arraybot.instance

    /**
     * Loads up the languages.
     */
    fun startLanguages() {
        checkStartable()
        arraybot.logger.info("Loading in all language...")
        try {
            arraybot.language = Language()
        } catch (exception: Exception) {
            throw ArraybotException("An error occurred loading up the languages: ${exception.message}")
        }
        arraybot.logger.info("All language have been registered.")
    }

    /**
     * Loads up the command related things.
     */
    fun startCommands() {
        checkStartable()
        arraybot.logger.info("Registering everything...")
        val registry = ManagerRegistry()
        arraybot.managerRegistry = registry
        registry.registerCommands()
        registry.registerCustomCommandTypes()
        registry.registerCustomCommandParameters()
        arraybot.logger.info("Finished registering everything.")
    }

    /**
     * Loads up MySQL and data management related things.
     */
    fun startData() {
        checkStartable()
        arraybot.logger.info("Setting up MySQL...")
        try {
            arraybot.managerSql = ManagerSQL()
        } catch(exception: Exception) {
            throw ArraybotException("An error occurred loading up data: ${exception.message}")
        }
        arraybot.logger.info("Everything related to MySQL has loaded.")
    }

    /**
     * Loads up misc. tasks.
     */
    fun startMisc() {
        checkStartable()
        arraybot.logger.info("Setting misc. tasks...")
        arraybot.managerScheduler = ManagerScheduler()
        arraybot.managerScheduler.scheduleGuildPurges()
        arraybot.managerPunish = ManagerPunish()
        Evaluator.init()
        arraybot.logger.info("Finished loading misc. tasks.")
    }

    /**
     * Validates given IDs. Returns true if there ARE invalid IDs.
     */
    fun areIdsInvalid(): Boolean  = (Cache.author == null || Cache.guild == null || Cache.premiumRole == null)

    /**
     * Checks if the bot can be started up.
     */
    private fun checkStartable() {
        if(arraybot.initialised) {
           throw ArraybotException("The bot has already been started up.")
        }
    }

}