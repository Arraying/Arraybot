package de.arraying.arraybot.commands.command.utils.uptime

import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.language.Messages
import net.dv8tion.jda.core.Permission
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
class CommandUptime:
        DefaultCommand("uptime",
                CommandCategory.UTILS,
                Permission.MESSAGE_WRITE) {

    /**
     * Invokes the uptime command. Shows how long the bot has been up.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val runtimeMXBeam = ManagementFactory.getRuntimeMXBean()
        val difference = runtimeMXBeam.uptime
        val differenceSeconds = difference / 1000 % 60
        val differenceMinutes = difference / (60 * 1000) % 60
        val differenceHours = difference / (60 * 60 * 1000) % 24
        val differenceDays = difference / (24 * 60 * 60 * 1000)
        channel.sendMessage(Messages.COMMAND_UPTIME_UPTIME.content(channel)
                .replace("{days}", differenceDays.toString())
                .replace("{hours}", differenceHours.toString())
                .replace("{minutes}", differenceMinutes.toString())
                .replace("{seconds}", differenceSeconds.toString())).queue()
    }

}