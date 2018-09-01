package de.arraying.arraybot.command.commands.customization.announcements.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.threadding.impl.AnnouncementsTask
import de.arraying.arraybot.util.Limits
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
class AnnouncementsIntervalSubCommand: SubCommand("interval",
        aliases = arrayOf("i")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size < 3) {
            val interval = entry.fetch(entry.getField(GuildEntry.Fields.ANNOUNCEMENT_INTERVAL), guild, null)
            Message.COMMANDS_ANNOUNCEMENTS_INTERVAL.send(channel, interval).queue()
            return
        }
        val newRaw = args[2]
        if(!UDatatypes.isInt(newRaw)) {
            Message.COMMANDS_ANNOUNCEMENTS_INTERVAL_INVALID.send(channel,
                    Limits.ANNOUNCEMENTS_INTERVAL_MIN.limit.toString(),
                    Limits.ANNOUNCEMENTS_INTERVAL_MAX.limit.toString()).queue()
            return
        }
        val new = newRaw.toInt()
        if(new > Limits.ANNOUNCEMENTS_INTERVAL_MAX.limit
            || new < Limits.ANNOUNCEMENTS_INTERVAL_MIN.limit) {
            Message.COMMANDS_ANNOUNCEMENTS_INTERVAL_INVALID.send(channel,
                    Limits.ANNOUNCEMENTS_INTERVAL_MIN.limit.toString(),
                    Limits.ANNOUNCEMENTS_INTERVAL_MAX.limit.toString()).queue()
            return
        }
        entry.push(entry.getField(GuildEntry.Fields.ANNOUNCEMENT_INTERVAL), guild, null, new)
        AnnouncementsTask.getTask(guild)?.setInterval(new)
        Message.COMMANDS_ANNOUNCEMENTS_INTERVAL_UPDATED.send(channel).queue()
    }

}