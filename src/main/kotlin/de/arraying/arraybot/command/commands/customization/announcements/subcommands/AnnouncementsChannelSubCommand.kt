package de.arraying.arraybot.command.commands.customization.announcements.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UChannel
import de.arraying.arraybot.util.UDefaults

/**
 * Copyright 2018 Arraying
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
class AnnouncementsChannelSubCommand: SubCommand("channel",
        aliases = arrayOf("c", "ch")) {

    /**
     * When the sub-command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size < 3) {
            val announcementChannelId = entry.fetch(entry.getField(GuildEntry.Fields.ANNOUNCEMENT_CHANNEL), guild, null).toLong()
            if(announcementChannelId == UDefaults.DEFAULT_SNOWFLAKE.toLong()) {
                Message.COMMANDS_ANNOUNCEMENTS_CHANNEL_NONE.send(channel).queue()
                return
            }
            val announcementChannel = environment.guild.getTextChannelById(announcementChannelId)
            if(announcementChannel == null) {
                Message.COMMANDS_ANNOUNCEMENTS_CHANNEL_INVALID.send(channel).queue()
                return
            }
            Message.COMMANDS_ANNOUNCEMENTS_CHANNEL.send(channel, announcementChannel.asMention).queue()
            return
        }
        val chan = UChannel.getTextChannel(environment.guild, args[2])
        if(chan == null) {
            Message.CHANNEL_INVALID.send(channel).queue()
            return
        }
        entry.push(entry.getField(GuildEntry.Fields.ANNOUNCEMENT_CHANNEL), guild, null, chan.idLong)
        Message.COMMANDS_ANNOUNCEMENTS_CHANNEL_UPDATED.send(channel).queue()
    }

}