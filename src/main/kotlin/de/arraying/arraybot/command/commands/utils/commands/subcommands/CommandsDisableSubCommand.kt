package de.arraying.arraybot.command.commands.utils.commands.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.Commands
import de.arraying.arraybot.command.commands.utils.commands.CommandsCommandData
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.internal.utils.PermissionUtil

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
class CommandsDisableSubCommand: SubCommand("disable",
        aliases = arrayOf("disablecommand", "d")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(!PermissionUtil.checkPermission(channel, environment.member, Permission.MANAGE_SERVER)) {
            Message.COMMAND_PERMISSION.send(channel).queue()
            return
        }
        if(args.size < 3) {
            Message.COMMAND_NAME_PROVIDE.send(channel).queue()
            return
        }
        val commandName = args[2].toLowerCase()
        val command = Commands.commands.getByKeyOrAlias(commandName)
        if(command == null) {
            Message.COMMAND_NAME_INVALID.send(channel).queue()
            return
        }
        if(command.category == DefaultCommand.CommandCategory.DEVELOPER) {
            Message.COMMANDS_COMMANDS_DISABLE_DEVELOPER.send(channel).queue()
            return
        }
        if(command.name == CommandsCommandData.name) {
            Message.COMMANDS_COMMANDS_DISABLE_COMMANDS.send(channel).queue()
            return
        }
        val entry = Category.DISABLED_COMMAND.entry as SetEntry
        entry.add(channel.guild.idLong, commandName)
        Message.COMMANDS_COMMANDS_DISABLE_DISABLED.send(channel, commandName).queue()
    }

}