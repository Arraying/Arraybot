package de.arraying.arraybot.command.commands.customization.custom

import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
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
class CustomCommand: DefaultCommand("custom",
        CommandCategory.CUSTOMIZATION,
        Permission.MANAGE_SERVER,
        aliases = arrayOf("customcommand", "customcommands", "cc")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_CUSTOM_EMBED_DESCRIPTION.content(channel))
                .addField(Message.EMBED_TITLE_COMMANDS.content(channel),
                        Message.COMMANDS_CUSTOM_EMBED_VALUE.content(channel, true),
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}