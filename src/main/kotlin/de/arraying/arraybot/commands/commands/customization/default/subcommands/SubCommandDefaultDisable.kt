package de.arraying.arraybot.commands.commands.customization.default.subcommands

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.types.DefaultCommand
import de.arraying.arraybot.commands.types.SubCommand
import de.arraying.arraybot.language.Messages

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
class SubCommandDefaultDisable:
        SubCommand("disable",
                arrayOf("off")) {

    private val commandName = "default"

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Messages.COMMAND_DEFAULT_SPECIFY.send(channel).queue()
            return
        }
        val commandName = args[2].toLowerCase()
        if(!Commands.commands.any {
            it.name == commandName
        }) {
            Messages.COMMAND_DEFAULT_EXISTS.send(channel).queue()
            return
        }
        if(Commands.commands.any {
            it.name == commandName
                && it.category == DefaultCommand.CommandCategory.DEVELOPER
        }) {
            Messages.COMMAND_DEFAULT_DISABLE_DEVELOPER.send(channel).queue()
            return
        }
        if(commandName == this.commandName) {
            Messages.COMMAND_DEFAULT_DISABLE_DEFAULT.send(channel).queue()
            return
        }
        if(environment.cache!!.disabled.contains(commandName)) {
            Messages.COMMAND_DEFAULT_DISABLE_ALREADY.send(channel).queue()
            return
        }
        arraybot.managerSql.addDisabledCommand(environment.guild.idLong, commandName)
        Messages.COMMAND_DEFAULT_DISABLE.send(channel).queue()
    }

}