package de.arraying.arraybot.command.commands.customization.filter.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.filter.FilterBypass
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDatatypes
import de.arraying.arraybot.util.UEmbed
import de.arraying.arraybot.util.UFormatting

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
class FilterBypassInfoSubCommand: SubCommand("bypassinfo",
        aliases = arrayOf("infobypass", "bi")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val bypassesEntry = Category.FILTER_BYPASS_IDS.entry as SetEntry
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
        val bypass = FilterBypass.fromRedis(guild, id)
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_FILTER_BYPASS_INFO_DESCRIPTION.getContent(channel))
                .addField(Message.COMMANDS_FILTER_BYPASS_INFO_TYPE.getContent(channel),
                        UFormatting.displayableEnumField(bypass.type),
                        false)
                .addField(Message.COMMANDS_FILTER_BYPASS_INFO_VALUE.getContent(channel),
                        bypass.value.toString(),
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}