package de.arraying.arraybot.util

import de.arraying.arraybot.language.Message
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.utils.PermissionUtil

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

    companion object {

        /**
         * Gets the permission from a string.
         */
        fun getPermissionFromString(guild: Guild, input: String): CustomPermission? {
            val permission: String?
            val role = URole.getRole(guild, input)
            permission = if(role != null) {
                role.id
            } else {
                try {
                    Permission.valueOf(input.toUpperCase()).toString()
                } catch(exception: Exception) {
                    return null
                }
            }
            return CustomPermission(permission!!)
        }

    }

    /**
     * Checks if the provided permission is valid.
     */
    private fun isValid(guild: Guild): Boolean {
        return asPermission() != null
                || isRole(guild)
    }

    /**
     * Whether or not the member has permission.
     */
    fun hasPermission(member: Member, channel: TextChannel): Boolean {
        if(!isValid(member.guild)) {
            return false
        }
        val permission = asPermission()
        return if(permission != null) {
            PermissionUtil.checkPermission(channel, member, permission)
        } else {
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
                    asPermission() != null -> Message.PERMISSION_PERMISSION.getContent(channel, asPermission()!!.getName())
                    isRole(channel.guild) -> Message.PERMISSION_ROLE.getContent(channel, "<@&$value>")
                    else -> Message.PERMISSION_INVALID.getContent(channel, value)
                }
    }

    /**
     * Gets the permission, or null if it is invalid.
     */
    private fun asPermission(): Permission? {
        return try {
            Permission.valueOf(value.toUpperCase())
        } catch(exception: Exception) {
            null
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