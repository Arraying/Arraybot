package de.arraying.arraybot.command.commands.utils.stats

import com.sun.management.OperatingSystemMXBean
import de.arraying.arraybot.command.CommandCollection
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDatabase
import de.arraying.arraybot.util.UEmbed
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.Permission
import org.apache.commons.io.FileUtils
import java.lang.management.ManagementFactory

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
class StatsCommand: DefaultCommand("stats",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("statistics", "info", "information")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val resource = Redis.INSTANCE.resource
        val shards = arraybot.botManager.shardManager.shards
        val jda = environment.guild.jda
        val guilds = shards.sumBy {
            it.guilds.size
        }.toString()
        val guildsShard = jda.guilds.size.toString()
        val users = shards.sumBy {
            it.users.size
        }.toString()
        val usersShard = jda.users.size.toString()
        val channels = shards.sumBy {
            it.textChannels.size + it.voiceChannels.size
        }.toString()
        val channelsShard = (jda.textChannels.size + jda.voiceChannels.size).toString()
        val osMxBeam = ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean
        val cpu = osMxBeam.systemCpuLoad * 100
        val memoryUsage = Runtime.getRuntime().freeMemory()
        val ram = FileUtils.byteCountToDisplaySize(memoryUsage)
        val threads = Thread.activeCount().toString()
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_STATS_EMBED_DESCRIPTION.getContent(channel))
                .addField(Message.COMMANDS_STATS_EMBED_VERSION.getContent(channel),
                        arraybot.configuration.botVersion,
                        true)
                .addBlankField(true)
                .addField(Message.COMMANDS_STATS_EMBED_JDA.getContent(channel),
                        JDAInfo.VERSION,
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_GUILDS_TITLE.getContent(channel),
                        Message.COMMANDS_STATS_EMBED_GUILDS_VALUE.getContent(channel, guilds, guildsShard),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_USERS_TITLE.getContent(channel),
                        Message.COMMANDS_STATS_EMBED_USERS_VALUE.getContent(channel, users, usersShard),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_CHANNELS_TITLE.getContent(channel),
                        Message.COMMANDS_STATS_EMBED_CHANNELS_VALUE.getContent(channel, channels, channelsShard),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_COMMANDS_REGISTERED.getContent(channel),
                        CommandCollection.values().size.toString(),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_COMMANDS_RUN.getContent(channel),
                        resource.get(UDatabase.COMMANDS_KEY).toString(),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_MESSAGES.getContent(channel),
                        resource.get(UDatabase.MESSAGES_KEY).toString(),
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_CPU.getContent(channel),
                        "$cpu%",
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_RAM.getContent(channel),
                        ram,
                        true)
                .addField(Message.COMMANDS_STATS_EMBED_THREADS.getContent(channel),
                        threads,
                        true)
        channel.sendMessage(embed.build()).queue()
    }

}