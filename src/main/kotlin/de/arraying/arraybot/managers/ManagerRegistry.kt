package de.arraying.arraybot.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.commands.other.CommandCollection
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.commands.custom.CustomCommands
import de.arraying.arraybot.commands.commands.custom.collections.CustomCommandParameterCollection
import de.arraying.arraybot.commands.commands.custom.collections.CustomCommandTypeCollection

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

    private val logger = Arraybot.instance.logger

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

    //    /**
//     * Registers all commands.
//     */
//    fun registerAllCommands() {
//        val commands = ArrayList<DefaultCommand>()
//        Reflections("de.arraying.arraybot.commands.commands")
//                .getSubTypesOf(DefaultCommand::class.java)
//                .forEach {
//                    val command = it.newInstance()
//                    commands.add(command)
//                }
//        Reflections("de.arraying.arraybot.commands.commands")
//                .getSubTypesOf(PunishmentCommand::class.java)
//                .forEach {
//                    val command = it.newInstance()
//                    commands.add(command)
//                }
//        for(command in commands) {
//            Commands.commands.add(command)
//            Arraybot.instance.logger.info("Registered the command \"${command.name}\" and its aliases.")
//            command.checks()
//        }
//    }
//
//    /**
//     * Registers all subcommands.
//     */
//    fun registerAllSubCommands() {
//        Reflections("de.arraying.arraybot.commands.commands")
//                .getSubTypesOf(SubCommand::class.java)
//                .forEach {
//                    val subCommand = it.newInstance()
//                    val subCommandCommandName = subCommand.commandName.toLowerCase()
//                    if(Commands.commands.any {
//                        it.name == subCommandCommandName
//                    }) {
//                        Commands.commands.filter {
//                            it.name == subCommandCommandName
//                        } .forEach {
//                            it.subCommands.add(subCommand)
//                        }
//                    }
//                }
//    }
//
//    /**
//     * Registers all custom command types.
//     */
//    fun registerAllCustomCommandTypes() {
//        Reflections("de.arraying.arraybot.commands.commands")
//                .getSubTypesOf(CustomCommandType::class.java)
//                .forEach {
//                    val type = it.newInstance()
//                    CustomCommands.types.put(type.type, type)
//                }
//    }
//
//    /**
//     * Registers all custom command parameters.
//     */
//    fun registerAllCustomCommandParameters() {
//        Reflections("de.arraying.arraybot.commands.commands")
//                .getSubTypesOf(CustomCommandParameter::class.java)
//                .forEach {
//                    val parameter = it.newInstance()
//                    CustomCommands.parameters.put(parameter.trigger, parameter)
//                }
//    }

}