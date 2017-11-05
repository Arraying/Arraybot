package de.arraying.arraybot.command.commands.utils.help

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UEmbed
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
class HelpCommand: DefaultCommand("help",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("helpme", "h")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        var announcement = arraybot.configuration.announcement
        announcement = if(announcement.length <= Limits.EMBED_FIELD.limit) {
                announcement
            } else {
                announcement.substring(0, Limits.EMBED_FIELD.limit)
            }
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_HELP_EMBED_DESCRIPTION.getContent(channel))
                .addField(Message.COMMANDS_HELP_EMBED_START_TITLE.getContent(channel),
                        Message.COMMANDS_HELP_EMBED_START_VALUE.getContent(channel),
                        false)
                .addField(Message.COMMANDS_HELP_EMBED_HELP_TITLE.getContent(channel),
                        Message.COMMANDS_HELP_EMBED_HELP_VALUE.getContent(channel),
                        false)
        if(!announcement.isEmpty()) {
            embed.addField(Message.COMMANDS_HELP_EMBED_ANNOUNCEMENT.getContent(channel),
                    announcement,
                    false)
        }
        channel.sendMessage(embed.build()).queue()
    }

}