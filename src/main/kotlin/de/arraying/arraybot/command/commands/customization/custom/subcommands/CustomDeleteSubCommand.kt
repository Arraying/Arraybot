package de.arraying.arraybot.command.commands.customization.custom.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.CustomCommandEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message

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
class CustomDeleteSubCommand: SubCommand("delete",
        aliases = arrayOf("remove", "d")) {

    /**
     * When the sub command is executed.
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
        val customCommand = Category.CUSTOM_COMMAND.entry as CustomCommandEntry
        customCommand.deleteSingleEntry(guildId, name)
        Message.COMMANDS_CUSTOM_DELETED.send(channel).queue()
    }

}