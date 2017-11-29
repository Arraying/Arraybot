package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UArguments
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
class FilterMessageSubCommand: SubCommand("message",
        aliases = arrayOf("m", "msg")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guildId = environment.guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size > 2) {
            val message = UArguments.combine(args.toTypedArray(), 2)
            if(message.length > Limits.FILTER_MESSAGE.limit) {
                Message.COMMANDS_FILTER_MESSAGE_LENGTH.send(channel, Limits.FILTER_MESSAGE.limit.toString())
                return
            }
            entry.push(entry.getField(GuildEntry.Fields.FILTER_MESSAGE), guildId, null, message)
            Message.COMMANDS_FILTER_UPDATED.send(channel).queue()
            return
        }
        val message = entry.fetch(entry.getField(GuildEntry.Fields.FILTER_MESSAGE), guildId, null)
        if(message == UDefaults.DEFAULT_NULL) {
            Message.COMMANDS_FILTER_MESSAGE_NONE.send(channel).queue()
            return
        }
        Message.COMMANDS_FILTER_MESSAGE.send(channel, message).queue()
    }

}