package de.arraying.arraybot.listeners

import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.CMod
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.UPlaceholders
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.events.message.guild.GenericGuildMessageEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.core.exceptions.PermissionException
import net.dv8tion.jda.core.hooks.ListenerAdapter
import net.dv8tion.jda.core.utils.PermissionUtil
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

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
class ListenerFilter:
        ListenerAdapter() {

    /**
     * Checks if a message should be filtered.
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
        if(event == null) {
            return
        }
        launch(CommonPool) {
            handleFilter(event)
        }
    }

    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent?) {
        if(event == null) {
            return
        }
        launch(CommonPool) {
            handleFilter(event)
        }
    }

    /**
     * Handles the filter related things.
     */
    private suspend fun handleFilter(event: GenericGuildMessageEvent) {
        val messageObject = event.channel.getMessageById(event.messageIdLong).complete()
        val messageAuthor = messageObject.author
        val cache = Cache.guilds[event.guild.idLong]?: return
        val mod = cache.mod?: return
        if(!mod.filterEnabled) {
            return
        }
        val message = messageObject.rawContent
        if(messageObject.author.idLong == event.jda.selfUser.idLong
                || (mod.filterMessage != null
                    && message == UPlaceholders.process(messageObject.member, mod.filterMessage!!))){
            return
        }
        var filteredPhrase: String? = null
        phraseLoop@for(phrase in mod.filtered) {
            if(needsFiltering(mod, phrase, message)) {
                filteredPhrase = phrase
                break@phraseLoop
            }
            if(messageAuthor.idLong == event.jda.selfUser.idLong) {
                continue
            }
            embedLoop@for(embed in messageObject.embeds) {
                if(embedNeedsFiltering(mod, phrase, embed)) {
                    filteredPhrase = phrase
                    break@phraseLoop
                }
            }
        }
        if(filteredPhrase == null) {
            return
        }
        try {
            messageObject.delete().queue()
        } catch(exception: PermissionException) {
            return
        }
        if(mod.filterSilent) {
           return
        }
        val filterMessage = UPlaceholders.process(messageObject.member, mod.filterMessage?: Messages.FILTER_MESSAGE.content(event.guild.idLong))
        if(!mod.filterPrivate) {
            if(PermissionUtil.checkPermission(event.channel, event.guild.selfMember, Permission.MESSAGE_WRITE)) {
                event.channel.sendMessage(filterMessage).queue()
            }
        } else {
            if(messageAuthor.idLong != event.jda.selfUser.idLong) {
                messageAuthor.openPrivateChannel().queue {
                    it.sendMessage(filterMessage.replace("{term}", filteredPhrase!!)).queue()
                }
            }
        }
    }

    /**
     * Checks if a message needs to be filtered.
     */
    private suspend fun needsFiltering(mod: CMod, phrase: String, message: String): Boolean  {
        if(!mod.filterRegex) {
            val phraseTrimmed = phrase.trim().toLowerCase()
            if(message.contains(phrase.toLowerCase())
                    || (message.startsWith(phraseTrimmed) && message.contains(" "))
                    || (message.endsWith(phraseTrimmed) && message.contains(" "))
                    || message.equals(phraseTrimmed, true)) {
                return true
            }
        } else {
            try {
                val pattern = Pattern.compile(phrase)
                val matcher = pattern.matcher(message)
                if(matcher.find()) {
                    return true
                }
            } catch(exception: PatternSyntaxException) {
                return false
            }
        }
        return false
    }

    /**
     * Checks if an embed needs filtering.
     */
    private suspend fun embedNeedsFiltering(mod: CMod, phrase: String, embed: MessageEmbed): Boolean {
        if(embed.description != null
                && needsFiltering(mod, phrase, embed.description.toLowerCase())) {
            return true
        }
        if(embed.title != null
                && needsFiltering(mod, phrase, embed.title.toLowerCase())) {
            return true
        }
        if(embed.footer != null
                && embed.footer.text != null
                && needsFiltering(mod, phrase, embed.footer.text.toLowerCase())) {
            return true
        }
        return false
    }

}