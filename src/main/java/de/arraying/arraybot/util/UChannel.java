package de.arraying.arraybot.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.internal.utils.PermissionUtil;

import java.util.List;
import java.util.regex.Pattern;

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
public class UChannel {

    /**
     * The regular expression for a regular role ID.
     */
    private static final Pattern ID_PATTERN = Pattern.compile("^\\d{17,20}$");

    /**
     * The regular expression for a mention.
     */
    private static final Pattern MENTION_PATTERN = Pattern.compile("^<#\\d{17,20}>$");

    /**
     * The regular expression for a role name.
     */
    private static final Pattern NAME_PATTERN = Pattern.compile("^.*$");

    /**
     * Gets the channel corresponding to the given role input.
     * @param guild The guild.
     * @param input The input.
     * @return A TextChannel object, or null if the input is invalid.
     */
    public static TextChannel getTextChannel(Guild guild, String input) {
        if(ID_PATTERN.matcher(input).find()
                || MENTION_PATTERN.matcher(input).find()) {
            return guild.getTextChannelById(input.replaceAll("\\D", ""));
        } else if(NAME_PATTERN.matcher(input).find()) {
            List<TextChannel> channels = guild.getTextChannelsByName(input, true);
            return channels.isEmpty() ? null : channels.get(0);
        } else {
            return null;
        }
    }

    /**
     * Whether or not the bot can talk in the text channel.
     * @param channel The channel.
     * @return True if they can, false otherwise.
     */
    public static boolean cantTalk(TextChannel channel) {
        Member self = channel.getGuild().getSelfMember();
        return !PermissionUtil.checkPermission(channel, self, Permission.MESSAGE_READ) || !PermissionUtil.checkPermission(channel, self, Permission.MESSAGE_WRITE);
    }

}
