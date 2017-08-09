package de.arraying.arraybot.commands.command.moderation.filter.subcommands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ISubCommand
import de.arraying.arraybot.core.language.Messages
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
class SubCommandFilterBypassRemove :
        ISubCommand {

    private val arraybot = Arraybot.instance

    /**
     * Gets the name.
     */
    override fun getName(): String {
        return "bypassremove"
    }

    /**
     * Gets the aliases
     */
    override fun getAliases(): Array<String> {
        return arrayOf("removebypass")
    }

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Messages.COMMAND_FILTER_BYPASSREMOVE_PROVIDE.send(channel).queue()
            return
        }
        if(!Utils.isLong(args[2])) {
            Messages.COMMAND_FILTER_BYPASSREMOVE_INVALID.send(channel).queue()
            return
        }
        val bypassId = args[2].toLong()
        val mod = environment.cache!!.mod!!
        if(!mod.filterBypasses.containsKey(bypassId)) {
            Messages.COMMAND_FILTER_BYPASSREMOVE_EXISTS.send(channel).queue()
            return
        }
        arraybot.managerSql.removeChatFilterBypass(environment.guild.idLong, bypassId)
        Messages.COMMAND_FILTER_BYPASSREMOVE_UPDATE.send(channel).queue()
    }

}