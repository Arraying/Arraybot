package de.arraying.arraybot.manager;

import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.templates.SetEntry;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.punishment.PunishmentObject;
import de.arraying.arraybot.punishment.PunishmentType;
import de.arraying.arraybot.util.*;
import de.arraying.arraybot.util.objects.Pair;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

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
public final class PunishmentManager {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(15);

    /**
     * Creates a new punishment.
     * @param guild The guild in which the punishment occurred.
     * @param punished The punished user.
     * @param type The punishment type.
     * @param staff The staff member.
     * @param viaAudit Whether or not the punishment has already occurred and the manager is just handling the result.
     * @param expiration The time, in milliseconds, when the punishment will expire.
     * @param reason The reason for the punishment.
     * @return True if the punishment was successful, false if it was not.
     */
    public boolean punish(Guild guild, long punished, PunishmentType type, long staff, long expiration, boolean viaAudit, String reason) {
        reason = reason == null ? Message.PUNISH_EMBED_REASON_DEFAULT.getContent(guild.getIdLong()) : reason;
        Boolean revocation = null;
        if(!viaAudit) {
            Pair<Boolean, Boolean> result = type.getPunishment().punish(guild, punished, reason);
            if(!result.getA()) {
                return false;
            }
            revocation = result.getB();
        }
        long guildId = guild.getIdLong();
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        int old = Integer.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.COUNT_PUNISHMENT), guildId, null));
        boolean revoke = revocation == null ? type == PunishmentType.KICK : revocation;
        PunishmentObject punishmentObject = new PunishmentObject(++old, punished, type, staff, expiration, revoke, reason);
        punishmentObject.toRedis(guild);
        schedulePunishmentRevocation(guild, punishmentObject);
        log(guild, punishmentObject, false, null);
        return true;
    }

    /**
     * Schedules the punishment revocation.
     * @param guild The guild.
     * @param punishmentObject The punishment object.
     */
    void schedulePunishmentRevocation(Guild guild, PunishmentObject punishmentObject) {
        if(punishmentObject.isRevoked()) {
            return;
        }
        if(punishmentObject.getType() != PunishmentType.TEMP_MUTE
                && punishmentObject.getType() != PunishmentType.TEMP_BAN) {
            return;
        }
        long expiration = punishmentObject.getExpiration();
        long current = System.currentTimeMillis();
        if(expiration <= 0) {
            return;
        }
        if(expiration <= current) {
            //noinspection ResultOfMethodCallIgnored
            revoke(guild, punishmentObject, null);
            return;
        }
        executor.schedule(() -> {
            if(!punishmentObject.isRevoked()) {
                revoke(guild, punishmentObject, null);
            }
        }, (expiration - current), TimeUnit.MILLISECONDS);
    }

    /**
     * Revokes a punishment.
     * @param guild The guild in which the punishment occurred.
     * @param punishmentObject The punishment.
     * @param revoker The person who revoked this punishment, can be null if automatic.
     * @return True if the revocation was successful, false otherwise.
     */
    public boolean revoke(Guild guild, PunishmentObject punishmentObject, Long revoker) {
        boolean success = punishmentObject.getType().getPunishment().revoke(guild, punishmentObject.getUser());
        if(!success) {
            return false;
        }
        punishmentObject.revoke();
        punishmentObject.toRedis(guild);
        log(guild, punishmentObject, true, revoker);
        return true;
    }

    /**
     * Gets all punishments for a guild.
     * @param guild The guild.
     * @return A list of all punishments.
     */
    public List<PunishmentObject> getAllPunishments(Guild guild) {
        long guildId = guild.getIdLong();
        SetEntry entry = (SetEntry) Category.PUNISHMENT_IDS.getEntry();
        List<PunishmentObject> punishments = new ArrayList<>();
        for(String punishment : entry.values(guildId)) {
            punishments.add(PunishmentObject.fromRedis(guild, Integer.valueOf(punishment)));
        }
        return punishments;
    }

    /**
     * Gets a specific punishment based on a filter.
     * @param guild The guild.
     * @param filter The filter.
     * @return A punishment, or null.
     */
    public PunishmentObject getSpecificPunishment(Guild guild, Predicate<? super PunishmentObject> filter) {
        return getAllPunishments(guild).stream()
                .filter(filter)
                .findFirst()
                .orElse(null);
    }

    /**
     * Logs the punishment into a logging channel.
     * @param guild The guild.
     * @param punishmentObject The punishment.
     * @param onRevoke If this log occurred during the revocation.
     * @param revoker The person who revoked this punishment, can be null if automatic or not revoked.
     */
    private void log(Guild guild, PunishmentObject punishmentObject, boolean onRevoke, Long revoker) {
        GuildEntry entry = (GuildEntry) Category.GUILD.getEntry();
        long channelId = Long.valueOf(entry.fetch(entry.getField(GuildEntry.Fields.PUNISHMENT_CHANNEL), guild.getIdLong(), null));
        TextChannel channel = guild.getTextChannelById(channelId);
        if(channel == null
                || !UChannel.canTalk(channel)) {
            return;
        }
        String punishmentType;
        if(onRevoke) {
            switch(punishmentObject.getType()) {
                case TEMP_BAN:
                case BAN:
                    punishmentType = Message.PUNISH_TYPE_UNBAN.getContent(channel);
                    break;
                case TEMP_MUTE:
                case MUTE:
                    punishmentType = Message.PUNISH_TYPE_UNMUTE.getContent(channel);
                    break;
                default:
                    punishmentType = Message.PUNISH_TYPE_UNKNOWN.getContent(channel);
            }
        } else {
            punishmentType = punishmentObject.getType().getName().getContent(channel);
        }
        CustomEmbedBuilder embed = UEmbed.getEmbed(channel)
                .setAuthor(Message.PUNISH_EMBED_TITLE.getContent(channel, punishmentType, String.valueOf(punishmentObject.getId())), null, null)
                .addField(Message.PUNISH_EMBED_USER.getContent(channel),
                        UUser.asMention(punishmentObject.getUser()),
                        false)
                .addField(Message.PUNISH_EMBED_STAFF.getContent(channel),
                        onRevoke ?
                                revoker == null ?
                                        Message.PUNISH_EMBED_AUTOMATIC.getContent(channel) :
                                        revoker == UDefaults.DEFAULT_UNKNOWN_SNOWFLAKE ? Message.PUNISH_EMBED_UNKNOWN.getContent(channel) :
                                                UUser.asMention(revoker)
                                :
                                UUser.asMention(punishmentObject.getStaff()),
                        false);
        if(!onRevoke) {
            embed.addField(Message.PUNISH_EMBED_REASON.getContent(channel),
                    punishmentObject.getReason(),
                    false);
            if(punishmentObject.getExpiration() > 0) {
                embed.addField(Message.PUNISH_EMBED_EXPIRATION.getContent(channel),
                        UTime.getDisplayableTime(guild, punishmentObject.getExpiration()),
                        false)
                        .setFooter(Message.PUNISH_EMBED_EXPIRATION_FOOTER.getContent(channel), null);
            }
        }
        channel.sendMessage(embed.build()).queue();
    }

}
