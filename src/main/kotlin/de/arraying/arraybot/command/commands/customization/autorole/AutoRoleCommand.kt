package de.arraying.arraybot.command.commands.customization.autorole

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.command.templates.SubCommand
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
class AutoRoleCommand(override val subCommands: Array<SubCommand>): DefaultCommand("autorole",
        CommandCategory.CUSTOMIZATION,
        Permission.MANAGE_SERVER,
        aliases = arrayOf("ar")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_AUTOROLE_EMBED_DESCRIPTION.getContent(channel))
                .addField(Message.EMBED_TITLE_COMMANDS.getContent(channel),
                        Message.COMMANDS_AUTOROLE_EMBED_VALUE.getContent(channel),
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}