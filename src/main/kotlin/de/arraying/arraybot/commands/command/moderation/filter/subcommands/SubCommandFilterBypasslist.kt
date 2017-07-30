package de.arraying.arraybot.commands.command.moderation.filter.subcommands

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.iface.ISubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.misc.Pages
import de.arraying.arraybot.utils.Utils
import java.util.TreeSet

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
class SubCommandFilterBypasslist:
        ISubCommand {

    override val subCommandName = "bypasslist"

    /**
     * Gets the aliases.
     */
    override fun getAliases(): Array<String> {
        return arrayOf("listbypass", "listbypasses")
    }

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val embed = Utils.getEmbed(channel)
        val bypasses = TreeSet<Long>(environment.cache!!.mod!!.filterBypasses.keys)
        if(bypasses.isEmpty()) {
            Messages.COMMAND_FILTER_BYPASSLIST_EMPTY.send(channel).queue()
            return
        }
        embed.setDescription(Messages.COMMAND_FILTER_BYPASSLIST_DESCRIPTION.content(channel))
        val pages = Pages(embed,
                Messages.COMMAND_FILTER_BYPASSLIST_TITLE.content(channel),
                bypasses.toTypedArray(),
                10)
        if(args.size > 2
                && Utils.isInt(args[2])
                && args[2].toInt() > 0
                && args[2].toInt() <= pages.getTotal()) {
            channel.sendMessage(pages.getPage(args[2].toInt(), channel).build()).queue()
            return
        }
        channel.sendMessage(pages.getPage(channel = channel).build()).queue()
    }

}