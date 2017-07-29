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
class CAutorole(val id: Long,
                enabled: Boolean,
                message: Boolean,
                role: Long):
        ICache {

    private val arraybot = Arraybot.instance

    /**
     * Whether or not auto action should be enabled.
     */
    var enabled = enabled
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.AUTOROLE, "enabled", value)
        }

    /**
     * Whether the action should be applied on first message
     * or when the user joins the guild.
     */
    var message = message
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.AUTOROLE, "message", value)
        }

    /**
     * The action for autorole (ID).
     */
    var role = role
        set(value) {
            field = value
            arraybot.managerSql.updateModuleTable(id, ManagerSQL.Table.AUTOROLE, "message", value)
        }

}