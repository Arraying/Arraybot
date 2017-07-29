package de.arraying.arraybot.commands.commands.utils.commands.subcommands

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.commands.custom.entities.CustomCommandSyntax
import de.arraying.arraybot.commands.types.CustomCommand
import de.arraying.arraybot.commands.types.DefaultCommand
import de.arraying.arraybot.commands.types.SubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.Utils

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
class SubCommandCommandsInfo: 
        SubCommand("info") {

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val cache = environment.cache!!
        if(args.size < 3) {
            Messages.COMMAND_COMMANDS_INFO_SPECIFY.send(channel).queue()
            return
        }
        val commandName = args[2].toLowerCase()
        val commandResult = Commands.getCommands(commandName, cache)
        if(commandResult.isEmpty()) {
            Messages.COMMAND_COMMANDS_UNKNOWN_COMMAND.send(channel).queue()
            return
        }
        for(command in commandResult) {
            val description: String
            val category: String
            val permission: String
            val syntax: String
            val help: String
            if(command is DefaultCommand) {
                description = Messages.getMessage(channel, command.descriptionPath)
                category = Utils.setFirstUppercase(command.category)
                permission = command.permission.toString()
                syntax = Messages.getMessage(channel, command.syntaxPath)
                help = Messages.COMMAND_COMMANDS_INFO_HELP_DESCRIPTION.content(channel)
                        .replace("{command}", command.name)
            } else if(command is CustomCommand) {
                description = command.description?: Messages.COMMAND_NODESCRIPTION.content(channel)
                category = Messages.MISC_CUSTOMCOMMAND.content(channel)
                permission = command.permission.getAsString(channel)
                syntax = if(command.syntax == CustomCommandSyntax.EQUALS) {
                    cache.prefix+command.name
                } else {
                    cache.prefix+Messages.CUSTOMCOMMAND_SYNTAX.content(channel)
                            .replace("{name}", command.name)
                }
                help = Messages.MISC_NONE.content(channel)
            } else {
                description = Messages.MISC_ERROR.content(channel)
                category = description
                permission = description
                syntax = description
                help = permission
            }
            val embed = Utils.getEmbed(channel)
                    .setDescription(Messages.COMMAND_COMMANDS_INFO_DESCRIPTION.content(channel))
                    .addField(Messages.COMMAND_COMMANDS_INFO_NAME.content(channel),
                            command.name,
                            false)
                    .addField(Messages.COMMAND_COMMANDS_INFO_DESCRIPTIONCOMMAND.content(channel),
                            description,
                            false)
                    .addField(Messages.COMMAND_COMMANDS_INFO_CATEGORY.content(channel),
                            category,
                            false)
                    .addField(Messages.COMMAND_COMMANDS_INFO_PERMISSION.content(channel),
                            permission,
                            false)
                    .addField(Messages.COMMAND_COMMANDS_INFO_SYNTAX.content(channel),
                            "`$syntax`",
                            false)
                    .addField(Messages.COMMAND_COMMANDS_INFO_HELP.content(channel),
                            help,
                            false)
            channel.sendMessage(embed.build()).queue()
        }
    }

}