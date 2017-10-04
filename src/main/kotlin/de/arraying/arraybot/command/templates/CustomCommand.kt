package de.arraying.arraybot.command.templates

import de.arraying.arraybot.command.Command
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.custom.permission.CustomCommandPermission
import de.arraying.arraybot.command.custom.syntax.CustomCommandSyntax
import de.arraying.arraybot.command.custom.type.CustomCommandType
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.data.database.categories.CustomCommandEntry
import de.arraying.arraybot.data.database.core.Entry
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDefaults
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.entities.TextChannel
import org.slf4j.LoggerFactory

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
class CustomCommand(val name: String,
                    val syntax: CustomCommandSyntax,
                    val permission: CustomCommandPermission,
                    val type: CustomCommandType,
                    val description: String,
                    val value: String): Command {

    private val logger = LoggerFactory.getLogger("Custom-Command")

    /**
     * Invokes the custom command.
     */
    override suspend fun invoke(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        launch(CommonPool) {
            logger.info("${environment.author.idLong} executed the custom command in the guild ${channel.guild.idLong}.")
            val resource = Redis.getInstance().resource
            resource.incr("commands")
        }
        if(type == CustomCommandType.UNKNOWN) {
            Message.CUSTOM_TYPE_INVALID.send(channel).queue()
            return
        }
        if(type.action == null) {
            Message.CUSTOM_TYPE_IMPLEMENTED.send(channel).queue()
            return
        }
        if(syntax == CustomCommandSyntax.UNKNOWN) {
            Message.CUSTOM_SYNTAX_INVALID.send(channel).queue()
            return
        }
        if(!permission.hasPermission(environment.member)) {
            Message.CUSTOM_PERMISSION.send(channel).queue()
            return
        }
        val value: String =
            if(syntax == CustomCommandSyntax.STARTS_WITH) {
                if(args.size < 2) {
                    Message.CUSTOM_ARGUMENT_PROVIDE.send(channel, name, Message.CUSTOM_ARGUMENT.getContent(channel)).queue()
                    return
                }
                val builder = StringBuilder()
                for(i in 1 until args.size) {
                    builder
                            .append(args[i])
                            .append(" ")
                }
                this.value.replace("{input}", builder.toString().trim())
            } else {
                this.value
            }
        if(value.isEmpty()) {
            Message.CUSTOM_VALUE_EMPTY.send(channel).queue()
            return
        }
        type.action.onAction(environment, value)
    }

    companion object {

        /**
         * Creates a custom command from Redis.
         */
        fun fromRedis(guildId: Long, commandName: String, channel: TextChannel): CustomCommand {
            val commandEntry = Entry.Category.CUSTOM_COMMAND.entry as CustomCommandEntry
            val syntax = CustomCommandSyntax.fromString(commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.SYNTAX), guildId, commandName))
            val permission = CustomCommandPermission(commandEntry.fetch(commandEntry.getField(CustomCommandEntry.Fields.PERMISSION), guildId, commandName))
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
            val commandList = Entry.Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
            val commandNames = commandList.values(guildId)
            val commands = ArrayList<CustomCommand>()
            commandNames.mapTo(commands) {
                CustomCommand.fromRedis(guildId, it, channel)
            }
            return commands.toTypedArray()
        }

    }

}