package de.arraying.arraybot.punishment;

import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.punishment.punishments.BanPunishment;
import de.arraying.arraybot.punishment.punishments.KickPunishment;
import de.arraying.arraybot.punishment.punishments.MutePunishment;
import de.arraying.arraybot.punishment.punishments.SoftBanPunishment;
import de.arraying.arraybot.util.objects.Pair;
import net.dv8tion.jda.core.entities.Guild;

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

    /**
     * When a user is kicked.
     */
    KICK(new KickPunishment(), Message.PUNISH_TYPE_KICK),

    /**
     * When a user is temporarily muted.
     */
    TEMP_MUTE(new MutePunishment(), Message.PUNISH_TYPE_TEMPMUTE),

    /**
     * When a user is permanently muted.
     */
    MUTE(new MutePunishment(), Message.PUNISH_TYPE_MUTE),

    /**
     * When a user is kicked and their messages of the last 1 day cleared.
     */
    SOFT_BAN(new SoftBanPunishment(), Message.PUNISH_TYPE_SOFTBAN),

    /**
     * When a user is temporarily banned.
     */
    TEMP_BAN(new BanPunishment(), Message.PUNISH_TYPE_TEMPBAN),

    /**
     * When a user is permanently banned.
     */
    BAN(new BanPunishment(), Message.PUNISH_TYPE_BAN),

    /**
     * When a severe error occurs or the Great War breaks out.
     * I feel like I play too much Fallout 4, thanks Bethesda for making it so addictive!
     */
    UNKNOWN(new Punishment() {

        /**
         * Just returns false.
         * @param guild The guild where the punishment is to occur.
         * @param punishedId The ID of the punished user.
         * @param reason The reason for the punishment.
         * @return False.
         */
        @Override
        public Pair<Boolean, Boolean> punish(Guild guild, long punishedId, String reason) {
            return new Pair<>(false, false);
        }

        /**
         * Returns false too.
         * @param guild The guild where the punishment occurred.
         * @param punishedId The ID of the punishment.
         * @return False.
         */
        @Override
        public boolean revoke(Guild guild, long punishedId) {
            return false;
        }

    }, Message.PUNISH_TYPE_UNKNOWN);

    private final Punishment punishment;
    private final Message name;

    /**
     * Sets the punishment.
     * @param punishment The punishment.
     * @param name The name of the type as a message.
     */
    PunishmentType(Punishment punishment, Message name) {
        this.punishment = punishment;
        this.name = name;
    }

    /**
     * Gets a punishment type from a string.
     * @param type The string.
     * @return The punishment type.
     */
    public static PunishmentType fromString(String type) {
        try {
            return PunishmentType.valueOf(type.toUpperCase());
        } catch(IllegalArgumentException exception) {
            return UNKNOWN;
        }
    }

    /**
     * Gets the punishment.
     * @return The punishment interface implementation.
     */
    public Punishment getPunishment() {
        return punishment;
    }

    /**
     * Gets the name.
     * @return The appropriate message enumeration for the type.
     */
    public Message getName() {
        return name;
    }

}
