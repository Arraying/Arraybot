package de.arraying.arraybot.util

import de.arraying.arraybot.language.Message
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel

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
class CustomPermission(val value: String) {

    /**
     * Checks if the provided permission is valid.
     */
    fun isValid(guild: Guild): Boolean {
        return isPermission()
                || isRole(guild)
    }

    /**
     * Whether or not the member has permission.
     */
    fun hasPermission(member: Member): Boolean {
        if(!isValid(member.guild)) {
            return false
        }
        return try {
            val permission = Permission.valueOf(value.toUpperCase())
            member.hasPermission(permission)
        } catch(exception: Exception) {
            member.roles.any {
                it.id == value
            }
        }
    }

    /**
     * Gets the permission as a string.
     */
    fun toString(channel: TextChannel): String {
        return when {
                    isPermission() -> Message.PERMISSION_PERMISSION.getContent(channel, value)
                    isRole(channel.guild) -> Message.PERMISSION_ROLE.getContent(channel, value)
                    else -> Message.PERMISSION_INVALID.getContent(channel, value)
                }
    }

    /**
     * Whether or not the permission is of the Permission enumeration.
     */
    private fun isPermission(): Boolean {
        return try {
            Permission.valueOf(value.toUpperCase())
            true
        } catch(exception: Exception) {
            false
        }
    }

    /**
     * Whether or not the permission is a role ID.
     */
    private fun isRole(guild: Guild): Boolean {
        return UDatatypes.isLong(value)
                && guild.getRoleById(value.toLong()) != null
    }

}