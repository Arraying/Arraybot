package de.arraying.arraybot.command.commands.developer.reload.subcommands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.language.Message
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
class ReloadAppSubCommand: SubCommand("app",
        aliases = arrayOf("application", "a")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        Runtime.getRuntime().addShutdownHook(RestartShutdownHook("Restart-Shutdown-Hook"))
        Message.COMMANDS_RELOAD_APP.send(channel).queue({
            System.exit(0)
        })
    }

    class RestartShutdownHook(name: String): Thread(name) {

        private val logger = LoggerFactory.getLogger(name)

        /**
         * When the thread is run.
         */
        override fun run() {
            logger.info("Attempting to reboot...")
            Runtime.getRuntime().exec(Arraybot.getInstance().configuration.startCommand)
        }

    }

}