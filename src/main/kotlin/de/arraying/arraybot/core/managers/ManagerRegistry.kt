package de.arraying.arraybot.core.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.command.custom.CustomCommands
import de.arraying.arraybot.commands.command.custom.collections.CustomCommandParameterCollection
import de.arraying.arraybot.commands.command.custom.collections.CustomCommandTypeCollection
import de.arraying.arraybot.commands.other.CommandCollection

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
class ManagerRegistry {

    private val arraybot = Arraybot.instance
    private val logger = arraybot.logger

    /**
     * Registers all commands.
     */
    fun registerCommands() {
        for(commandEntry in CommandCollection.values()) {
            val command = commandEntry.command
            Commands.commands.add(command)
            logger.info("Registered the command \"${command.name}\" and its aliases.")
            command.checks()
        }
    }

    /**
     * Registers all custom command types.
     */
    fun registerCustomCommandTypes() {
        for(typeEntry in CustomCommandTypeCollection.values()) {
            val type = typeEntry.type
            CustomCommands.types.put(typeEntry.types, type)
            logger.info("Registered the custom command type $typeEntry.")
        }
    }

    /**
     * Registers all custom command parameters.
     */
    fun registerCustomCommandParameters() {
        for(parameterEntry in CustomCommandParameterCollection.values()) {
            val parameter = parameterEntry.parameter
            CustomCommands.parameters.put(parameter.trigger, parameter)
            logger.info("Registered the custom command parameter $parameterEntry.")
        }
    }

}