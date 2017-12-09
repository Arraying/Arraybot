package de.arraying.arraybot.command.commands.moderation.lookup

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.punishment.PunishmentObject
import de.arraying.arraybot.util.UDatatypes
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
class LookupCommand: DefaultCommand("lookup",
        CommandCategory.MODERATION,
        Permission.KICK_MEMBERS) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Message.COMMANDS_LOOKUP_PROVIDE.send(channel).queue()
            return
        }
        val idRaw = args[1]
        if(!UDatatypes.isInt(idRaw)) {
            Message.COMMANDS_LOOKUP_INTEGER.send(channel).queue()
            return
        }
        val id = idRaw.toInt()
        val guild = environment.guild
        val guildId = guild.idLong
        val punishmentIds = Category.PUNISHMENT_IDS.entry as SetEntry
        if(!punishmentIds.contains(guildId, id)) {
            Message.COMMANDS_LOOKUP_EXISTS.send(channel).queue()
            return
        }
        val punishment = PunishmentObject.fromRedis(guild, id)
        val embed = arraybot.punishmentManager.getEmbed(guild, punishment, false, null)
        channel.sendMessage(embed.build()).queue()
    }

}