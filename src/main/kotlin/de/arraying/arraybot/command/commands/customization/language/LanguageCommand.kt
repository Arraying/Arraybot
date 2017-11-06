package de.arraying.arraybot.command.commands.customization.language

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Languages
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
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
class LanguageCommand: DefaultCommand("language",
        CommandCategory.CUSTOMIZATION,
        Permission.MANAGE_SERVER,
        aliases = arrayOf("lang", "i18n")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size < 2) {
            val language = entry.fetch(entry.getField(GuildEntry.Fields.LANGUAGE), guildId, null)
            val embed = UEmbed.getEmbed(channel)
                    .setDescription(Message.COMMANDS_LANGUAGE_EMBED_DESCRIPTION.getContent(channel, language))
            val pages = PageBuilder()
                    .withEntries(Languages.getAll().toTypedArray())
                    .withTotal(100)
                    .withType(PageBuilder.Type.LIST)
                    .withTitle(Message.COMMANDS_LANGUAGE_EMBED_TITLE.getContent(channel))
                    .withEmbed(embed)
                    .build()
            channel.sendMessage(pages.getPage(pages.getPageNumber("1"), channel).build()).queue()
            return
        }
        val language = args[1]
        if(!Languages.getAll().contains(language)) {
            Message.COMMANDS_LANGUAGE_INVALID.send(channel).queue()
            return
        }
        entry.push(entry.getField(GuildEntry.Fields.LANGUAGE), guildId, null, language)
        Message.COMMANDS_LANGUAGE_UPDATED.send(channel).queue()
    }

}