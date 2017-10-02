package de.arraying.arraybot.command.custom.type.actions

import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UPair
import de.arraying.arraybot.util.objects.ActionPair

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
open class RoleAction {

    /**
     * Pre processes the role action for ease and DRY code.
     */
    protected fun preprocess(environment: CommandEnvironment, value: String): ActionPair<Long, Long?>? {
        val channel = environment.channel
        if(!UPair.isValid(value)) {
            Message.CUSTOM_TYPE_INVALID.send(channel).queue()
            return null
        }
        val pair = UPair.getAction(value)
        if(environment.guild.getRoleById(pair.a) == null) {
            Message.CUSTOM_TYPE_ROLE_UNKNOWN_ROLE.send(channel).queue()
            return null
        }
        if(pair.b != null
                && environment.guild.getMemberById(pair.b) == null) {
            Message.CUSTOM_TYPE_ROLE_UNKNOWN_USER.send(channel).queue()
            return null
        }
        return pair
    }

}