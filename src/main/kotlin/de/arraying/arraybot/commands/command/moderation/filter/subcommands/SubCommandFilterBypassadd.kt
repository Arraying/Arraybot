package de.arraying.arraybot.commands.command.moderation.filter.subcommands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.entities.CBypass
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.iface.ISubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.UInput

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
class SubCommandFilterBypassadd:
        ISubCommand {

    override val subCommandName = "bypassadd"
    private val arraybot = Arraybot.instance

    /**
     * Gets the aliases.
     */
    override fun getAliases(): Array<String> {
        return arrayOf("addbypass")
    }

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val guild = environment.guild
        if(args.size < 3) {
            Messages.COMMAND_FILTER_BYPASSADD_PROVIDETYPE.send(channel).queue()
            return
        }
        if(args.size < 4) {
            Messages.COMMAND_FILTER_BYPASSADD_PROVIDEID.send(channel).queue()
            return
        }
        val type = CBypass.BypassType.getBypassType(args[2].toLowerCase())
        if(type == CBypass.BypassType.UNKNOWN) {
            Messages.COMMAND_FILTER_BYPASSADD_INVALIDTYPE.send(channel).queue()
            return
        }
        val target = args[3]
        if(!UInput.isValid(UInput.InputType.ALL, target, true)) {
            Messages.COMMAND_FILTER_BYPASSADD_INVALIDID.send(channel).queue()
            return
        }
        val value = UInput.retrieve(target)
        if((type == CBypass.BypassType.USER
                && guild.getMemberById(value) == null)
                || (type == CBypass.BypassType.CHANNEL
                && guild.getTextChannelById(value) == null)
                || (type == CBypass.BypassType.ROLE
                && guild.getRoleById(value) == null)) {
            Messages.COMMAND_FILTER_BYPASSADD_INVALIDID.send(channel).queue()
            return
        }
        val mod = environment.cache!!.mod!!
        if(mod.filterBypasses.values.any {
            it.bypassType == type
            && it.value == value
        }) {
            Messages.COMMAND_FILTER_BYPASSADD_EXISTS.send(channel).queue()
            return
        }
        val bypassId = ++mod.bypassCount
        arraybot.managerSql.addChatFilterBypass(guild.idLong, bypassId, type.toString(), value)
        channel.sendMessage(Messages.COMMAND_FILTER_BYPASSADD_UPDATE.content(channel)
                .replace("{id}", bypassId.toString())).queue()
    }

}