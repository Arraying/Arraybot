package de.arraying.arraybot.command.commands.utils

import de.arraying.arraybot.command.Commands
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.command.templates.CustomCommand
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.pagination.PageImpl
import de.arraying.arraybot.util.UEmbed
import net.dv8tion.jda.core.Permission
import org.apache.commons.collections4.list.TreeList

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
class TestCommand : DefaultCommand("test",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val commands = TreeList<Any>()
        commands.addAll(Commands.commands.values)
        commands.addAll(CustomCommand.getAll(environment.guild.idLong, channel))
        val pages = PageBuilder()
                .withType(PageBuilder.Type.COMMANDS)
                .withEmbed(UEmbed.getEmbed(channel))
                .withTitle(Message.EMBED_TITLE_COMMANDS.content(channel))
                .withTotal(5)
                .withEntries(commands.toTypedArray())
                .build()
        val page = if(args.size > 1) pages.getPageNumber(args[1]) else PageImpl.FIRST_PAGE
        channel.sendMessage(pages.getPage(page, channel).build()).queue()
    }

}