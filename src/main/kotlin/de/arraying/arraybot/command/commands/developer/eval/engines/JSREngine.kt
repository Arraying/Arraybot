package de.arraying.arraybot.command.commands.developer.eval.engines

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.commands.developer.eval.EvalEngine
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.language.Message
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
class JSREngine(private val mode: Mode): EvalEngine {

    private val scriptEngineManager = ScriptEngineManager()
    private val kotlinEngine = scriptEngineManager.getEngineByExtension("kts")!!
    private val javaScriptEngine = scriptEngineManager.getEngineByName("nashorn")!!

    init {
        kotlinEngine.put("arraybot", Arraybot.getInstance())
        javaScriptEngine.put("arraybot", Arraybot.getInstance())
        javaScriptEngine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, java.net, " +
                "Packages.net.dv8tion.jda.core, Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers);")
    }

    /**
     * Evaluates the code.
     */
    override fun evaluate(environment: CommandEnvironment, code: String): String {
        val channel = environment.channel
        val engine = when(mode) {
            Mode.KOTLIN -> kotlinEngine
            Mode.JAVASCRIPT -> javaScriptEngine
        }
        engine.put("jda", environment.guild.jda)
        engine.put("e", environment)
        engine.put("environment", environment)
        var output: Any? = try {
            when(mode) {
                Mode.KOTLIN -> engine.eval(code)
                Mode.JAVASCRIPT -> engine.eval("(function() { with (imports) {\n$code\n} })();")
            }
        } catch(exception: Exception) {
            Message.COMMANDS_EVAL_ERROR.content(channel)
                    .replace("{error}", if(exception.message == null) "null" else exception.message!!)
        }
        if(output == null) {
            output = Message.COMMANDS_EVAL_EVALUATED.content(channel)
        }
        return output.toString()
    }

    /**
     * All eval modes for JSE.
     */
    enum class Mode {

        /**
         * Evaluates Kotlin code.
         */
        KOTLIN,


        /**
         * Evaluates JS code using the Nashorn interpreter.
         */
        JAVASCRIPT

    }

}