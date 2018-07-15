package de.arraying.arraybot.util

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.CommandEnvironment
import net.dv8tion.jda.core.Permission
import org.slf4j.LoggerFactory

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
object UPremium {

    /**
     * Checks if the guild is able to execute premium commands.
     */
    fun isPremium(environment: CommandEnvironment): Boolean {
        val arraybot = Arraybot.INSTANCE
        val logger = LoggerFactory.getLogger("Premium")
        val hub = arraybot.botManager.hub
        if(hub == null) {
            logger.error("The hub guild returned null.")
            return false
        }
        if(environment.guild.idLong == hub.idLong) {
            return true
        }
        val roleId = arraybot.configuration.premiumId
        return environment.guild.members.any {
            it.hasPermission(Permission.MANAGE_SERVER)
                    && hub.getMemberById(it.user.idLong) != null
                    && hub.getMemberById(it.user.idLong).roles.any {
                role -> role.idLong == roleId
            }
        }
    }

}