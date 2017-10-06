package de.arraying.arraybot.language

import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Entry
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.requests.RestAction

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
enum class Message(private val prefix: Boolean = false) {

    COMMAND_UNAVAILABLE_DEVELOPER,
    COMMAND_UNAVAILABLE_EMBED,
    COMMAND_PERMISSION,
    COMMAND_SUBCOMMAND_UNKNOWN,
    COMMANDS_CUSTOM_EMBED_DESCRIPTION,
    COMMANDS_CUSTOM_EMBED_VALUE(true),
    COMMANDS_EVAL_CODE_PROVIDE,
    COMMANDS_EVAL_ENGINE_INVALID,
    COMMANDS_EVAL_ENGINE_PROVIDE,
    COMMANDS_EVAL_ERROR,
    COMMANDS_EVAL_EVALUATED,
    COMMANDS_HELP_EMBED_ANNOUNCEMENT,
    COMMANDS_HELP_EMBED_DESCRIPTION,
    COMMANDS_HELP_EMBED_HELP_TITLE,
    COMMANDS_HELP_EMBED_HELP_VALUE,
    COMMANDS_HELP_EMBED_START_TITLE,
    COMMANDS_HELP_EMBED_START_VALUE(true),
    COMMANDS_PING_PING,
    COMMANDS_RELOAD_EMBED_DESCRIPTION,
    COMMANDS_RELOAD_EMBED_VALUE(true),
    COMMANDS_RELOAD_INPUT_PROVIDE,
    COMMANDS_RELOAD_LANGUAGES_RELOADED,
    COMMANDS_RELOAD_MODE_PROVIDE,
    COMMANDS_RELOAD_SHARD_ERROR,
    COMMANDS_RELOAD_SHARD_INVALID,
    COMMANDS_RELOAD_SHARD_RELOADED,
    COMMANDS_SCRIPT_ERROR_LINK,
    COMMANDS_SCRIPT_EXECUTED,
    COMMANDS_SCRIPT_PROVIDE,
    COMMANDS_SHARDS_EMBED_DESCRIPTION(true),
    COMMANDS_SHARDS_EMBED_TITLE,
    CUSTOM_ARGUMENT,
    CUSTOM_ARGUMENT_PROVIDE(true),
    CUSTOM_DESCRIPTION,
    CUSTOM_PERMISSION,
    CUSTOM_SYNTAX_INVALID,
    CUSTOM_TYPE_INVALID,
    CUSTOM_TYPE_IMPLEMENTED,
    CUSTOM_TYPE_ROLE_ADDED,
    CUSTOM_TYPE_ROLE_REMOVED,
    CUSTOM_TYPE_ROLE_PERMISSION,
    CUSTOM_TYPE_ROLE_UNKNOWN_ROLE,
    CUSTOM_TYPE_ROLE_UNKNOWN_USER,
    CUSTOM_VALUE_EMPTY,
    EMBED_FOOTER,
    EMBED_TITLE_COMMANDS,
    PAGE_FOOTER,
    PAGE_COMMAND_UNKNOWN,
    PERMISSION_PERMISSION,
    PERMISSION_ROLE,
    PERMISSION_INVALID,
    ZEUS_ERROR,
    ZEUS_ERROR_ARGUMENTS_INDEX,
    ZEUS_ERROR_BAN_REASON_LENGTH,
    ZEUS_ERROR_CHANNEL_NAME_LENGTH,
    ZEUS_ERROR_CHANNEL_NSFW_TYPE,
    ZEUS_ERROR_CHANNEL_POSITION_TYPE,
    ZEUS_ERROR_CHANNEL_TOPIC_LENGTH,
    ZEUS_ERROR_NICKNAME_LENGTH,
    ZEUS_ERROR_NICKNAME_PERMISSION,
    ZEUS_ERROR_MESSAGE_PIN_BOOLEAN,
    ZEUS_ERROR_MESSAGE_PIN_PERMISSION,
    ZEUS_ERROR_PROVIDED;

    /**
     * Returns a usable rest action that can be queued to send to the channel.
     * The replace parameter can be used to format the string.
     */
    fun send(channel: TextChannel, vararg format: String): RestAction<net.dv8tion.jda.core.entities.Message> {
        return channel.sendMessage(getContent(channel.guild.idLong, *format))
    }

    /**
     * Gets the content of the message for the specific guild.
     * The replace parameter can be used to format the string.
     */
    fun getContent(guild: Long, vararg format: String): String {
        val entry = Entry.Category.GUILD.entry as GuildEntry
        var content = Languages.getEntry(name.toLowerCase(), entry.fetch(entry.getField(GuildEntry.Fields.LANGUAGE), guild, null))
        if(prefix) {
            content = content.replace("{prefix}", entry.fetch(entry.getField(GuildEntry.Fields.PREFIX), guild, null))
        }
        content = content
                .replace("{github}", "https://github.com/Arraying/arraybot/")
                .replace("{zwsp}", "​")
                .replace("-", "    **-**")
        content = content.format(*format)
        return content
    }

    /**
     * Alias method to get the content of the message.
     */
    fun getContent(channel: TextChannel, vararg format: String): String {
        return getContent(channel.guild.idLong, *format)
    }

}