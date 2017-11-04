package de.arraying.arraybot.util

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.punishment.PunishmentObject
import de.arraying.arraybot.punishment.PunishmentType
import net.dv8tion.jda.core.entities.Guild

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
object ULambda {

    /**
     * Gets a specific punishment but uses generalized types.
     * This method is written in Kotlin as it is extremely tedious to do within Java.
     * E.g. MUTE = MUTE || TEMP_MUTE
     */
    fun getSpecificGeneralizedPunishment(guild: Guild, user: Long, type: PunishmentType): PunishmentObject? {
        return Arraybot.getInstance().punishmentManager.getSpecificPunishment(guild, {
            it.user == user
            && if(type == PunishmentType.BAN) {
                (it.type == PunishmentType.BAN
                        || it.type == PunishmentType.TEMP_BAN)
            } else if (it.type == PunishmentType.MUTE){
                (it.type == PunishmentType.MUTE
                        || it.type == PunishmentType.TEMP_MUTE)
            } else {
                true
            }
            && !it.isRevoked
        })
    }

}