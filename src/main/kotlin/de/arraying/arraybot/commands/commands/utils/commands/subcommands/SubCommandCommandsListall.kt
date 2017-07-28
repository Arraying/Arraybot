package de.arraying.arraybot.commands.commands.utils.commands.subcommands

import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.entities.SubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.misc.Pages
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
class SubCommandCommandsListall: 
        SubCommand("listall") {

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val member = environment.member
        val commands = Commands.getCommandList(member, channel)
        if(commands.isEmpty()) {
            Messages.COMMAND_COMMANDS_EMPTY.send(channel).queue()
            return
        }
        val embed = Utils.getEmbed(channel)
                .setDescription(Messages.COMMAND_COMMANDS_LISTALL_DESCRIPTION.content(channel))
        val pages = Pages(embed,
                Messages.COMMAND_COMMANDS_LISTALL_COMMANDS.content(channel),
                commands,
                7)
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