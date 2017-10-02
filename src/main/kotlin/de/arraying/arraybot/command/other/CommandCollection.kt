package de.arraying.arraybot.command.other

import de.arraying.arraybot.command.commands.customization.custom.CustomCommand
import de.arraying.arraybot.command.commands.developer.eval.EvalCommand
import de.arraying.arraybot.command.commands.developer.script.ScriptCommand
import de.arraying.arraybot.command.commands.utils.TestCommand
import de.arraying.arraybot.command.commands.utils.ping.PingCommand
import de.arraying.arraybot.command.templates.DefaultCommand

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
enum class CommandCollection(val command: DefaultCommand) {

    /**
     * The custom command management command.
     */
    CUSTOM(CustomCommand()),

    /**
     * The command to evaluate code.
     */
    EVAL(EvalCommand()),

    /**
     * The command to check the WebSocket ping.
     */
    PING(PingCommand()),

    /**
     * The command to use to quickly evaluate a long Zeus script.
     */
    SCRIPT(ScriptCommand()),

    /**
     * A test command.
     */
    TEST(TestCommand());

}