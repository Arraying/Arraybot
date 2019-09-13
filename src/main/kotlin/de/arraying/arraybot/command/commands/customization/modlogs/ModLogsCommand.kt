package de.arraying.arraybot.command.commands.customization.modlogs

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UChannel
import de.arraying.arraybot.util.UDefaults
import net.dv8tion.jda.api.Permission

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
class ModLogsCommand: DefaultCommand("modlogs",
        CommandCategory.CUSTOMIZATION,
        Permission.MANAGE_SERVER,
        aliases = arrayOf("moderatorlogs", "modchannel", "modchan")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val entry = Category.GUILD.entry as GuildEntry
        if(args.size < 2) {
            val channelId = entry.fetch(entry.getField(GuildEntry.Fields.PUNISHMENT_CHANNEL), guildId, null).toLong()
            if(channelId == UDefaults.DEFAULT_SNOWFLAKE.toLong()) {
                Message.COMMANDS_MODLOGS_CHANNEL_NONE.send(channel).queue()
                return
            }
            val chan = guild.getTextChannelById(channelId)
            if(chan == null) {
                Message.COMMANDS_MODLOGS_CHANNEL_INVALID.send(channel).queue()
                return
            }
            Message.COMMANDS_MODLOGS_CHANNEL.send(channel, chan.asMention).queue()
            return
        }
        val input = args[1]
        val chan: Long
        chan = if(input == "none") {
                UDefaults.DEFAULT_SNOWFLAKE.toLong()
            } else {
                val c = UChannel.getTextChannel(guild, input)
                if(c == null) {
                    Message.CHANNEL_INVALID.send(channel).queue()
                    return
                }
                c.idLong
            }
        entry.push(entry.getField(GuildEntry.Fields.PUNISHMENT_CHANNEL), guildId, null, chan)
        Message.COMMANDS_MODLOGS_UPDATED.send(channel).queue()
    }

}