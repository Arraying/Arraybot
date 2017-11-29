package de.arraying.arraybot.util;

import net.dv8tion.jda.core.entities.*;

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
public final class UPlaceholder {

    /**
     * Parses replaces the core placeholders.
     * @param member The member.
     * @param input The input.
     * @return A replaced string.
     */
    public static String replaceCore(Member member, String input) {
        Guild guild = member.getGuild();
        User user = member.getUser();
        return input.replace("{user}", user.getAsMention())
                .replace("{user_id}", user.getId())
                .replace("{user_avatar}", user.getEffectiveAvatarUrl())
                .replace("{user_name}", user.getName())
                .replace("{user_discriminator}", user.getDiscriminator())
                .replace("{user_nickname}", member.getEffectiveName())
                .replace("{guild_id}", guild.getId())
                .replace("{guild_icon}", guild.getIconUrl())
                .replace("{guild_name}", guild.getName())
                .replace("{guild_owner}", guild.getOwner().getAsMention())
                .replace("{guild_owner_name}", guild.getOwner().getUser().getName())
                .replace("{guild_owner_discriminator}", guild.getOwner().getUser().getDiscriminator())
                .replace("{guild_owner_nickname}", guild.getOwner().getEffectiveName())
                .replace("{guild_region}", guild.getRegionRaw());
    }

    /**
     * Replaces additional channel placeholders.
     * @param channel The channel.
     * @param input The input.
     * @return A replaced string.
     */
    public static String replaceChannel(TextChannel channel, String input) {
        return input.replace("{channel}", channel.getAsMention())
                .replace("{channel_name}", channel.getName())
                .replace("{channel_topic}", channel.getTopic())
                .replace("{channel_nsfw}", String.valueOf(channel.isNSFW()));
    }

    /**
     * Replaces additional message placeholders.
     * @param message The message.
     * @param input The input.
     * @return A replaced string.
     */
    public static String replaceMessage(Message message, String input) {
        return input.replace("{message_id}", message.getId())
                .replace("{message_time}", message.getCreationTime().toString());
    }

}
