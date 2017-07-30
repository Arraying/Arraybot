package de.arraying.arraybot.cache.entities

import de.arraying.arraybot.core.iface.ICache

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
class CBypass(val id: Long,
              val bypassId: Long,
              rawType: String,
              val value: Long):
        ICache {

    val bypassType = BypassType.getBypassType(rawType)

    enum class BypassType {

        USER,
        CHANNEL,
        ROLE,
        UNKNOWN;

        companion object {
            /**
             * Gets the bypass type via string.
             */
            fun getBypassType(value: String): BypassType {
                var type: BypassType
                try {
                    type = BypassType.valueOf(value.toUpperCase())
                } catch(exception: Exception) {
                    type = UNKNOWN
                }
                return type
            }
        }

    }

}