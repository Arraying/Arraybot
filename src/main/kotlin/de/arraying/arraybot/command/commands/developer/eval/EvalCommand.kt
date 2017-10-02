package de.arraying.arraybot.command.commands.developer.eval

import de.arraying.arraybot.command.commands.developer.eval.engines.JSREngine
import de.arraying.arraybot.command.commands.developer.eval.engines.ZeusEngine
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.objects.MultiKeyMap
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
class EvalCommand: DefaultCommand("eval",
        CommandCategory.DEVELOPER,
        Permission.MESSAGE_WRITE,
        aliases = arrayOf("evaluate", "exec", "execute", "breakbot")) {

    private val engines = MultiKeyMap<String, EvalEngine>()
    private val placeholder = "[...]"

    init {
        engines.add(ZeusEngine(), "zeus", arrayOf("z"))
        engines.add(JSREngine(JSREngine.Mode.JAVASCRIPT), "javascript", arrayOf("js", "javashit"))
    }

    /**
     * Evaluates the given code with the given engine.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 2) {
            Message.COMMANDS_EVAL_ENGINE_PROVIDE.send(channel).queue()
            return
        }
        val engine = args[1].toLowerCase()
        if(engines.getByKeyOrAlias(engine) == null) {
            val builder = StringBuilder()
            for(eng in engines.keys) {
                builder.append(eng)
                        .append(", ")
            }
            var engines = builder.toString()
            engines = if(!engines.isEmpty()) {
                    engines.substring(0, engines.length-2)
                } else {
                    "-"
                }
            val message = Message.COMMANDS_EVAL_ENGINE_INVALID.content(channel)
                    .replace("{engines}", engines)
            channel.sendMessage(message).queue()
            return
        }
        if(args.size < 3) {
            Message.COMMANDS_EVAL_CODE_PROVIDE.send(channel).queue()
            return
        }
        val builder = StringBuilder()
        for(i in (2 until args.size)) {
            builder.append(args[i])
                    .append(" ")
        }
        val code = builder.toString().trim()
        var result = engines.getByKeyOrAlias(engine)!!.evaluate(environment, code)
        if(result.length > Limits.MESSAGE.limit) {
            result = result.substring(0, Limits.MESSAGE.limit - placeholder.length) + placeholder
        }
        channel.sendMessage(result).queue()
    }
}