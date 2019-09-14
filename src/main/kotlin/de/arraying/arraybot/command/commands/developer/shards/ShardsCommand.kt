package de.arraying.arraybot.command.commands.developer.shards

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.pagination.PageImpl
import de.arraying.arraybot.util.UEmbed
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
class ShardsCommand: DefaultCommand("shards",
        CommandCategory.DEVELOPER,
        Permission.MESSAGE_WRITE) {

    /**
     * When he command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_SHARDS_EMBED_TITLE.getContent(channel))
        val entries = ArrayList<String>()
        for(shard in Arraybot.INSTANCE.botManager.shardManager.shards) {
            entries.add("Shard #${shard.shardInfo.shardId} ${shard.status} [" +
                    "${shard.guilds.size}g, " +
                    "${shard.users.size}u, " +
                    "${shard.textChannels.size + shard.voiceChannels.size}c, " +
                    "${shard.restPing.complete()}ms]")
        }
        val pages = PageBuilder()
                .withType(PageBuilder.Type.LIST)
                .withEmbed(embed)
                .withTitle(Message.COMMANDS_SHARDS_EMBED_TITLE.getContent(channel))
                .withTotal(15)
                .withEntries(entries.toTypedArray())
                .build()
        var pageNumber = PageImpl.FIRST_PAGE
        if(args.size > 1) {
            pageNumber = pages.getPageNumber(args[1])
        }
        channel.sendMessage(pages.getPage(pageNumber, channel).build()).queue()
    }

}