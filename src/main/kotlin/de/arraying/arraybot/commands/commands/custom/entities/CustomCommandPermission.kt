package de.arraying.arraybot.commands.commands.custom.entities

import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.UtilsPermission
import net.dv8tion.jda.core.Permission
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
class CustomCommandPermission(val value: String) {

    /**
     * Gets the permission as a displayable string.
     */
    fun getAsString(channel: TextChannel): String {
        try {
            return Permission.valueOf(value.toUpperCase()).toString()
        } catch(exception: Exception) {
            try {
                val role = channel.guild.getRoleById(value)
                return Messages.MISC_ROLE.content(channel) + " (" + role.name + ")"
            } catch(exception: Exception) {
                return Messages.MISC_ERROR.content(channel)
            }
        }
    }

    /**
     * Checks if the user has permission to execute the custom command.
     */
    fun hasPermission(member: Member) = UtilsPermission.hasPermission(member, value)

}