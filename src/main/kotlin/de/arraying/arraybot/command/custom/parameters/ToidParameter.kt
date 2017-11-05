package de.arraying.arraybot.command.custom.parameters

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.parameter.Parameter

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
class ToidParameter: Parameter {

    /**
     * Gets the trigger.
     */
    override fun getTrigger(): String {
        return "--toid"
    }

    /**
     * Parses the parameter.
     */
    override fun parse(environment: CommandEnvironment?, input: String?): String {
        var value = input!!
        val message = environment!!.message
        for(user in message.mentionedUsers) {
            value = value.replace(environment.guild.getMember(user).asMention, user.id)
        }
        for(channel in message.mentionedChannels) {
            value = value.replace(channel.asMention, channel.id)
        }
        for(role in message.mentionedRoles) {
            value = value.replace(role.asMention, role.id)
        }
        return value
    }

}