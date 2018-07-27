package de.arraying.arraybot.command.commands.customization.announcements.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.threadding.impl.AnnouncementsTask

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
class AnnouncementsToggleSubCommand: SubCommand("toggle",
        aliases = arrayOf("t")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        val field = entry.getField(GuildEntry.Fields.ANNOUNCEMENT_ANNOUNCER)
        val announcing = entry.fetch(field, guild, null)!!.toBoolean()
        entry.push(field, guild, null, !announcing)
        if(announcing) {
            Message.COMMANDS_ANNOUNCEMENTS_TOGGLE_OFF.send(channel).queue()
            AnnouncementsTask.stopTask(guild)
        } else {
            AnnouncementsTask.addTask(guild)
            Message.COMMANDS_ANNOUNCEMENTS_TOGGLE_ON.send(channel).queue()
        }
    }

}