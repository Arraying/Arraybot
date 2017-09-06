package de.arraying.arraybot.misc;

/**
 * Copyright 2017 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public enum  PunishmentType {

    /**
     * When a user is kicked.
     */
    KICK,

    /**
     * When a user is temporarily muted.
     */
    TEMP_MUTE,

    /**
     * When a user is un-muted.
     */
    UN_MUTE,

    /**
     * When a user is permanently muted.
     */
    MUTE,

    /**
     * When a user is kicked and their messages of the last 1 day cleared.
     */
    SOFTBAN,

    /**
     * When a user is temporarily banned.
     */
    TEMP_BAN,

    /**
     * When a user is un-banned.
     */
    UN_BAN,

    /**
     * When a user is permanently banned.
     */
    BAN,

    /**
     * When a severe error occurs or the Great War breaks out.
     * I feel like I play too much Fallout 4, thanks Bethesda for making it so addictive!
     */
    UNKNOWN

}
