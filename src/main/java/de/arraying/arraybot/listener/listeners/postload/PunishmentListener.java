package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.manager.PunishmentManager;
import de.arraying.arraybot.punishment.PunishmentObject;
import de.arraying.arraybot.punishment.PunishmentType;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.util.ULambda;
import de.arraying.arraybot.util.UPunishment;
import de.arraying.arraybot.util.objects.Pair;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;

import java.util.List;

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
public final class PunishmentListener extends PostLoadListener {

    private PunishmentManager punishmentManager;

    /**
     * Initializes the punishment manager.
     */
    @Override
    public void init() {
        punishmentManager = Arraybot.getInstance().getPunishmentManager();
    }

    /**
     * When a member joins the guild.
     * This event is used to check if they are bypassing a mute.
     * @param event The event.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        if(event.getGuild() == null
                || !event.getGuild().getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            return;
        }
        new AbstractTask("Punishment-BypassChecker") {

            /**
             * Checks if the specified member is bypassing a punishment.
             */
            @Override
            public void onExecution() {
                Guild guild = event.getGuild();
                long user = event.getMember().getUser().getIdLong();
                boolean punish = punishmentManager.getAllPunishments(guild).stream()
                        .anyMatch(punishment -> punishment.getType() == PunishmentType.MUTE
                            && punishment.getUser() == user
                            && !punishment.isRevoked());
                if(!punish) {
                    return;
                }
                Pair<Boolean, Boolean> success = UPunishment.mute(guild, user);
                if(!success.getA()) {
                    logger.warn("Re-muting of {} unsuccessful.", user);
                } else {
                    logger.info("{} was caught mute evading in the guild {}.", user, guild.getIdLong());
                }
            }

        }.create();
    }

    /**
     * When a member leaves the guild.
     * Used to check if they have been kicked.
     * @param event The event.
     */
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        handlePunishment(event, event.getUser(), PunishmentType.KICK, null, false);
    }

    /**
     * When a member has a role added to them.
     * Used to check if somebody is being muted.
     * @param event The event.
     */
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        handlePunishment(event, event.getUser(), PunishmentType.MUTE, event.getRoles().toArray(new Role[event.getRoles().size()]), false);
    }

    /**
     * When a member is banned.
     * @param event The event.
     */
    @Override
    public void onGuildBan(GuildBanEvent event) {
        handlePunishment(event, event.getUser(), PunishmentType.BAN, null, false);
    }

    /**
     * When a member has a role removed from them.
     * Used to check if somebody is being unmuted.
     * @param event The event.
     */
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        handlePunishment(event, event.getUser(), PunishmentType.MUTE, event.getRoles().toArray(new Role[event.getRoles().size()]), true);
    }

    /**
     * When a member is unbanned.
     * @param event The event.
     */
    @Override
    public void onGuildUnban(GuildUnbanEvent event) {
        handlePunishment(event, event.getUser(), PunishmentType.BAN, null, true);
    }

    /**
     * Handles the punishment or punishment revocation.
     * @param event The event.
     * @param userObject The user object.
     * @param type The type of punishment.
     * @param roles The roles involved in the event, should be null unless it is a mute or unmute.
     * @param revoke True: revocation, false: punishment creation.
     */
    private void handlePunishment(GenericGuildEvent event, User userObject, PunishmentType type, Role[] roles, boolean revoke) {
        Guild guild = event.getGuild();
        if(guild == null
                || !event.getGuild().getSelfMember().hasPermission(Permission.VIEW_AUDIT_LOGS)) {
            return;
        }
        long user = userObject.getIdLong();
        guild.getAuditLogs().queue(entries -> {
            if(entries.isEmpty()) {
                return;
            }
            AuditLogEntry entry = entries.get(0);
            if(!isPunishment(entry, user, type, roles)
                    || entry.getUser().getIdLong() == guild.getSelfMember().getUser().getIdLong()) {
                return;
            }
            long staff = entry.getUser().getIdLong();
            if(!revoke) {
                punishmentManager.punish(guild, user, type, staff, -1, true, entry.getReason());
            } else {
                PunishmentObject punishmentObject = ULambda.INSTANCE.getSpecificGeneralizedPunishment(guild, user, type);
                if(punishmentObject == null) {
                    return;
                }
                if(!punishmentManager.revoke(guild, punishmentObject, staff)) {
                    Arraybot.getInstance().getLogger().error("Failed the revocation of the punishment number {} in the guild {}.", punishmentObject.getId(), guild.getIdLong());
                }
            }
        });
    }


    /**
     * Whether or not the latest audit log shows that it is a punishment.
     * @param entry The audit log entry.
     * @param user The user.
     * @param type The punishment type.
     * @param roles The roles involved in the event, should be null unless it is a mute.
     * @return True if it is, false otherwise.
     */
    private boolean isPunishment(AuditLogEntry entry, long user, PunishmentType type, Role[] roles) {
        if(entry.getTargetIdLong() != user) {
            return false;
        }
        if(entry.getType() != type.getAuditType()
                && entry.getType() != type.getUndoAuditType()) {
            return false;
        }
        if(type == PunishmentType.MUTE) {
            String mutedRole = UPunishment.getMutedRole(entry.getGuild());
            for(Role role : roles) {
                if(role.getId().equals(mutedRole)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
