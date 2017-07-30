package de.arraying.arraybot.commands.command.custom

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.storage.CustomCommandDataStorage
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandPermission
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandSyntax
import de.arraying.arraybot.commands.command.custom.entities.CustomCommandTypes
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.iface.ICommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.Utils
import net.dv8tion.jda.core.exceptions.PermissionException

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
class CustomCommand(val id: Long,
                    override val name: String,
                    syntax: CustomCommandSyntax,
                    permission: CustomCommandPermission,
                    type: CustomCommandTypes,
                    description: String?,
                    value: String):
        ICommand {

    private val arraybot = Arraybot.instance

    /**
     * The custom command syntax, determines if arguments are required.
     */
    var syntax = syntax
        set(value) {
            field = value
            arraybot.managerSql.updateCommandsTable(id, name, "syntax", value.toString())
        }

    /**
     * The custom command permission. Can be either a action ID or permission.
     */
    var permission = permission
        set(value) {
            field = value
            arraybot.managerSql.updateCommandsTable(id, name, "permission", permission.value)
        }

    /**
     * The custom command type. Determines the action of the command.
     */
    var type = type
        set(value) {
            field = value
            arraybot.managerSql.updateCommandsTable(id, name, "type", type.toString())
        }

    /**
     * The custom command description.
     */
    var description = description
        set(value) {
            field = value
            arraybot.managerSql.updateCommandsTable(id, name, "description", description)
        }

    /**
     * The custom command value.
     */
    var value = value
        set(value) {
            field = value
            arraybot.managerSql.updateCommandsTable(id, name, "value", this.value)
        }

    /**
     * Invokes the custom command.
     */
    override suspend fun invoke(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val author = environment.author
        val storageId = environment.message.idLong
        arraybot.logger.info("${author.name}#${author.discriminator} executed the command (custom) \"${environment.message.rawContent}\" on shard ${Utils.getShard(environment.guild.idLong)}.")
        Cache.commandsRun++
        if(!CustomCommands.types.containsKey(type)) {
            Messages.CUSTOMCOMMAND_TYPE_INVALID.send(channel).queue()
            return
        }
        CustomCommands.storage.store(storageId, CustomCommandDataStorage())
        var value = this.value
        if(syntax == CustomCommandSyntax.UNKNOWN) {
            Messages.CUSTOMCOMMAND_SYNTAX_INVALID.send(channel).queue()
            return
        } else if(syntax == CustomCommandSyntax.STARTSWITH) {
            if(args.size < 2) {
                Messages.CUSTOMCOMMAND_SYNTAX_PROVIDE.send(channel).queue()
                return
            }
            val stringBuilder = StringBuilder()
            for(i in 1..args.size-1) {
                stringBuilder
                        .append(args[i])
                        .append(" ")
            }
            value = value.replace("{input}", stringBuilder.toString().trim())
        }
        if(!permission.hasPermission(environment.member)) {
            Messages.CUSTOMCOMMAND_PERMISSION.send(channel).queue()
            return
        }
        for(parameter in CustomCommands.parameters.values) {
            while(value.toLowerCase().contains(parameter.trigger)) {
                value = parameter.handleParameter(environment, value)
            }
        }
        val storage = CustomCommands.storage.retrieve(storageId)!!
        if(storage.delete) {
            try {
                environment.message.delete().queue()
            } catch(exception: PermissionException) {
                arraybot.logger.fatal("Could not delete custom command command message.")
            }
        }
        if(value.isEmpty()) {
            Messages.CUSTOMCOMMAND_EMPTY.send(channel).queue()
            return
        }
        val type = CustomCommands.types[type]!!
        type.invoke(environment, value)
        if(!storage.silent
                && this.type != CustomCommandTypes.MESSAGE) {
            channel.sendMessage(type.getMessage(channel)).queue()
        }
        CustomCommands.storage.prune(storageId)
        // TODO SEND MESSAGE AND CHECK IF ITS SILENT
    }

}