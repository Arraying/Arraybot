package de.arraying.arraybot.command.commands.developer.reload.subcommands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
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
class ReloadShardsSubCommand: SubCommand("shards",
        aliases = arrayOf("shards", "shard", "s")) {

    private val config = arraybot.configuration

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Message.COMMANDS_RELOAD_INPUT_PROVIDE.send(channel).queue()
            return
        }
        val toReload = args[2]
        val shards = ArrayList<Int>()
        val min: Int
        val max: Int
        if(config.botShards < 2) {
            min = Arraybot.SINGLE_SHARD_INDEX
            max = Arraybot.SINGLE_SHARD_INDEX
        } else {
            min = 0
            max = config.botShards-1
        }
        if(UDatatypes.isInt(toReload)) {
            val shard = toReload.toInt()
            if(shard < min
                    || shard > max) {
                Message.COMMANDS_RELOAD_SHARD_INVALID.send(channel, min.toString(), max.toString()).queue()
                return
            }
            shards.add(shard)
        } else {
            if(toReload.equals("all", true)) {
                shards.addAll(arraybot.botManager.shards.keys)
            } else {
                Message.COMMANDS_RELOAD_SHARD_INVALID.send(channel, min.toString(), max.toString()).queue()
                return
            }
        }
        Message.COMMANDS_RELOAD_SHARD_RELOADED.send(channel).queue()
        for(shard in shards) {
            val result = arraybot.botManager.restartShard(shard)
            if(!result) {
                Message.COMMANDS_RELOAD_SHARD_ERROR.send(channel).queue()
                return
            }
        }
    }

}