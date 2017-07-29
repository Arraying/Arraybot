package de.arraying.arraybot.commands.commands.utils.help

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.commands.types.DefaultCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.Utils
import net.dv8tion.jda.core.Permission

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
class CommandHelp:
        DefaultCommand("help",
                CommandCategory.UTILS,
                Permission.MESSAGE_WRITE) {

    /**
     * Invokes the help command which shows help.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val embed = Utils.getEmbed(channel)
                .setDescription(Messages.COMMAND_HELP_EMBED_DESCRIPTION.content(channel))
                .addField(Messages.COMMAND_HELP_EMBED_COMMANDS_TITLE.content(channel),
                        Messages.COMMAND_HELP_EMBED_COMMANDS_VALUE.content(channel),
                        false)
                .addField(Messages.COMMAND_HELP_EMBED_HELP_TITLE.content(channel),
                        Messages.COMMAND_HELP_EMBED_HELP_VALUE.content(channel),
                        false)
                .addField(Messages.COMMAND_HELP_EMBED_ANNOUNCEMENT.content(channel),
                            if(arraybot.configuration.miscAnnouncement.isNotEmpty()) {
                                arraybot.configuration.miscAnnouncement
                            } else {
                                Messages.MISC_NONE.content(channel)
                            }+"\n​",
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}