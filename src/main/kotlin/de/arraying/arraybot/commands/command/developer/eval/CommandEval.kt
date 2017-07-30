package de.arraying.arraybot.commands.command.developer.eval

import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.iface.ISubCommand
import de.arraying.arraybot.language.Messages
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
class CommandEval(override val subCommands: Array<ISubCommand>):
        DefaultCommand("eval",
                CommandCategory.DEVELOPER,
                Permission.MESSAGE_WRITE,
                subCommands,
                aliases = arrayOf("evaluate", "exec", "execute")) {

    /**
     * When the command is executed.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val stringBuilder = StringBuilder(Messages.COMMAND_EVAL_INFO.content(channel))
        for(mode in EvalModes.values()) {
            stringBuilder.append(mode.name.toLowerCase())
                    .append(", ")
        }
        stringBuilder.setCharAt(stringBuilder.length-2, '.')
        channel.sendMessage(stringBuilder.toString().trim()).queue()
    }

    enum class EvalModes {

        KOTLIN,
        JAVASCRIPT

    }

}