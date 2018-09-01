package de.arraying.arraybot.command.commands.utils.commands.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.Commands
import de.arraying.arraybot.command.templates.CustomCommand
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UEmbed
import de.arraying.arraybot.util.UFormatting

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
class CommandsInfoSubCommand: SubCommand("info",
        aliases = arrayOf("commandinfo", "wat", "i")) {

    /**
     * When the sub command is getting executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guildId = channel.guild.idLong
        val entry = Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
        if(args.size < 3) {
            Message.COMMAND_NAME_PROVIDE.send(channel).queue()
            return
        }
        val commandName = args[2].toLowerCase()
        when {
            Commands.commands.getByKeyOrAlias(commandName) != null -> {
                val command = Commands.commands.getByKeyOrAlias(commandName)
                val builder = StringBuilder()
                for(alias in command.aliases) {
                    builder.append(alias)
                            .append(", ")
                }
                val guildEntry = Category.GUILD.entry as GuildEntry
                val aliases = if(builder.toString().isEmpty()) Message.MISC_NONE.getContent(channel)
                        else builder.toString().substring(0, builder.toString().length-2)
                val language = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.LANGUAGE), guildId, null)
                val embed = UEmbed.getEmbed(channel)
                        .setDescription(Message.COMMANDS_COMMANDS_INFO_DESCRIPTION.getContent(channel))
                        .addField(Message.COMMANDS_COMMANDS_INFO_NAME.getContent(channel),
                                commandName,
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_ALIASES.getContent(channel),
                                aliases,
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_CATEGORY.getContent(channel),
                                UFormatting.displayableEnumField(command.category.toString()),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_PERMISSION.getContent(channel),
                                command.permission.getName(),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_COMMANDDESCRIPTION.getContent(channel),
                                command.getDescription(language),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_SYNTAX.getContent(channel),
                                guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.PREFIX), guildId, null) + command.getSyntax(language),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_HELP_TITLE.getContent(channel),
                                Message.COMMANDS_COMMANDS_INFO_HELP_VALUE.getContent(channel, commandName),
                                false)
                channel.sendMessage(embed.build()).queue()
            }
            entry.contains(channel.guild.idLong, commandName) -> {
                val command = CustomCommand.fromRedis(guildId, commandName)
                val embed = UEmbed.getEmbed(channel)
                        .setDescription(Message.COMMANDS_COMMANDS_INFO_DESCRIPTION.getContent(channel))
                        .addField(Message.COMMANDS_COMMANDS_INFO_NAME.getContent(channel),
                                commandName,
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_TYPE.getContent(channel),
                                UFormatting.displayableEnumField(command.type.toString()),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_PERMISSION.getContent(channel),
                                command.permission.toString(channel),
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_COMMANDDESCRIPTION.getContent(channel),
                                command.description,
                                false)
                        .addField(Message.COMMANDS_COMMANDS_INFO_SYNTAX.getContent(channel),
                                command.getSyntax(channel),
                                false)
                channel.sendMessage(embed.build()).queue()
            }
            else -> Message.COMMAND_NAME_INVALID.send(channel).queue()
        }
    }

}