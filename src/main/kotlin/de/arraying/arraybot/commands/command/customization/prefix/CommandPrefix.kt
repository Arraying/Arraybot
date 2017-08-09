package de.arraying.arraybot.commands.command.customization.prefix

import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.language.Messages
import de.arraying.arraybot.utils.ULimit
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
class CommandPrefix:
        DefaultCommand("prefix",
                CommandCategory.CUSTOMIZATION,
                Permission.MANAGE_SERVER) {

    /**
     * Invokes the prefix command which shows or sets the prefix.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val cache = environment.cache
        if(args.size > 1) {
            val stringBuilder = StringBuilder()
            for(i in 1..args.size-1) {
                stringBuilder
                        .append(args[i])
                        .append(" ")
            }
            val prefix = stringBuilder.toString()
                    .trim()
                    .replace("{space}", " ")
                    .replace("{empty}", "")
            if(prefix.length > ULimit.PREFIX.maxLength)  {
                channel.sendMessage(Messages.COMMAND_PREFIX_LENGTH.content(channel)
                        .replace("{max}", ULimit.PREFIX.maxLength.toString())).queue()
                return
            }
            cache!!.prefix = prefix
            channel.sendMessage(Messages.COMMAND_PREFIX_UPDATED.content(channel)
                    .replace("{prefix}", prefix)).queue()
            return
        }
        val prefix = cache!!.prefix
        channel.sendMessage(Messages.COMMAND_PREFIX_CURRENT.content(channel)
                .replace("{prefix}", prefix)).queue()
    }

}