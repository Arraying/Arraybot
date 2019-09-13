package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message

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
class FilterRegexSubCommand: SubCommand("regex",
        aliases = arrayOf("r")) {

    /**
     * When the command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guildId = environment.guild.idLong
        val guildEntry = Category.GUILD.entry as GuildEntry
        val filter = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_REGEX), guildId, null)!!.toBoolean()
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.FILTER_REGEX), guildId, null, !filter)
        if(filter) {
            Message.COMMANDS_FILTER_REGEX_OFF.send(channel).queue()
        } else {
            Message.COMMANDS_FILTER_REGEX_ON.send(channel).queue()
        }
    }

}