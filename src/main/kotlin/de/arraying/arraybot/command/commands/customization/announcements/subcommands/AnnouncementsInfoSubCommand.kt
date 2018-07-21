package de.arraying.arraybot.command.commands.customization.announcements.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.AnnouncementEntry
import de.arraying.arraybot.data.database.categories.AnnouncementIdsEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDatatypes

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
class AnnouncementsInfoSubCommand: SubCommand("info",
        aliases = arrayOf("i")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild.idLong
        val announcementsEntry = Category.ANNOUNCEMENT_IDS.entry as AnnouncementIdsEntry
        if(args.size < 3) {
            Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_PROVIDE_ID.send(channel).queue()
            return
        }
        val idRaw = args[2]
        if(!UDatatypes.isInt(idRaw)) {
            Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_ID_NUMBER.send(channel).queue()
            return
        }
        val id = idRaw.toInt()
        if(!announcementsEntry.contains(guild, id)) {
            Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT_ID_EXISTS.send(channel).queue()
            return
        }
        val announcementEntry = Category.ANNOUNCEMENT.entry as AnnouncementEntry
        val text = announcementEntry.fetch(announcementEntry.getField(AnnouncementEntry.Fields.ANNOUNCEMENT), guild, id)
        Message.COMMANDS_ANNOUNCEMENTS_ANNOUNCEMENT.send(channel, text).queue()
    }

}