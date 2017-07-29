package de.arraying.arraybot.cache.entities

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.iface.ICache
import de.arraying.arraybot.managers.ManagerSQL

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
class CLogs(val id: Long,
            userEnabled: Boolean,
            userChannel: Long,
            messageEnabled: Boolean,
            messageChannel: Long,
            guildEnabled: Boolean,
            guildChannel: Long,
            modEnabled: Boolean,
            modChannel: Long):
        ICache {

    private val arraybot = Arraybot.instance

    /**
     * Whether or not user related actions should be logged.
     */
    var userEnabled = userEnabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "user_enabled", value)
        }

    /**
     * The channel to send user related logs to (ID).
     */
    var userChannel = userChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "user_channel", value)
        }

    /**
     * Whether or not message related actions should be logged.
     */
    var messageEnabled = messageEnabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "message_enabled", value)
        }

    /**
     * The channel to send message related logs to (ID).
     */
    var messageChannel = messageChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "message_channel", value)
        }

    /**
     * Whether or not guild related actions should be logged.
     */
    var guildEnabled = guildEnabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "guild_enabled", value)
        }

    /**
     * The channel to send guild related logs to (ID).
     */
    var guildChannel = guildChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "guild_channel", value)
        }

    /**
     * Whether or not user related actions should be logged.
     */
    var modEnabled = modEnabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "mod_enabled", value)
        }

    /**
     * The channel to send mod related logs to (ID).
     */
    var modChannel = modChannel
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.LOGS, "mod_channel", value)
        }

}