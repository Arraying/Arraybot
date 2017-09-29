package de.arraying.arraybot.command.commands.utils

import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.util.UEmbed
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
class FibonacciCommand : DefaultCommand("fibonacci",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("fib")) {

    private val fibonacci = arrayOfNulls<Long>(51)

    /**
     * Sets up the fibonacci sequence.
     */
    init {
        println("init'd")
        for(i in (0..50)) {
            fibonacci[i] = i.toLong()
        }
    }

    /**
     * Executes the command.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_FIBONACCI_EMBED_DESCRIPTION.content(channel))
        val page = PageBuilder()
                .withType(PageBuilder.Type.LIST)
                .withEmbed(embed)
                .withTitle(Message.COMMANDS_FIBONACCI_EMBED_TITLE.content(channel))
                .withTotal(5)
                .withEntries(fibonacci)
                .build()
        channel.sendMessage(page.getPage(1, channel).build()).queue()
    }

    /**
     * Gets the fibonacci sequence.
     */
    private fun fibonacci(n: Int): Long {
        println("got $n")
        return if (n <= 1)
            n.toLong()
        else
            fibonacci(n - 1) + fibonacci(n - 2)
    }

}