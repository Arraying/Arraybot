package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UArguments

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
class FilterRemoveSubCommand: SubCommand("remove",
        aliases = arrayOf("r")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val id = environment.guild.idLong
        val entry = Category.FILTER.entry as SetEntry
        if(args.size < 3) {
            Message.COMMANDS_FILTER_PHRASE_PROVIDE.send(channel).queue()
            return
        }
        val phrase = UArguments.combine(args.toTypedArray(), 2)
        entry.remove(id, phrase)
        Message.COMMANDS_FILTER_UPDATED.send(channel).queue()
    }

}