package de.arraying.arraybot.commands.command.moderation.filter.subcommands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ISubCommand
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
class SubCommandFilterRemove: 
        ISubCommand {

    private val arraybot = Arraybot.instance

    /**
     * Gets the name.
     */
    override fun getName(): String {
        return "remove"
    }

    /**
     * Gets the aliases.
     */
    override fun getAliases(): Array<String> {
        return arrayOf("delete")
    }

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val mod = environment.cache?.mod?: return
        if(args.size < 3) {
            Messages.COMMAND_FILTER_REMOVE_PROVIDE.send(channel).queue()
            return
        }
        val stringBuilder = StringBuilder()
        for(i in (2..args.size-1)) {
            stringBuilder.append(args[i])
                    .append(" ")
        }
        val phrase = stringBuilder.toString().toLowerCase().trim()
                .replace("{space}", " ")
        if(!mod.filtered.contains(phrase)) {
            Messages.COMMAND_FILTER_REMOVE_EXISTS.send(channel).queue()
            return
        }
        arraybot.managerSql.removeChatFilterPhrase(environment.guild.idLong, phrase)
        Messages.COMMAND_FILTER_REMOVE_UPDATE.send(channel).queue()
    }

}