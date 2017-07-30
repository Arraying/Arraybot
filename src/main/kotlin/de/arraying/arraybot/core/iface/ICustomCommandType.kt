package de.arraying.arraybot.core.iface

import de.arraying.arraybot.commands.command.custom.entities.CustomCommandTypes
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.IResultMessage

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
interface ICustomCommandType :
        IResultMessage {

    /**
     * Gets the custom command type.
     */
    fun getType(): CustomCommandTypes

    /**
     * What happens when this type is invoked.
     */
    fun invoke(environment: CommandEnvironment, value: String)

}