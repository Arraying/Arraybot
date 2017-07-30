package de.arraying.arraybot.cache.entities

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.commands.command.custom.CustomCommand
import de.arraying.arraybot.iface.ICache
import java.util.*

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
class CGuild(val id: Long,
             prefix: String,
             val password: String,
             language: String?):
        ICache {

    private var arraybot = Arraybot.instance

    /**
     * A map of custom commands.
     */
    val customCommands = HashMap<String, CustomCommand>()

    /**
     * A map of punishments.
     */
    val punishments = HashMap<Long, CPunishment>()

    /**
     * A list of disabled command names.
     */
    val disabled = ArrayList<String>()

    var announcer: CAnnouncer? = null
    var mod: CMod? = null
    var logs: CLogs? = null
    var autorole: CAutorole? = null

    /**
     * The per guild prefix for the bot.
     */
    var prefix = prefix
        set(value) {
            field = value
            arraybot.managerSql.updateGuildTable(id, "prefix", value)
        }

    /**
     * The per guild language for the bot.
     */
    var language = language
        set(value) {
            field = value
            arraybot.managerSql.updateGuildTable(id, "language", value)
        }

}