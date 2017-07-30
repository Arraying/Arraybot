package de.arraying.arraybot.commands.command.custom.types

import de.arraying.arraybot.commands.command.custom.entities.CustomCommandTypes
import de.arraying.arraybot.commands.command.custom.entities.action.CustomCommandActionPair
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.ULimit
import net.dv8tion.jda.core.entities.TextChannel
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
class CustomCommandTypeNickname:
        CustomCommandType(CustomCommandTypes.NICKNAME) {

    /**
     * Gets the custom command message.
     */
    override fun getMessage(channel: TextChannel): String {
        return Messages.CUSTOMCOMMAND_NICKNAME.content(channel)
    }

    /**
     * Invokes the custom command type.
     */
    override fun invoke(environment: CommandEnvironment, value: String) {
        val channel = environment.channel
        val guild = channel.guild
        if(value.length > ULimit.NICKNAME.maxLength) {
            Messages.CUSTOMCOMMAND_NICKNAME_LENGTH.send(channel).queue()
            return
        }
        var target = environment.member
        var nickname = value
        if(CustomCommandActionPair.hasPair(false, value)) {
            val pair = CustomCommandActionPair.getPair(value)
            if(guild.getMemberById(pair.second) == null) {
                Messages.CUSTOMCOMMAND_USER_ID.send(channel).queue()
                return
            }
            target = guild.getMemberById(pair.second)
            nickname = pair.first
        }
        try {
            guild.controller.setNickname(target, nickname).queue()
        } catch(exception: PermissionException) {
            Messages.CUSTOMCOMMAND_NICKNAME_PERMISSION.send(channel).queue()
        }
    }

}