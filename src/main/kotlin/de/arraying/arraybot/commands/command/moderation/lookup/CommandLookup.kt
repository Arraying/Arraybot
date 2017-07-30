package de.arraying.arraybot.commands.command.moderation.lookup

import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.Utils
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
class CommandLookup:
        DefaultCommand("lookup",
                CommandCategory.MODERATION,
                Permission.KICK_MEMBERS){

    /**
     * Invokes the default command which looks up the punishment.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Messages.COMMAND_LOOKUP_PROVIDE.send(channel).queue()
            return
        }
        if(!Utils.isLong(args[1])) {
            Messages.COMMAND_LOOKUP_INVALID.send(channel).queue()
            return
        }
        val punishmentId = args[1].toLong()
        val cache = environment.cache!!
        if(!cache.punishments.containsKey(punishmentId)) {
            Messages.COMMAND_LOOKUP_EXISTS.send(channel).queue()
            return
        }
        channel.sendMessage(arraybot.managerPunish.getPunishmentEmbed(environment.guild, punishmentId)?.build()).queue()
    }

}