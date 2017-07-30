package de.arraying.arraybot.commands.command.utils.commands.subcommands

import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ICommand
import de.arraying.arraybot.core.iface.ISubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.misc.Pages
import de.arraying.arraybot.utils.Utils

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
class SubCommandCommandsCategory: 
        ISubCommand {

    override val subCommandName = "category"
    private val categories: String

    /**
     * Initializes the categories.
     */
    init {
        val categoryBuilder = StringBuilder()
        for(category in DefaultCommand.CommandCategory.values()) {
            categoryBuilder
                    .append("- ")
                    .append(Utils.setFirstUppercase(category))
                    .append("\n")
        }
        categories = categoryBuilder.toString()
    }

    /**
     * Invokes the sub command.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val member = environment.member
        val channel = environment.channel
        if(args.size < 3) {
            val embed = Utils.getEmbed(channel)
                    .setDescription(Messages.COMMAND_COMMANDS_CATEGORY_DESCRIPTION.content(channel))
                    .addField(Messages.COMMAND_COMMANDS_CATEGORY_CATEGORIES.content(channel),
                            categories,
                            false)
            channel.sendMessage(embed.build()).queue()
            return
        }
        val categoryName = args[2].toUpperCase()
        val category = try {
            DefaultCommand.CommandCategory.valueOf(categoryName)
        } catch(exception: Exception) {
            Messages.COMMAND_COMMANDS_UNKNOWN_CATEGORY.send(channel).queue()
            return
        }
        val commands = ArrayList<ICommand>()
        Commands.getCommandList(member, channel)
                .filter {
                    it is DefaultCommand
                    && it.category == category
                }
                .forEach {
                    commands.add(it)
                }
        if(commands.isEmpty()) {
            Messages.COMMAND_COMMANDS_EMPTY.send(channel).queue()
            return
        }
        val embed = Utils.getEmbed(channel)
                .setDescription(Messages.COMMAND_COMMANDS_CATEGORY_LIST_DESCRIPTION.content(channel))
        val pages = Pages(embed,
                Messages.COMMAND_COMMANDS_CATEGORY_LIST_COMMANDS.content(channel),
                commands.toTypedArray(),
                7)
        if(args.size > 3
                && Utils.isInt(args[3])
                && args[3].toInt() > 0
                && args[3].toInt() <= pages.getTotal()) {
            channel.sendMessage(pages.getPage(args[3].toInt(), channel).build()).queue()
            return
        }
        channel.sendMessage(pages.getPage(channel = channel).build()).queue()
    }

}