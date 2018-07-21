package de.arraying.arraybot.command.commands.customization.announcements.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.AnnouncementEntry
import de.arraying.arraybot.data.database.categories.AnnouncementIdsEntry
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UArguments

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
class AnnouncementsAddSubCommand: SubCommand("add",
        aliases = arrayOf("a", "n", "new")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_PROVIDE_TEXT.send(channel).queue()
            return
        }
        val guild = environment.guild.idLong
        val guildEntry = Category.GUILD.entry as GuildEntry
        val announcementsEntry = Category.ANNOUNCEMENT_IDS.entry as AnnouncementIdsEntry
        val announcementEntry = Category.ANNOUNCEMENT.entry as AnnouncementEntry
        val announcement = UArguments.combine(args.toTypedArray(), 2)
        if(announcement.length > Limits.ANNOUNCEMENTS_MESSAGE.limit) {
            Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_LENGTH.send(channel, Limits.ANNOUNCEMENTS_MESSAGE.limit.toString())
            return
        }
        val id = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.COUNT_ANNOUNCEMENT), guild, null).toInt() + 1
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.COUNT_ANNOUNCEMENT), guild, null, id)
        announcementsEntry.add(guild, id)
        announcementEntry.push(announcementEntry.getField(AnnouncementEntry.Fields.ANNOUNCEMENT), guild, id, announcement)
        Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_UPDATED.send(channel).queue()
    }

}