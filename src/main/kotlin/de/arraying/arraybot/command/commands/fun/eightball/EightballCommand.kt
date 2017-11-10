package de.arraying.arraybot.command.commands.`fun`.eightball

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import net.dv8tion.jda.core.Permission
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
class EightballCommand: DefaultCommand("eightball",
        CommandCategory.FUN,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("8ball", "magiceightball", "magic8ball")) {

    private val replies = arrayOf(Message.COMMANDS_EIGHTBALL_ANSWER_DEFINITELY,
            Message.COMMANDS_EIGHTBALL_ANSWER_MAYBE,
            Message.COMMANDS_EIGHTBALL_ANSWER_NO,
            Message.COMMANDS_EIGHTBALL_ANSWER_NOT,
            Message.COMMANDS_EIGHTBALL_ANSWER_YES)

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Message.COMMANDS_EIGHTBALL_PROVIDE.send(channel).queue()
            return
        }
        val message = replies[Random().nextInt(replies.size)]
        Message.COMMANDS_EIGHTBALL_EIGHTBALL.send(channel, message.getContent(channel)).queue()
    }

}