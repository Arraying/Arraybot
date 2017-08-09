package de.arraying.arraybot.core.punishment

import de.arraying.arraybot.core.iface.IPunishment
import de.arraying.arraybot.core.punishment.types.*

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
enum class PunishmentType(val punishment: IPunishment) {

    KICK(PunishmentTypeKick()),
    TEMPMUTE(PunishmentTypeTempMute()),
    UNMUTE(PunishmentTypeUnMute()),
    MUTE(PunishmentTypeMute()),
    SOFTBAN(PunishmentTypeSoftBan()),
    TEMPBAN(PunishmentTypeTempBan()),
    UNBAN(PunishmentTypeUnBan()),
    BAN(PunishmentTypeBan()),
    UNKNOWN(PunishmentTypeUnknown());

    /**
     * Whether or not the punishment type is temporary.
     */
    fun isTemp() = (this == TEMPMUTE || this == TEMPBAN)

    companion object {

        /**
         * Gets the punishment type via string.
         */
        fun getPunishableType(value: String): PunishmentType {
            var type: PunishmentType
            try {
                type = PunishmentType.valueOf(value.toUpperCase())
                if(type == UNMUTE
                        || type == UNBAN) {
                    type = UNKNOWN
                }
            } catch(exception: Exception) {
                type = UNKNOWN
            }
            return type
        }

    }

}