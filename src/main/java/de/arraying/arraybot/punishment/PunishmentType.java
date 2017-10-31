package de.arraying.arraybot.punishment;

import de.arraying.arraybot.punishment.punishments.KickPunishment;

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
public enum PunishmentType {

    /*
     * Todo: Create different punishment types!
     */

    /**
     * When a user is kicked.
     */
    KICK(new KickPunishment()),

    /**
     * When a user is temporarily muted.
     */
    TEMP_MUTE(null),

    /**
     * When a user is un-muted.
     */
    UN_MUTE(null),

    /**
     * When a user is permanently muted.
     */
    MUTE(null),

    /**
     * When a user is kicked and their messages of the last 1 day cleared.
     */
    SOFT_BAN(null),

    /**
     * When a user is temporarily banned.
     */
    TEMP_BAN(null),

    /**
     * When a user is un-banned.
     */
    UN_BAN(null),

    /**
     * When a user is permanently banned.
     */
    BAN(null),

    /**
     * When a severe error occurs or the Great War breaks out.
     * I feel like I play too much Fallout 4, thanks Bethesda for making it so addictive!
     */
    UNKNOWN((guild, punishedId, reason) -> false);

    private final Punishment punishment;

    /**
     * Sets the punishment.
     * @param punishment The punishment.
     */
    PunishmentType(Punishment punishment) {
        this.punishment = punishment;
    }

    /**
     * Gets the punishment.
     * @return The punishment interface implementation.
     */
    public Punishment getPunishment() {
        return punishment;
    }
}
