package de.arraying.arraybot.command.commands.moderation.punishment

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.punishment.PunishmentType
import de.arraying.arraybot.util.*
import net.dv8tion.jda.core.Permission
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
open class PunishmentCommand(commandName: String,
                             private val newPermission: Permission,
                             private val type: PunishmentType): DefaultCommand(commandName,
        CommandCategory.MODERATION,
        Permission.MESSAGE_WRITE,
        customPermissionChecking = true) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val member = environment.member
        if(type == PunishmentType.MUTE
                || type == PunishmentType.TEMP_MUTE) {
            val mutedRole = UPunishment.getMutedRole(guild)
            if(guild.getRoleById(mutedRole) == null) {
                Message.PUNISH_MUTE_INVALID_ROLE.send(channel).queue()
                return
            }
            val permission = CustomPermission(UPunishment.getMutedPermission(guild))
            if(!permission.hasPermission(environment.member)) {
                Message.PUNISH_MUTE_INVALID_PERMISSION.send(channel).queue()
                return
            }
            if(UPunishment.isMute(member)) {
                Message.PUNISH_COMMAND_ALREADY_MUTED.send(channel).queue()
                return
            }

        }
        if(!PermissionUtil.checkPermission(channel, environment.member, newPermission)) {
            Message.COMMAND_PERMISSION.send(channel, permission.getName()).queue()
            return
        }
        if(args.size < 2) {
            Message.PUNISH_COMMAND_USER_PROVIDE.send(channel).queue()
            return
        }
        val userRaw = args[1]
        val userObject = UUser.getMember(guild, userRaw)
        if(userObject == null) {
            Message.PUNISH_COMMAND_USER_INVALID.send(channel).queue()
            return
        }
        val user = userObject.user.idLong
        if((type == PunishmentType.BAN
                || type == PunishmentType.TEMP_BAN)
                && UPunishment.isBan(guild, user)) {
            Message.PUNISH_COMMAND_ALREADY_BANNED.send(channel).queue()
            return
        }
        val expiration: Long
        val reasonIndex: Int
        if(type == PunishmentType.TEMP_MUTE
                || type == PunishmentType.TEMP_BAN) {
            if(args.size < 3) {
                Message.PUNISH_COMMAND_TIME_PROVIDE.send(channel).queue()
                return
            }
            val timeRaw = args[2]
            expiration = UTime.parseDuration(timeRaw)
            if(expiration == -1L) {
                Message.PUNISH_COMMAND_TIME_INVALID.send(channel).queue()
                return
            }
            reasonIndex = 3
        } else {
            expiration = -1
            reasonIndex = 2
        }
        val reason = if(args.size > reasonIndex) {
            UArguments.combine(args.toTypedArray(), reasonIndex)
        } else {
            null
        }
        if(reason != null
                && reason.length > Limits.REASON.limit) {
            Message.PUNISH_REASON_LENGTH.send(channel, Limits.REASON.limit.toString()).queue()
            return
        }
        if(arraybot.punishmentManager.punish(guild, user, type, environment.author.idLong, expiration, false, reason)) {
            Message.PUNISH_COMMAND_SUCCESS.send(channel).queue()
        } else {
            Message.PUNISH_COMMAND_FAIL.send(channel).queue()
        }
    }

}