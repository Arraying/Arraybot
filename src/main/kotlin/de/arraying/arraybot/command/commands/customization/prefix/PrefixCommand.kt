package de.arraying.arraybot.command.commands.customization.prefix

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UArguments
import net.dv8tion.jda.core.Permission

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
class PrefixCommand: DefaultCommand("prefix",
        CommandCategory.CUSTOMIZATION,
        Permission.MANAGE_SERVER,
        aliases = arrayOf("trigger")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size < 2) {
            val prefix = entry.fetch(entry.getField(GuildEntry.Fields.PREFIX), guildId, null)
            Message.COMMANDS_PREFIX_PREFIX.send(channel, prefix).queue()
            return
        }
        val newPrefix = UArguments.combine(args.toTypedArray(), 1)
                .replace("{space}", " ")
        if(newPrefix.length > Limits.PREFIX.limit) {
            Message.COMMANDS_PREFIX_PREFIX_LENGTH.send(channel, Limits.PREFIX.limit.toString()).queue()
            return
        }
        entry.push(entry.getField(GuildEntry.Fields.PREFIX), guildId, null, newPrefix)
        Message.COMMANDS_PREFIX_UPDATED.send(channel).queue()
    }

}