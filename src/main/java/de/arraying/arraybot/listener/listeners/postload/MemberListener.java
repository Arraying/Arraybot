package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.util.Limits;
import de.arraying.arraybot.util.UChannel;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.arraybot.util.UPlaceholder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class MemberListener extends PostLoadListener {

    private final Logger logger = LoggerFactory.getLogger("Member-Listener");
    private final String privateMessageParameter = "--pm";

    /**
     * Does not need initialization.
     */
    @Override
    public void init() {
    }

    /**
     * The event fired when a member joins a guild.
     * @param event The event.
     */
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        handleMemberMessages(event, true);
        assignAutoRole(event.getMember());
    }

    /**
     * The event fired when a member leaves a guild.
     * @param event The event.
     */
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        handleMemberMessages(event, false);
    }

    /**
     * Logs a join/leave message.
     * @param event The event.
     * @param join True = join, false = leave.
     */
    private void handleMemberMessages(GenericGuildMemberEvent event, boolean join) {
        Guild guild = event.getGuild();
        long guildId = guild.getIdLong();
        GuildEntry entry = (GuildEntry) Category.GUILD.getEntry();
        GuildEntry.Fields field;
        GuildEntry.Fields channelId;
        GuildEntry.Fields messageId;
        if(join) {
            field = GuildEntry.Fields.JOIN_ANNOUNCER;
            channelId = GuildEntry.Fields.JOIN_CHANNEL;
            messageId = GuildEntry.Fields.JOIN_MESSAGE;
        } else {
            field = GuildEntry.Fields.LEAVE_ANNOUNCER;
            channelId = GuildEntry.Fields.LEAVE_CHANNEL;
            messageId = GuildEntry.Fields.LEAVE_MESSAGE;
        }
        boolean log = Boolean.valueOf(entry.fetch(entry.getField(field), guildId, null));
        if(!log) {
            return;
        }
        String id = entry.fetch(entry.getField(channelId), guildId, null);
        TextChannel channel = guild.getTextChannelById(id);
        if(channel == null
                || !UChannel.canTalk(channel)) {
            return;
        }
        String message = entry.fetch(entry.getField(messageId), guildId, null);
        if(message.equals(UDefaults.DEFAULT_NULL)) {
            return;
        }
        message = UPlaceholder.replaceCore(event.getMember(), message);
        boolean privateMessage = false;
        if(message.contains(privateMessageParameter)) {
            privateMessage = true;
            message = message.replace(privateMessageParameter, "");
        }
        if(message.length() > Limits.MESSAGE.getLimit()) {
            return;
        }
        final String finalMessage = message;
        if(privateMessage) {
            event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(finalMessage).queue());
            return;
        }
        channel.sendMessage(finalMessage).queue();
    }

    /**
     * Applies the autorole to the specified member.
     * @param member The member.
     */
    private void assignAutoRole(Member member) {
        Guild guild = member.getGuild();
        long guildId = guild.getIdLong();
        GuildEntry entry = (GuildEntry) Category.GUILD.getEntry();
        boolean apply = Boolean.valueOf(entry.fetch(entry.getField(GuildEntry.Fields.AUTOROLE_ENABLED), guildId, null));
        if(!apply) {
            return;
        }
        String roleId = entry.fetch(entry.getField(GuildEntry.Fields.AUTOROLE_ROLE), guildId, null);
        Role role = guild.getRoleById(roleId);
        if(role == null) {
            return;
        }
        try {
            guild.getController().addSingleRoleToMember(member, role).queue();
        } catch(PermissionException exception) {
            logger.warn("Could not apply the autorole in the guild {} due to a permission error.", guildId);
        }
    }

}
