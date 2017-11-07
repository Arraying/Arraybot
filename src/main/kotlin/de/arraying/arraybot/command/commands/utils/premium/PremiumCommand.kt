package de.arraying.arraybot.command.commands.utils.premium

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
class PremiumCommand: DefaultCommand("premium",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("patreon")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_PREMIUM_EMBED_DESCRIPTION.getContent(channel, isPremium(environment).toString()))
                .addField(Message.COMMANDS_PREMIUM_EMBED_WHAT_TITLE.getContent(channel),
                        Message.COMMANDS_PREMIUM_EMBED_WHAT_VALUE.getContent(channel),
                        false)
                .addField(Message.COMMANDS_PREMIUM_EMBED_WHY_TITLE.getContent(channel),
                        Message.COMMANDS_PREMIUM_EMBED_WHY_VALUE.getContent(channel),
                        false)
                .addField(Message.COMMANDS_PREMIUM_EMBED_LOCKED_TITLE.getContent(channel),
                        Message.COMMANDS_PREMIUM_EMBED_LOCKED_VALUE.getContent(channel, Limits.VS_CAP.limit.toString()),
                        false)
                .addField(Message.COMMANDS_PREMIUM_EMBED_HOW_TITLE.getContent(channel),
                        Message.COMMANDS_PREMIUM_EMBED_HOW_VALUE.getContent(channel),
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}