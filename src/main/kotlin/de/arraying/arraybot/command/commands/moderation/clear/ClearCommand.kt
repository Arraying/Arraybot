package de.arraying.arraybot.command.commands.moderation.clear

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.commands.moderation.clear.parameters.BotsClearParameter
import de.arraying.arraybot.command.commands.moderation.clear.parameters.ChannelClearParameter
import de.arraying.arraybot.command.commands.moderation.clear.parameters.UserClearParameter
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UArguments
import de.arraying.arraybot.util.UDatatypes
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.utils.PermissionUtil
import java.time.OffsetDateTime


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
class ClearCommand: DefaultCommand("clear",
        CommandCategory.MODERATION,
        Permission.MESSAGE_MANAGE,
        aliases = arrayOf("purge", "pwn")) {

    private val parameters = arrayOf(BotsClearParameter(), ChannelClearParameter(), UserClearParameter())

    /**
     * When the command is executed.
     */
    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun onCommand(environment: CommandEnvironment, arguments: List<String>) {
        val channel = environment.channel
        if(!PermissionUtil.checkPermission(channel, channel.guild.selfMember, Permission.MESSAGE_MANAGE)) {
            Message.COMMANDS_CLEAR_PERMISSION.send(channel).queue()
            return
        }
        val storageId = environment.message.idLong
        arraybot.storageManager.clearCommandStorageDataStorage.create(storageId)
        var input = UArguments.combine(arguments.toTypedArray(), 0).toLowerCase()
        for(parameter in parameters) {
            while(input.contains(parameter.trigger)) {
                input = parameter.parse(environment, input)
            }
        }
        val args = input.split(" ")
        if(args.size < 2) {
            Message.COMMANDS_CLEAR_PROVIDE.send(channel).queue()
            return
        }
        val amountRaw = args[1]
        if(!UDatatypes.isInt(amountRaw)) {
            Message.COMMANDS_CLEAR_INTEGER.send(channel).queue()
            return
        }
        val amount = amountRaw.toInt()
        if(amount < 2
                || amount > 50) {
            Message.COMMANDS_CLEAR_BOUND.send(channel).queue()
            return
        }
        val storage = arraybot.storageManager.clearCommandStorageDataStorage.get(storageId)
        val channelDelete = storage.channel?: channel
        val messageHistory = channelDelete.history
        messageHistory.retrievePast(amount + 1).queue({
            val target = ArrayList<net.dv8tion.jda.core.entities.Message>()
            if(storage.isBots) {
                it.filterTo(target) { msg -> msg.author.isBot }
            }
            if(storage.users.isNotEmpty()) {
                it.filterTo(target) { msg -> storage.users.contains(msg.author.idLong) }
            } else {
                if(!storage.isBots) {
                    target.addAll(it)
                }
            }
            target.removeIf({
                msg -> msg.creationTime.isBefore(OffsetDateTime.now().minusWeeks(2))
            })
            if(target.size < 2) {
                Message.COMMANDS_CLEAR_LITTLE.send(channel).queue()
                return@queue
            }
            channelDelete.deleteMessages(target).queue({
                Message.COMMANDS_CLEAR_CLEAR.send(channel, target.size.toString()).queue()
                arraybot.storageManager.clearCommandStorageDataStorage.remove(storageId)
            })
        })
    }

}