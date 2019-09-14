package de.arraying.arraybot.command.commands.utils.id

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.pagination.PageImpl
import de.arraying.arraybot.util.UArguments
import de.arraying.arraybot.util.UEmbed
import net.dv8tion.jda.api.Permission

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
class IDCommand: DefaultCommand("id",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        if(args.size < 2) {
            Message.COMMANDS_ID_PROVIDE.send(channel).queue()
            return
        }
        val name = UArguments.combine(args.toTypedArray(), 1)
        val entities = ArrayList<String>()
        guild.getMembersByEffectiveName(name, true).mapTo(entities) { "[${Message.COMMANDS_ID_USER.getContent(channel)}] \"${it.user.name}#${it.user.discriminator}\": ${it.user.idLong}" }
        guild.getRolesByName(name, true).mapTo(entities) { "[${Message.COMMANDS_ID_ROLE.getContent(channel)}] \"${it.name}\": ${it.idLong}" }
        guild.getTextChannelsByName(name, true).mapTo(entities) { "[${Message.COMMANDS_ID_CHANNEL_TEXT.getContent(channel)}] \"${it.name}\": ${it.idLong}" }
        guild.getVoiceChannelsByName(name, true).mapTo(entities) { "[${Message.COMMANDS_ID_CHANNEL_VOICE.getContent(channel)}] \"${it.name}\": ${it.idLong}" }
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_ID_EMBED_DESCRIPTION.getContent(channel))
        val pages = PageBuilder()
                .withEntries(entities.toTypedArray())
                .withTotal(10)
                .withType(PageBuilder.Type.LIST)
                .withTitle(Message.COMMANDS_ID_EMBED_TITLE.getContent(channel))
                .withEmbed(embed)
                .build()
        var page = PageImpl.FIRST_PAGE
        if(args.size > 3) {
            page = pages.getPageNumber(args[2])
        }
        channel.sendMessage(pages.getPage(page, channel).build()).queue()
    }

}