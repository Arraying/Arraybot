package de.arraying.arraybot.command.commands.customization.announcer

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UArguments
import de.arraying.arraybot.util.UChannel
import de.arraying.arraybot.util.UDefaults

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
class AnnouncerGenericSubCommand(override val name: String,
                                 override val aliases: Array<String>,
                                 private val join: Boolean): SubCommand(name, aliases) {

    private val toggleSubCommand = "toggle"
    private val channelSubCommand = "channel"
    private val messageSubCommand = "message"

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        if(args.size < 3) {
            Message.COMMANDS_ANNOUNCER_PROVIDE.send(channel).queue()
            return
        }
        val subCommand = args[2].toLowerCase()
        val entry = Category.GUILD.entry as GuildEntry
        when(subCommand) {
            toggleSubCommand -> {
                val field = if(join) {
                    GuildEntry.Fields.JOIN_ANNOUNCER
                } else {
                    GuildEntry.Fields.LEAVE_ANNOUNCER
                }
                val announcing = entry.fetch(entry.getField(field), guildId, null).toBoolean()
                entry.push(entry.getField(field), guildId, null, !announcing)
                if(announcing) {
                    Message.COMMANDS_ANNOUNCER_TOGGLE_OFF.send(channel).queue()
                } else {
                    Message.COMMANDS_ANNOUNCER_TOGGLE_ON.send(channel).queue()
                }
                return
            }
            channelSubCommand -> {
                val field = if(join) {
                    GuildEntry.Fields.JOIN_CHANNEL
                } else {
                    GuildEntry.Fields.LEAVE_CHANNEL
                }
                if(args.size < 4) {
                    val channelId = entry.fetch(entry.getField(field), guildId, null).toLong()
                    if(channelId == UDefaults.DEFAULT_SNOWFLAKE.toLong()) {
                        Message.COMMANDS_ANNOUNCER_CHANNEL_NONE.send(channel).queue()
                        return
                    }
                    val chan = guild.getTextChannelById(channelId)
                    if(chan == null) {
                        Message.COMMANDS_ANNOUNCER_CHANNEL_INVALID.send(channel).queue()
                        return
                    }
                    Message.COMMANDS_ANNOUNCER_CHANNEL.send(channel, chan.asMention).queue()
                    return
                }
                val chan = UChannel.getTextChannel(guild, args[3])
                if(chan == null) {
                    Message.CHANNEL_INVALID.send(channel).queue()
                    return
                }
                entry.push(entry.getField(field), guildId, null, chan.idLong)
                Message.COMMANDS_ANNOUNCER_UPDATED.send(channel).queue()
                return
            }
            messageSubCommand -> {
                val field = if(join) {
                    GuildEntry.Fields.JOIN_MESSAGE
                } else {
                    GuildEntry.Fields.LEAVE_MESSAGE
                }
                if(args.size < 4) {
                    val message = entry.fetch(entry.getField(field), guildId, null)
                    if(message == UDefaults.DEFAULT_NULL) {
                        Message.COMMANDS_ANNOUNCER_MESSAGE_NONE.send(channel).queue()
                        return
                    }
                    Message.COMMANDS_ANNOUNCER_MESSAGE.send(channel, message).queue()
                    return
                }
                val message = UArguments.combine(args.toTypedArray(), 3)
                if(message.length > Limits.ANNOUNCER_MESSAGE.limit) {
                    Message.COMMANDS_ANNOUNCER_MESSAGE_LENGTH.send(channel, Limits.ANNOUNCER_MESSAGE.limit.toString()).queue()
                    return
                }
                entry.push(entry.getField(field), guildId, null, message)
                Message.COMMANDS_ANNOUNCER_UPDATED.send(channel).queue()
                return
            }
            else -> Message.COMMAND_SUBCOMMAND_UNKNOWN.send(channel).queue()
        }
    }

}