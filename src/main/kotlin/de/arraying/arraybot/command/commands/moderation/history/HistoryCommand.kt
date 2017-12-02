package de.arraying.arraybot.command.commands.moderation.history

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.pagination.PageImpl
import de.arraying.arraybot.util.UEmbed
import de.arraying.arraybot.util.UUser
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
class HistoryCommand: DefaultCommand("history",
        CommandCategory.MODERATION,
        Permission.KICK_MEMBERS) {

    /**
     * When the command is execute.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        if(args.size < 2) {
            Message.COMMANDS_HISTORY_USER_PROVIDE.send(channel).queue()
            return
        }
        val userRaw = args[1]
        if(!UUser.ID_PATTERN.matcher(userRaw).find()) {
            Message.COMMANDS_HISTORY_USER_INVALID.send(channel).queue()
            return
        }
        val user = userRaw.replace("\\D".toRegex(), "").toLong()
        val ids = ArrayList<Int>()
        arraybot.punishmentManager.getAllPunishments(guild)
                .filter {
                    it.user == user
                }
                .mapTo(ids) {
                    it.id
                }
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_HISTORY_EMBED_DESCRIPTION.getContent(channel))
        val pages = PageBuilder()
                .withEntries(ids.toTypedArray())
                .withTotal(10)
                .withType(PageBuilder.Type.LIST)
                .withTitle(Message.COMMANDS_HISTORY_EMBED_TITLE.getContent(channel))
                .withEmbed(embed)
                .build()
        var page = PageImpl.FIRST_PAGE
        if(args.size > 3) {
            page = pages.getPageNumber(args[2])
        }
        channel.sendMessage(pages.getPage(page, channel).build()).queue()
    }

}