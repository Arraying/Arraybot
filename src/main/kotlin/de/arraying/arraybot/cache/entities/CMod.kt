package de.arraying.arraybot.cache.entities

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.entities.iface.Cachable
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
class CMod(val id: Long,
           punishmentCount: Long,
           bypassCount: Long,
           filterEnabled: Boolean,
           filterRegex: Boolean,
           filterSilent: Boolean,
           filterPrivate: Boolean,
           filterMessage: String?,
           muteRole: Long,
           mutePermission: String?):
        Cachable {

    private val arraybot = Arraybot.instance
    val filtered = ArrayList<String>()
    val filterBypasses = HashMap<Long, CBypass>()

    /**
     * The ID of the last punishment, used to create unique punishment IDs for management.
     */
    var punishmentCount = punishmentCount
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "punishment_count", value)
        }

    /**
     * The ID of the last filter bypass, used to create unique bypass IDs for management.
     */
    var bypassCount = bypassCount
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "bypass_count", value)
        }

    /**
     * Whether or not the chat filter should be enabled.
     */
    var filterEnabled = filterEnabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "filter_enabled", value)
        }

    /**
     * Whether or not the filtered messages act as a regular expression.
     */
    var filterRegex = filterRegex
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "filter_regex", value)
        }

    /**
     * Whether or not the filtered message should get filtered silently (no message).
     */
    var filterSilent = filterSilent
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "filter_silent", value)
        }

    /**
     * Whether or not the filtered message should be sent via private message.
     */
    var filterPrivate = filterPrivate
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "filter_private", value)
        }

    /**
     * The message that should be sent if a phrase is filtered.
     */
    var filterMessage = filterMessage
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "filter_message", value)
        }

    /**
     * The role that is used for mutes.
     */
    var muteRole = muteRole
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "mute_role", value)
        }

    /**
     * The permission required to mute.
     */
    var mutePermission = mutePermission
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.MOD, "mute_permission", value)
        }

}