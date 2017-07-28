package de.arraying.arraybot.commands.commands.developer.eval

import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.entities.DefaultCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.UtilsLimit
import net.dv8tion.jda.core.Permission
import javax.script.ScriptEngineManager
import javax.script.ScriptException

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
class CommandEval:
        DefaultCommand("eval",
                CommandCategory.DEVELOPER,
                Permission.MESSAGE_WRITE,
                aliases = arrayOf("evaluate", "exec", "execute")) {

    //TODO NEED TO WORK ON SCRIPT ENGINE

    val engine = ScriptEngineManager().getEngineByExtension("kts")!!

    init {
        engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, java.net, " +
                "Packages.net.dv8tion.jda.core, Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers);")
        engine.put("arraybot", arraybot)
        engine.put("cache", Cache)
    }

    /**
     * When the command is executed.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Messages.COMMAND_EVAL_PROVIDE.send(channel).queue()
            return
        }
        engine.put("jda", environment.guild.jda)
        engine.put("e", environment)
        engine.put("environment", environment)
        val stringBuilder = StringBuilder()
        for(i in (1..args.size-1)) {
            stringBuilder.append(args[i])
                    .append(" ")
        }
        val input = stringBuilder.toString().trim()
        var output: Any?
        try {
            output = engine.eval("(function() { with (imports) {\n$input\n} })();")
        } catch(scriptException: ScriptException) {
            val message = scriptException.message?: Messages.MISC_NONE.content(channel)
            output = Messages.COMMAND_EVAL_ERROR.content(channel)
                    .replace("{error}", message)
        }
        if(output == null) {
            output = Messages.COMMAND_EVAL_SUCCESSFUL.content(channel)
        }
        val outputString = output.toString()
        if(outputString.length > UtilsLimit.MESSAGE.maxLength) {
            Messages.COMMAND_EVAL_LENGTH.send(channel).queue()
            return
        }
        channel.sendMessage(outputString).queue()
    }


}