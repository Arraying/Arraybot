package de.arraying.arraybot.command.custom.type

import de.arraying.arraybot.command.custom.type.actions.*
import de.arraying.arraybot.util.UFormatting

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
enum class CustomCommandType(val action: CustomCommandAction?) {

    /**
     * Adds a role to a member.
     */
    ADDROLE(AddRoleAction()),

    /**
     * Messages the channel the command was invoked in.
     */
    MESSAGE(MessageAction()),

    /**
     * Private messages the command executor.
     */
    PRIVATEMESSAGE(PrivateMessageAction()),

    /**
     * Removes a role from a member.
     */
    REMOVEROLE(RemoveRoleAction()),

    /**
     * Executes a piece of Zeus script.
     */
    SCRIPT(ScriptAction()),

    /**
     * Toggles the role of a member.
     * If the member has the role, it will remove it.
     * Else, it will add it.
     */
    TOGGLEROLE(ToggleRoleAction()),

    /**
     * If the type is unknown.
     */
    UNKNOWN(null);

    companion object {

        /**
         * Gets the type from the string.
         */
        fun fromString(value: String): CustomCommandType {
            return try {
                CustomCommandType.valueOf(value.toUpperCase())
            } catch(exception: Exception) {
                UNKNOWN
            }
        }

        /**
         * Gets the types as a list.
         */
        fun getTypes(): String {
            return UFormatting.formatToList(values())
        }

    }

}