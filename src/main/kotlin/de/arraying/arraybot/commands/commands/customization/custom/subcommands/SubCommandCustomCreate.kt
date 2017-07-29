package de.arraying.arraybot.commands.commands.customization.custom.subcommands

import de.arraying.arraybot.commands.other.CommandEnvironment
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
class SubCommandCustomCreate:
        SubCommand("create",
                arrayOf("add")) {

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Messages.COMMAND_CUSTOM_SPECIFY.send(channel).queue()
            return
        }
        val commandName = args[2].toLowerCase()
        if(environment.cache!!.customCommands.containsKey(commandName)) {
            Messages.COMMAND_CUSTOM_CREATE_EXISTS.send(channel).queue()
            return
        }
        arraybot.managerSql.createCustomCommand(environment.guild.idLong, commandName)
        Messages.COMMAND_CUSTOM_CREATE.send(channel).queue()
    }

}