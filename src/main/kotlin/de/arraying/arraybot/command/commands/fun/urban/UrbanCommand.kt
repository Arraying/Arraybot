package de.arraying.arraybot.command.commands.`fun`.urban

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UArguments
import de.arraying.arraybot.util.UEmbed
import de.arraying.arraybot.util.URequest
import net.dv8tion.jda.api.Permission

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
class UrbanCommand: DefaultCommand("urban",
        CommandCategory.FUN,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("urbandictionary", "ud")) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Message.COMMANDS_URBAN_PROVIDE.send(channel).queue()
            return
        }
        val phrase = UArguments.combine(args.toTypedArray(), 1)
                .replace(" ", "%20")
        var json = URequest.get("http://api.urbandictionary.com/v0/define?term=$phrase")
        val array = json.array("list")
        if(array.length() == 0) {
            Message.COMMANDS_URBAN_NONE.send(channel).queue()
            return
        }
        json = json.array("list").json(0)
        val embed = UEmbed.getEmbed(channel)
                .setDescription(Message.COMMANDS_URBAN_EMBED_DESCRIPTION.getContent(channel, "[$phrase](https://www.urbandictionary.com/define.php?term=$phrase)"))
                .addField(Message.COMMANDS_URBAN_EMBED_DEFINITION.getContent(channel),
                        json.string("definition"),
                        false)
        val example = json.string("example")
        if(example.isNotEmpty()) {
            embed.addField(Message.COMMANDS_URBAN_EMBED_EXAMPLE.getContent(channel),
                    example,
                    false)
        }
        channel.sendMessage(embed.build()).queue()
    }

}