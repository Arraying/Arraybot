package de.arraying.arraybot.command.templates

import de.arraying.arraybot.command.Command
import de.arraying.arraybot.command.custom.permission.CustomCommandPermission
import de.arraying.arraybot.command.custom.syntax.CustomCommandSyntax
import de.arraying.arraybot.command.custom.type.CustomCommandType
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDefaults
import net.dv8tion.jda.core.entities.TextChannel

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

    /**
     * Invokes the custom command.
     */
    override suspend fun invoke(environment: CommandEnvironment, args: List<String>) {
        //TODO Add custom command invocation.
    }

    companion object {

        fun fromRedis(name: String, syntaxString: String, permissionString: String, typeString: String,
                      descriptionString: String, value: String, channel: TextChannel): CustomCommand {
            val syntax = CustomCommandSyntax.fromString(syntaxString)
            val permission = CustomCommandPermission(permissionString)
            val type = CustomCommandType.fromString(typeString)
            val description = if(descriptionString == UDefaults.DEFAULT_NULL) {
                    Message.CUSTOM_DESCRIPTION.content(channel)
                } else {
                    descriptionString
                }
            return CustomCommand(name, syntax, permission, type, description, value)
        }

    }

}