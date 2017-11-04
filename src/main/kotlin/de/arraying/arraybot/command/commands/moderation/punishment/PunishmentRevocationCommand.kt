package de.arraying.arraybot.command.commands.moderation.punishment

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.punishment.PunishmentType
import de.arraying.arraybot.util.CustomPermission
import de.arraying.arraybot.util.ULambda
import de.arraying.arraybot.util.UPunishment
import de.arraying.arraybot.util.UUser
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
class PunishmentRevocationCommand(commandName: String,
                                  private val type: PunishmentType): DefaultCommand(commandName,
        CommandCategory.MODERATION,
        Permission.MESSAGE_WRITE,
        customPermissionChecking = true)  {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        if(type == PunishmentType.MUTE) {
            val permission = CustomPermission(UPunishment.getMutedPermission(guild))
            if(!permission.hasPermission(environment.member)) {
                Message.PUNISH_MUTE_INVALID_PERMISSION.send(channel).queue()
                return
            }
        } else if(type == PunishmentType.BAN) {
            if(!PermissionUtil.checkPermission(channel, environment.member, Permission.BAN_MEMBERS)) {
                Message.COMMAND_PERMISSION.send(channel).queue()
                return
            }
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
        val punishment = ULambda.getSpecificGeneralizedPunishment(guild, user, type)
        if(punishment == null) {
            Message.PUNISH_COMMAND_REVOKE_FIND.send(channel).queue()
            return
        }
        if(arraybot.punishmentManager.revoke(guild, punishment, environment.author.idLong)) {
            Message.PUNISH_COMMAND_REVOKE_SUCCESS.send(channel).queue()
        } else {
            Message.PUNISH_COMMAND_REVOKE_FAIL.send(channel).queue()
        }
    }

}