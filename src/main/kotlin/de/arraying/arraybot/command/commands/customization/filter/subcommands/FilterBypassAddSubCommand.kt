package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.FilterBypassEntry
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.filter.FilterBypassType
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UChannel
import de.arraying.arraybot.util.URole
import de.arraying.arraybot.util.UUser
import net.dv8tion.jda.api.entities.ISnowflake

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
class FilterBypassAddSubCommand: SubCommand("bypassadd",
        aliases = arrayOf("addbypass", "ba")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val guildEntry = Category.GUILD.entry as GuildEntry
        val bypassesEntry = Category.FILTER_BYPASS_IDS.entry as SetEntry
        val bypassEntry = Category.FILTER_BYPASS.entry as FilterBypassEntry
        if(args.size < 3) {
            Message.COMMANDS_FILTER_BYPASS_TYPE_PROVIDE.send(channel).queue()
            return
        }
        if(args.size < 4) {
            Message.COMMANDS_FILTER_BYPASS_PROVIDE_VALUE.send(channel).queue()
            return
        }
        val type = FilterBypassType.fromString(args[2])
        val valueRaw = args[3]
        val value: ISnowflake?
        when(type) {
            FilterBypassType.USER -> {
                val member = UUser.getMember(guild, valueRaw)
                value = member?.user
            }
            FilterBypassType.CHANNEL -> {
                value = UChannel.getTextChannel(guild, valueRaw)
            }
            FilterBypassType.ROLE -> {
                value = URole.getRole(guild, valueRaw)
            }
            else -> {
                Message.COMMANDS_FILTER_BYPASS_TYPE_INVALID.send(channel, FilterBypassType.getTypes()).queue()
                return
            }
        }
        if(value == null) {
            Message.COMMANDS_FILTER_BYPASS_VALUE_INVALID.send(channel).queue()
            return
        }
        val id = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.COUNT_BYPASS), guildId, null).toInt() + 1
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.COUNT_BYPASS), guildId, null, id)
        bypassesEntry.add(guildId, id)
        bypassEntry.createBypass(guildId, id, type, value.idLong)
        Message.COMMANDS_FILTER_BYPASS_ADD.send(channel, id.toString()).queue()
    }

}