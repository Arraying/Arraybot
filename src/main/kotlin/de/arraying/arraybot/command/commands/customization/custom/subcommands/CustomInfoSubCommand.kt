package de.arraying.arraybot.command.commands.customization.custom.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.CustomCommand
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UEmbed

/**
 * Copyright 2018 Arraying
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
class CustomInfoSubCommand: SubCommand("info",
        aliases = arrayOf("i")) {

    /**
     * When the sub-command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Message.COMMANDS_CUSTOM_PROVIDE_NAME.send(channel).queue()
            return
        }
        val name = args[2].toLowerCase()
        val customCommands = Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
        val guildId = environment.guild.idLong
        if(!customCommands.contains(guildId, name)) {
            Message.COMMANDS_CUSTOM_NOT_EXISTS.send(channel).queue()
            return
        }
        val command = CustomCommand.fromRedis(guildId, name)
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_CUSTOM_INFO.getContent(channel))
                .addField(Message.COMMANDS_COMMANDS_INFO_NAME.getContent(channel),
                        name,
                        false)
                .addField(Message.COMMANDS_COMMANDS_INFO_TYPE.getContent(channel),
                        command.type.name,
                        false)
                .addField(Message.COMMANDS_COMMANDS_INFO_SYNTAX.getContent(channel),
                        command.syntax.name,
                        false)
                .addField(Message.COMMANDS_COMMANDS_INFO_PERMISSION.getContent(channel),
                        command.permission.toString(channel),
                        false)
                .addField(Message.COMMANDS_COMMANDS_INFO_COMMANDDESCRIPTION.getContent(channel),
                        command.description,
                        false)
                .addField(Message.COMMANDS_FILTER_BYPASS_INFO_VALUE.getContent(channel),
                        command.value,
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}