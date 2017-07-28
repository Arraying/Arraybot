package de.arraying.arraybot.commands.commands.developer.restart

import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.entities.DefaultCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.scheduler.Scheduler
import de.arraying.arraybot.scheduler.SchedulerTask
import de.arraying.arraybot.utils.Utils
import kotlinx.coroutines.experimental.runBlocking
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
class CommandRestart:
        DefaultCommand("restart",
                CommandCategory.DEVELOPER,
                Permission.MESSAGE_WRITE) {

    /**
     * When the command is executed.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size != 2) {
            sendSyntax(channel)
            return
        }
        if(!Utils.isInt(args[1])) {
            Messages.COMMAND_RESTART_INT.send(channel).queue()
            return
        }
        val shard = Integer.valueOf(args[1])
        if(!arraybot.managerBot.shards.containsKey(shard)) {
            Messages.COMMAND_RESTART_EXISTS.send(channel).queue()
            return
        }
        Messages.COMMAND_RESTART_RESTARTING.send(channel).queue()
        runBlocking {
            if(!arraybot.managerBot.restartShard(shard)) {
                Messages.COMMAND_RESTART_ERROR.send(channel).queue()
            }
        }
        val task = object: SchedulerTask(1000, 666, true) {
            override fun onTask() {
                Messages.COMMAND_RESTART_RESTARTED.send(arraybot.managerBot.shards[Utils.getShard(channel.guild.idLong).toInt()]
                    !!.getTextChannelById(channel.idLong)).queue()
            }
        }
        Scheduler.schedule(task)
    }

}