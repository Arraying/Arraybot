package de.arraying.arraybot.commands.command.moderation.filter.subcommands

import de.arraying.arraybot.cache.entities.CBypass
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ISubCommand
import de.arraying.arraybot.core.language.Messages
import de.arraying.arraybot.utils.Utils

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
class SubCommandFilterBypassInfo:
        ISubCommand {

    /**
     * Gets the name.
     */
    override fun getName(): String {
        return "bypassinfo"
    }

    /**
     * Gets the aliases.
     */
    override fun getAliases(): Array<String> {
        return arrayOf("infobypass")
    }

    /**
     * When the subcommand is invoked.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Messages.COMMAND_FILTER_BYPASSINFO_PROVIDE.send(channel).queue()
            return
        }
        if(!Utils.isLong(args[2])) {
            Messages.COMMAND_FILTER_BYPASSINFO_INVALID.send(channel).queue()
            return
        }
        val bypassId = args[2].toLong()
        val mod = environment.cache!!.mod!!
        if(!mod.filterBypasses.containsKey(bypassId)) {
            Messages.COMMAND_FILTER_BYPASSINFO_EXISTS.send(channel).queue()
            return
        }
        val bypass = mod.filterBypasses[bypassId]!!
        val bypassValue = when(bypass.bypassType) {
            CBypass.BypassType.USER -> "<@${bypass.value}>"
            CBypass.BypassType.CHANNEL -> "<#${bypass.value}>"
            CBypass.BypassType.ROLE -> "<@&${bypass.value}>"
            else -> Messages.MISC_ERROR.content(channel)
        }
        val embed = Utils.getEmbed(channel)
                .setTitle(Messages.COMMAND_FILTER_BYPASSINFO_TITLE.content(channel)
                        .replace("{id}", bypassId.toString()))
                .setDescription(Messages.COMMAND_FILTER_BYPASSINFO_DESCRIPTION.content(channel))
                .addField(Messages.COMMAND_FILTER_BYPASSINFO_TYPE.content(channel),
                        Utils.setFirstUppercase(bypass.bypassType),
                        false)
                .addField(Messages.COMMAND_FILTER_BYPASSINFO_VALUE.content(channel),
                        bypassValue,
                        false)
        channel.sendMessage(embed.build()).queue()
    }

}