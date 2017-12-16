package de.arraying.arraybot.command.commands.utils.test

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import net.dv8tion.jda.core.Permission

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
class TestCommand: DefaultCommand("test",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE) {

    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            channel.sendMessage("mesage ffor fkcs sake!").queue()
            return
        }
        val url = args[1]
        try {
            arraybot.scriptManager.executeScript2(url, environment)
        } catch(exception: Exception) {
            channel.sendMessage("Error: ${exception.message}").queue()
        }
    }

}