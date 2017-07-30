package de.arraying.arraybot.misc

import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.command.custom.CustomCommand
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandSyntax
import de.arraying.arraybot.iface.ICommand
import de.arraying.arraybot.language.Messages
import net.dv8tion.jda.core.entities.TextChannel
import java.util.*
import kotlin.collections.ArrayList

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
open class Pages(private val embedBuilder: CustomEmbedBuilder,
                 private val contentTitle: String,
                 entries: Array<out Any>,
                 itemsPerPage: Int = 1) {

    private val pages = TreeMap<Int, ArrayList<Any>>()

    /**
     * Sets up the pages.
     */
    init {
        if(itemsPerPage < 1) {
            throw IllegalArgumentException("The items per page cannot be < 1.")
        }
        if(entries.isEmpty()) {
            throw IllegalArgumentException("The entries cannot be empty.")
        }
        var currentPage = 1
        var currentIteration = 0
        pages.put(currentPage, ArrayList())
        for(entry in entries) {
            pages[currentPage]?.add(entry)
            currentIteration++
            if(currentIteration >= itemsPerPage) {
                currentPage++
                currentIteration = 0
                pages.put(currentPage, ArrayList())
            }
        }
        if(pages[pages.lastKey()]!!.isEmpty()) {
            pages.remove(pages.lastKey())
        }
    }

    /**
     * Gets the specified page number.
     */
    fun getPage(pageNumber: Int = 1, channel: TextChannel): CustomEmbedBuilder {
        if(!pages.containsKey(pageNumber)) {
            throw IllegalArgumentException("A page with that page number does not exist.")
        }
        val cache = Cache.guilds[channel.guild.idLong]!!
        val pageEntries = pages[pageNumber]
        val pageContentBuilder = StringBuilder()
        if(pageEntries == null) {
            throw IllegalArgumentException("The page entries were null.")
        }
        for(entry in pageEntries) {
            if(entry !is ICommand) {
                pageContentBuilder
                        .append("- $entry")
                        .append("\n")
                continue
            }
            val command = entry
            val category: String
            val syntax: String
            val description: String
            if(command is DefaultCommand) {
                syntax = "`${cache.prefix}${Messages.getMessage(channel, command.syntaxPath)}`"
                category = " **➜** __${Messages.MISC_DEFAULTCOMMAND.content(channel)}, ${command.category.toString().toLowerCase()}__"
                description = Messages.replace("-  ${Messages.getMessage(channel, command.descriptionPath)}", channel)
            } else if(command is CustomCommand) {
                syntax = "`${cache.prefix}" +
                        if(command.syntax == CustomCommandSyntax.STARTSWITH) {
                            Messages.CUSTOMCOMMAND_SYNTAX.content(channel)
                                    .replace("{name}", command.name)
                        } else {
                            command.name
                        } +
                        "`"
                category = " **➜** __${Messages.MISC_CUSTOMCOMMAND.content(channel)}__"
                val localDescription = command.description?: Messages.COMMAND_NODESCRIPTION.content(channel)
                description = Messages.replace("- $localDescription", channel)
            } else {
                syntax = Messages.MISC_ERROR.content(channel)
                category = syntax
                description = syntax
            }
            pageContentBuilder
                    .append(syntax)
                    .append(category)
                    .append("\n")
                    .append(description)
                    .append("\n\n")
        }
        val pageContent = pageContentBuilder.toString()
        return embedBuilder.addField(contentTitle,
                    pageContent,
                    false)
                .setFooter(Messages.COMMAND_EMBED_PAGE.content(channel)
                            .replace("{page}", pageNumber.toString())
                            .replace("{total}", getTotal().toString()),
                        null)
    }

    /**
     * Gets the amount of misc.
     */
    fun getTotal(): Int {
        return pages.size
    }

}