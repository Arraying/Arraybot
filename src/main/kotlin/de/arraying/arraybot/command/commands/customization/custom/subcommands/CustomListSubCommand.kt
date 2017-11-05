package de.arraying.arraybot.command.commands.customization.custom.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.CustomCommand
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.pagination.PageBuilder
import de.arraying.arraybot.pagination.PageImpl
import de.arraying.arraybot.util.UEmbed

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
class CustomListSubCommand: SubCommand("list",
        aliases = arrayOf("l", "showmewhatyougot")) {

    /**
     * When the command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val commands = CustomCommand.getAll(environment.guild.idLong, channel)
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_CUSTOM_LIST.getContent(channel))
        val pages = PageBuilder()
                .withEntries(commands)
                .withTotal(5)
                .withType(PageBuilder.Type.COMMANDS)
                .withTitle(Message.EMBED_TITLE_COMMANDS.getContent(channel))
                .withEmbed(embed)
                .build()
        var page = PageImpl.FIRST_PAGE
        if(args.size > 2) {
            page = pages.getPageNumber(args[2])
        }
        channel.sendMessage(pages.getPage(page, channel).build()).queue()
    }

}