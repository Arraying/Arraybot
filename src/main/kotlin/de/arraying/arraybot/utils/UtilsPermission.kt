package de.arraying.arraybot.utils

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Member

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
object UtilsPermission {

    /**
     * Checks if the user has permission.
     * This permission can be a Permission object name, or a role ID.
     */
    fun hasPermission(member: Member, value: String): Boolean {
        try {
            val permission = Permission.valueOf(value.toUpperCase())
            return member.hasPermission(permission)
        } catch(exception: Exception) {
            return member.roles.any {
                it.id == value
            }
        }
    }

}