package de.arraying.arraybot.command.commands.`fun`.dog

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.util.URequest
import net.dv8tion.jda.core.Permission
import java.net.URL

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
class DogCommand: DefaultCommand("dog",
        CommandCategory.FUN,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("woof")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val url = URequest.get("https://random.dog/woof.json").getString("url")
        channel.sendFile(URL(url).openStream(), "dog.png").queue()
    }

}