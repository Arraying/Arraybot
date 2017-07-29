package de.arraying.arraybot.commands.commands.developer.eval

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.ULimit
import javax.script.ScriptEngineManager

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
object Evaluator {

    private val scriptEngineManager = ScriptEngineManager()
    private val kotlinEngine = scriptEngineManager.getEngineByExtension("kts")!!
    private val javascriptEngine = scriptEngineManager.getEngineByName("nashorn")!!
    private var initialized = false
            set(value) {
                if(!initialized) {
                    field = value
                }
            }

    fun init() {
        if(initialized) {
            throw IllegalStateException("The evaluator has already been initialized.")
        }
        kotlinEngine.put("arraybot", Arraybot.instance)
        kotlinEngine.put("cache", Cache)
        javascriptEngine.put("arraybot", Arraybot.instance)
        javascriptEngine.put("cache", Cache)
        javascriptEngine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, java.net, " +
                "Packages.net.dv8tion.jda.core, Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers);")
        initialized = true
    }

    fun evaluate(mode: CommandEval.EvalModes, environment: CommandEnvironment, args: Array<String>) {
        if(args.size < 3) {
            throw IllegalStateException("The arguments supplied must have at least 3 entries.")
        }
        val channel = environment.channel
        val stringBuilder = StringBuilder()
        for(i in (2..args.size-1)) {
            stringBuilder.append(args[i])
                    .append(" ")
        }
        val input = stringBuilder.toString().trim()
        val engine = when(mode) {
            CommandEval.EvalModes.KOTLIN -> kotlinEngine
            CommandEval.EvalModes.JAVASCRIPT -> javascriptEngine
        }
        engine.put("jda", environment.guild.jda)
        engine.put("e", environment)
        engine.put("environment", environment)
        var output:  Any?
        try {
            output = when(mode) {
                CommandEval.EvalModes.KOTLIN -> engine.eval(input)
                CommandEval.EvalModes.JAVASCRIPT -> engine.eval("(function() { with (imports) {\n$input\n} })();")
            }
        } catch(exception: Exception) {
            val message = exception.message?: Messages.MISC_NONE.content(channel)
            output = Messages.COMMAND_EVAL_ERROR.content(channel)
                    .replace("{error}", message)
        }
        if(output == null) {
            output = Messages.COMMAND_EVAL_SUCCESSFUL.content(channel)
        }
        val outputString = output.toString()
        if(outputString.length > ULimit.MESSAGE.maxLength) {
            Messages.COMMAND_EVAL_LENGTH.send(channel).queue()
            return
        }
        channel.sendMessage(outputString).queue()
    }

}