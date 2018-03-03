package de.arraying.arraybot.command.commands.developer.reload.subcommands

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
        val min = "0"
        val max = (config.botShards - 1).toString()
        if(UDatatypes.isInt(toReload)) {
            val shard = toReload.toInt()
            try {
                arraybot.botManager.shardManager.restart(shard)
            } catch(exception: IllegalArgumentException) {
                Message.COMMANDS_RELOAD_SHARD_INVALID.send(channel, min, max).queue()
            }
        } else {
            if(toReload.equals("all", true)) {
                arraybot.botManager.shardManager.restart()
            } else {
                Message.COMMANDS_RELOAD_SHARD_INVALID.send(channel, min, max).queue()
                return
            }
        }
        Message.COMMANDS_RELOAD_SHARD_RELOADED.send(channel).queue()
    }

}