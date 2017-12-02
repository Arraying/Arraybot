package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.FilterBypassEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDatatypes

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
class FilterBypassRemoveSubCommand: SubCommand("bypassremove",
        aliases = arrayOf("removebypass", "rb")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val bypassesEntry = Category.FILTER_BYPASS_IDS.entry as SetEntry
        val bypassEntry = Category.FILTER_BYPASS.entry as FilterBypassEntry
        if(args.size < 3) {
            Message.COMMANDS_FILTER_BYPASS_PROVIDE_ID.send(channel).queue()
            return
        }
        val idRaw = args[2]
        if(!UDatatypes.isInt(idRaw)) {
            Message.COMMANDS_FILTER_BYPASS_ID_NUMBER.send(channel).queue()
            return
        }
        val id = idRaw.toInt()
        if(!bypassesEntry.contains(guildId, id)) {
            Message.COMMANDS_FILTER_BYPASS_ID_EXISTS.send(channel).queue()
            return
        }
        bypassEntry.deleteSingleEntry(guildId, id)
        Message.COMMANDS_FILTER_BYPASS_REMOVE.send(channel).queue()
    }

}