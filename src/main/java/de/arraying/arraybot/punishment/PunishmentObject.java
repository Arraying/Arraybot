package de.arraying.arraybot.punishment;

import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.categories.PunishmentEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.templates.SetEntry;
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
public final class PunishmentObject {

    private final int id;
    private final long user;
    private final PunishmentType type;
    private final long staff;
    private final long expiration;
    private boolean revoked;
    private final String reason;

    /**
     * Creates a new punishment cache object.
     * @param id The ID of the punishment.
     * @param user The ID of the punished user.
     * @param type The type of punishment.
     * @param staff The ID of the staff member.
     * @param expiration The time, in milliseconds, when the punishment expires.
     * @param revoked Whether or not the punishment has been revoked already.
     * @param reason The reason for the punishment.
     */
    public PunishmentObject(int id, long user, PunishmentType type, long staff, long expiration, boolean revoked, String reason) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.staff = staff;
        this.expiration = expiration;
        this.revoked = revoked;
        this.reason = reason;
    }

    /**
     * Gets the punishment from Redis.
     * @param guild The guild.
     * @param id The ID of the punishment.
     * @return A punishment object.
     */
    public static PunishmentObject fromRedis(Guild guild, int id) {
        long guildId = guild.getIdLong();
        PunishmentEntry entry = (PunishmentEntry) Category.PUNISHMENT.getEntry();
        long user = Long.valueOf(entry.fetch(entry.getField(PunishmentEntry.Fields.USER), guildId, id));
        PunishmentType type = PunishmentType.fromString(entry.fetch(entry.getField(PunishmentEntry.Fields.TYPE), guildId, id));
        long staff = Long.valueOf(entry.fetch(entry.getField(PunishmentEntry.Fields.STAFF), guildId, id));
        long expiration = Long.valueOf(entry.fetch(entry.getField(PunishmentEntry.Fields.EXPIRATION), guildId, id));
        boolean revoked = Boolean.valueOf(entry.fetch(entry.getField(PunishmentEntry.Fields.REVOKED), guildId, id));
        String reason = entry.fetch(entry.getField(PunishmentEntry.Fields.REASON), guildId, id);
        return new PunishmentObject(id, user, type, staff, expiration, revoked, reason);
    }

    /**
     * Saves the punishment object to Redis.
     * @param guild The guild.
     */
    public void toRedis(Guild guild) {
        long guildId = guild.getIdLong();
        SetEntry punishments = (SetEntry) Category.PUNISHMENT_IDS.getEntry();
        punishments.add(guildId, id);
        PunishmentEntry punishmentEntry = (PunishmentEntry) Category.PUNISHMENT.getEntry();
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.PUNISHMENT_ID), guildId, id, id);
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.USER), guildId, id, user);
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.TYPE), guildId, id, type.toString());
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.STAFF), guildId, id, staff);
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.EXPIRATION), guildId, id, expiration);
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.REVOKED), guildId, id, revoked);
        punishmentEntry.push(punishmentEntry.getField(PunishmentEntry.Fields.REASON), guildId, id, reason);
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.COUNT_PUNISHMENT), guildId, null, id);
    }

    /**
     * Gets the ID of the punishment.
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the punished user.
     * @return The ID of the punished user.
     */
    public long getUser() {
        return user;
    }

    /**
     * Gets the punishment type.
     * @return The type of punishment.
     */
    public PunishmentType getType() {
        return type;
    }

    /**
     * Gets the staff who punished.
     * @return The ID of the punishing user.
     */
    public long getStaff() {
        return staff;
    }

    /**
     * Gets the expiration date.
     * @return A time, in milliseconds, when the punishment will expire, or -1 if it is permanent.
     */
    public long getExpiration() {
        return expiration;
    }

    /**
     * Whether or not the punishment is revoked.
     * @return True if it is, false otherwise.
     */
    public boolean isRevoked() {
        return revoked;
    }

    /**
     * Gets the punishment reason.
     * @return The reason for the punishment.
     */
    public String getReason() {
        return reason;
    }

    /**
     * Marks the punishment as revoked.
     * This does not update the Redis entry for this punishment.
     * If the Redis entry needs to be updated, invoke toRedis(Guild) afterwards.
     */
    public void revoke() {
        this.revoked = true;
    }

}
