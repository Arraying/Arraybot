package de.arraying.arraybot.util;

import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.util.objects.Pair;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.utils.PermissionUtil;

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
public final class UPunishment {

    /**
     * Whether or not a member is muted.
     * @param member The member.
     * @return True if they are, false otherwise.
     */
    public static boolean isMute(Member member) {
        String mutedRole = getMutedRole(member.getGuild());
        for(Role role : member.getRoles()) {
            if(role.getId().equals(mutedRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the ID of the mute role.
     * @param guild The guild.
     * @return The ID of the mute role as a string.
     */
    public static String getMutedRole(Guild guild) {
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        return guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.MUTE_ROLE), guild.getIdLong(), null);
    }

    /**
     * Gets the ID of the mute permission role.
     * @param guild The guild.
     * @return The ID of the mute role as a string.
     */
    public static String getMutedPermission(Guild guild) {
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        return guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.MUTE_PERMISSION), guild.getIdLong(), null);
    }

    /**
     * Applies the muted role to the user.
     * @param guild The guild.
     * @param punishedId The ID of the punished user.
     * @return A pair of success and whether the punishment needs to be revoked.
     */
    public static Pair<Boolean, Boolean> mute(Guild guild, long punishedId) {
        return manageMute(guild, punishedId, true);
    }

    /**
     * Checks whether the specified member is banned in the guild.
     * @param guild The guild.
     * @param user The user.
     * @return True if they are, false otherwise.
     * @throws PermissionException If the bot has no permissions.
     */
    public static boolean isBan(Guild guild, long user)
            throws PermissionException {
        return PermissionUtil.checkPermission(guild.getSelfMember(), Permission.BAN_MEMBERS) && guild.getBanList().complete().stream().anyMatch(ban -> ban.getUser().getIdLong() == user);
    }

    /**
     * Removes the muted role from the user.
     * @param guild The guild.
     * @param punishedId The ID of the punished user.
     * @return A pair of success and whether the punishment needs to be revoked.
     */
    public static Pair<Boolean, Boolean> unmute(Guild guild, long punishedId) {
        return manageMute(guild, punishedId, false);
    }

    /**
     * Bans a user.
     * @param guild The guild.
     * @param punishedId The ID of the punished user.
     * @param reason The reason for the ban.
     * @return A pair of success and whether the punishment needs to be revoked.
     */
    public static Pair<Boolean, Boolean> ban(Guild guild, long punishedId, String reason) {
        try {
            guild.getController().ban(String.valueOf(punishedId), 0, reason).queue();
            return new Pair<>(true, false);
        } catch(PermissionException exception) {
            return new Pair<>(false, false);
        }
    }

    /**
     * Unbans a user.
     * @param guild The guild.
     * @param punishedId The ID of the punished user.
     * @return True if the unban was successful, false otherwise.
     */
    public static boolean unban(Guild guild, long punishedId) {
        if(!isBan(guild, punishedId)) {
            return true;
        }
        try {
            guild.getController().unban(String.valueOf(punishedId)).queue();
            return true;
        } catch(PermissionException exception) {
            return false;
        }
    }

    /**
     * Applies or removes the muted role to the specified user.
     * This method is used to permanently mute.
     * @param guild The guild.
     * @param punishedId The ID of the muted user.
     * @param apply True: adds role, false: removes role.
     * @return A pair of success and whether the punishment needs to be revoked.
     */
    private static Pair<Boolean, Boolean> manageMute(Guild guild, long punishedId, boolean apply) {
        GuildEntry entry = (GuildEntry) Category.GUILD.getEntry();
        String mutedRoleId = entry.fetch(entry.getField(GuildEntry.Fields.MUTE_ROLE), guild.getIdLong(), null);
        Member muted = guild.getMemberById(punishedId);
        Role mutedRole = guild.getRoleById(mutedRoleId);
        if(muted == null
                || mutedRole == null) {
            return new Pair<>(false, false);
        }
        try {
            if(apply) {
                guild.getController().addSingleRoleToMember(muted, mutedRole).queue();
            } else {
                guild.getController().removeSingleRoleFromMember(muted, mutedRole).queue();
            }
            return new Pair<>(true, false);
        } catch(PermissionException exception) {
            return new Pair<>(false, false);
        }
    }

}
