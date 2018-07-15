package de.arraying.arraybot.command.commands.moderation.clear.parameters

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.parameter.Parameter
import de.arraying.arraybot.util.UParameter
import de.arraying.arraybot.util.UUser

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
class UserClearParameter: Parameter {

    /**
     * Gets the trigger.
     */
    override fun getTrigger(): String {
        return "--user"
    }

    /**
     * Parses the parameter.
     */
    override fun parse(environment: CommandEnvironment?, input: String?): String {
        var toReturn = input!!.replace(trigger, "")
        val id = environment!!.message.idLong
        val storage = Arraybot.INSTANCE.storageManager.clearCommandStorageDataStorage.get(id)
        val userRaw = UParameter.getParameterValue(input, trigger)
        if(userRaw.isEmpty()) {
            return toReturn
        }
        toReturn = input.replace(trigger + " " + userRaw, "")
        val user = UUser.getMember(environment.guild, userRaw)?: return toReturn
        storage.users.add(user.user.idLong)
        return toReturn
    }

}