package de.arraying.arraybot.commands.commands.customization.filter.subcommands

import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.entities.SubCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.UtilsLimit
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

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
class SubCommandFilterAdd: 
        SubCommand("add") {

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val mod = environment.cache?.mod?: return
        if(args.size < 3) {
            Messages.COMMAND_FILTER_ADD_PROVIDE.send(channel).queue()
            return
        }
        val stringBuilder = StringBuilder()
        for(i in (2..args.size-1)) {
            stringBuilder.append(args[i])
                    .append(" ")
        }
        val phrase = stringBuilder.toString().toLowerCase().trim()
                .replace("{space}", " ")
        if(phrase.length > UtilsLimit.FILTER_PHRASE.maxLength) {
            Messages.COMMAND_FILTER_ADD_LENGTH.send(channel).queue()
            return
        }
        if(mod.filtered.contains(phrase)) {
            Messages.COMMAND_FILTER_ADD_EXISTS.send(channel).queue()
            return
        }
        if(mod.filterRegex) {
            try {
                Pattern.compile(phrase)
            } catch(exception: PatternSyntaxException) {
                Messages.COMMAND_FILTER_ADD_REGEX.send(channel).queue()
                return
            }
        }
        arraybot.managerSql.addChatFilterPhrase(environment.guild.idLong, phrase)
        Messages.COMMAND_FILTER_ADD_UPDATE.send(channel).queue()
    }

}