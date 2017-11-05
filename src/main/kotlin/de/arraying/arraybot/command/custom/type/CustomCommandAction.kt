package de.arraying.arraybot.command.custom.type

import de.arraying.arraybot.command.CommandEnvironment
import net.dv8tion.jda.core.entities.TextChannel

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
interface CustomCommandAction {

    /**
     * Gets the message for the action.
     */
    fun getMessage(channel: TextChannel): String

    /**
     * Executes the action.
     */
    fun onAction(environment: CommandEnvironment, value: String): Boolean

}