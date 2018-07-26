package de.arraying.arraybot.command.templates

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.Command
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.custom.parameters.DeleteParameter
import de.arraying.arraybot.command.custom.parameters.RandomParameter
import de.arraying.arraybot.command.custom.parameters.SilentParameter
import de.arraying.arraybot.command.custom.parameters.ToidParameter
import de.arraying.arraybot.command.custom.syntax.CustomCommandSyntax
import de.arraying.arraybot.command.custom.type.CustomCommandType
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.data.database.categories.CustomCommandEntry
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.entities.TextChannel
import org.slf4j.LoggerFactory
import java.util.*

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
class CustomCommand(override val name: String,
                    val syntax: CustomCommandSyntax,
                    val permission: CustomPermission,
                    val type: CustomCommandType,
                    val description: String,
                    val value: String): Command {

    private val logger = LoggerFactory.getLogger("Custom-Command")
    private val parameters = arrayOf(DeleteParameter(), RandomParameter(), SilentParameter(), ToidParameter())
    private val random = Random()

    /**
     * Invokes the custom command.
     */
    override suspend fun invoke(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        launch(CommonPool) {
            logger.info("${environment.author.idLong} executed the custom command in the guild ${channel.guild.idLong}.")
            val resource = Redis.INSTANCE.resource
            resource.incr(UDatabase.COMMANDS_KEY)
        }
        if(type == CustomCommandType.UNKNOWN) {
            Message.CUSTOM_TYPE_INVALID.send(channel, CustomCommandType.getTypes()).queue()
            return
        }
        if(type.action == null) {
            Message.CUSTOM_TYPE_IMPLEMENTED.send(channel).queue()
            return
        }
        if(syntax == CustomCommandSyntax.UNKNOWN) {
            Message.CUSTOM_SYNTAX_INVALID.send(channel, CustomCommandSyntax.getSyntaxes()).queue()
            return
        }
        if(!permission.hasPermission(environment.member, channel)) {
            Message.CUSTOM_PERMISSION.send(channel).queue()
            return
        }
        val uid = environment.message.idLong
        val storageManager = Arraybot.INSTANCE.storageManager
        storageManager.customCommandStorageDataStorage.create(uid)
        var value: String =
            if(syntax == CustomCommandSyntax.STARTSWITH) {
                var localValue = this.value
                if(args.size < 2) {
                    Message.CUSTOM_ARGUMENT_PROVIDE.send(channel, name, Message.CUSTOM_ARGUMENT.getContent(channel)).queue()
                    return
                }
                val input = UArguments.combine(args.toTypedArray(), 1)
                val inputSlot = input.split(" ")
                for(i in (1..inputSlot.size)) {
                    localValue = localValue.replace("{argument_$i}", inputSlot[i-1])
                }
                localValue.replace("{input}", input)
            } else {
                this.value
            }
        for(parameter in parameters) {
            while(value.contains(parameter.trigger, true)) {
                value = parameter.parse(environment, value)
                        .replace(parameter.trigger, "")
            }
        }
        val storage = storageManager.customCommandStorageDataStorage.get(uid)
        if(storage.isRandom) {
            val parts = value.split(";".toRegex())
            value = parts[random.nextInt(parts.size)]
        }
        value = value.trim()
        value = UPlaceholder.replaceCore(environment.member, value)
        value = UPlaceholder.replaceChannel(environment.channel, value)
        value = UPlaceholder.replaceMessage(environment.message, value)
        if(value.isEmpty()) {
            Message.CUSTOM_VALUE_EMPTY.send(channel).queue()
            return
        } else if(value.length > Limits.MESSAGE.limit) {
            Message.CUSTOM_VALUE_LENGTH.send(channel, Limits.MESSAGE.limit.toString()).queue()
            return
        }
        val success = type.action.onAction(environment, value)
        if(success) {
            if(storage.isDelete) {
                try {
                    environment.message.delete().queue()
                } catch(ignored: Exception) {}
            }
            val message = type.action.getMessage(channel)
            if(!storage.isSilent
                    && message.isNotEmpty()) {
                channel.sendMessage(message).queue()
            }
        }
        storageManager.customCommandStorageDataStorage.remove(uid)
    }

    /**
     * Gets the syntax.
     */
    fun getSyntax(channel: TextChannel): String {
        val entry = Category.GUILD.entry as GuildEntry
        val prefix = entry.fetch(entry.getField(GuildEntry.Fields.PREFIX), channel.guild.idLong, null)
        return prefix + name + " " + if(syntax == CustomCommandSyntax.STARTSWITH) Message.CUSTOM_ARGUMENT.getContent(channel) else ""
    }

    companion object {

        /**
         * Creates a custom command from Redis.
         */
        fun fromRedis(guildId: Long, commandName: String, channel: TextChannel): CustomCommand {
            val commandEntry = Category.CUSTOM_COMMAND.entry as CustomCommandEntry
            val syntax = CustomCommandSyntax.fromString(commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.SYNTAX), guildId, commandName))
            val permission = CustomPermission(commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.PERMISSION), guildId, commandName))
            val type = CustomCommandType.fromString(commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.TYPE), guildId, commandName))
            val descriptionString = commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.DESCRIPTION), guildId, commandName)
            val description = if(descriptionString == UDefaults.DEFAULT_NULL) {
                    Message.CUSTOM_DESCRIPTION.getContent(channel)
                } else {
                    descriptionString
                }
            val value = commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.VALUE), guildId, commandName)
            return CustomCommand(commandName, syntax, permission, type, description, value)
        }

        /**
         * Gets all custom commands for the guild.
         */
        fun getAll(guildId: Long, channel: TextChannel): Array<CustomCommand> {
            val commandList = Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
            val commandNames = commandList.values(guildId)
            val commands = ArrayList<CustomCommand>()
            commandNames.mapTo(commands) {
                CustomCommand.fromRedis(guildId, it, channel)
            }
            return commands.toTypedArray()
        }

    }

}