package de.arraying.arraybot.commands.commands.custom.entities.action

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.commands.commands.custom.CustomCommands
import de.arraying.arraybot.commands.commands.custom.entities.CustomCommandTypes
import de.arraying.arraybot.language.Messages
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.exceptions.PermissionException

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
object CustomCommandRoleAction {

    /**
     * Executes a custom command role action.
     */
    fun execute(environment: CommandEnvironment, value: String, type: CustomCommandTypes) {
        val channel = environment.channel
        val message = environment.message
        val guild = channel.guild
        var target = environment.member
        val role: Role?
        if(CustomCommandActionPair.hasPair(true, value)) {
            val pair = CustomCommandActionPair.getPair(value)
            if(guild.getMemberById(pair.second) == null) {
                Messages.CUSTOMCOMMAND_USER_ID.send(channel).queue()
                return
            }
            role = guild.getRoleById(pair.first)
            target = guild.getMemberById(pair.second)
        } else {
            role = try {
                guild.getRoleById(value)
            } catch(exception: NumberFormatException) {
                Messages.CUSTOMCOMMAND_ROLE_ID.send(channel).queue()
                return
            }
        }
        if(role == null) {
            Messages.CUSTOMCOMMAND_ROLE_ID.send(channel).queue()
            return
        }
        try {
            val controller = guild.controller
            when(type) {
                CustomCommandTypes.ADDROLE -> {
                    controller.addRolesToMember(target, role).queue()
                    sendMessage(Messages.CUSTOMCOMMAND_ADDROLE, message)
                }
                CustomCommandTypes.REMOVEROLE -> {
                    controller.removeRolesFromMember(target, role).queue()
                    sendMessage(Messages.CUSTOMCOMMAND_REMOVEROLE, message)
                }
                CustomCommandTypes.TOGGLEROLE -> {
                    if(!target.roles.any {
                        it.idLong == role.idLong
                    }) {
                        controller.addRolesToMember(target, role).queue()
                        sendMessage(Messages.CUSTOMCOMMAND_ADDROLE, message)
                    } else {
                        controller.removeRolesFromMember(target, role).queue()
                        sendMessage(Messages.CUSTOMCOMMAND_REMOVEROLE, message)
                    }
                }
                else -> return
            }
        } catch(exception: PermissionException) {
            Messages.CUSTOMCOMMAND_ROLE_PERMISSION.send(channel).queue()
        }
    }

    /**
     * Sends the message.
     */
    private fun sendMessage(message: Messages, messageObject: Message) {
        val silent = CustomCommands.storage.retrieve(messageObject.idLong)!!.silent
        if(!silent) {
            message.send(messageObject.textChannel).queue()
        }
    }

}