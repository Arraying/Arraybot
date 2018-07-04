package de.arraying.arraybot.script.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.util.UUser;
import net.dv8tion.jda.core.entities.*;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2018 Arraying
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
@SuppressWarnings("unused")
public final class ScriptGuild implements ScriptEntity {

    private final CommandEnvironment environment;
    private final Guild underlying;
    private final Map<String, ScriptTextChannel> textChannels = new HashMap<>();
    private final Map<String, ScriptVoiceChannel> voiceChannels = new HashMap<>();
    private final Map<String, ScriptUser> users = new HashMap<>();
    private final Map<String, ScriptRole> roles = new HashMap<>();

    /**
     * Creates a new script guild.
     * This caches entities to ensure that there can only be one ScriptX entity instance.
     * @param environment The command environment.
     * @param underlying The underlying guild.
     */
    public ScriptGuild(CommandEnvironment environment, Guild underlying) {
        this.environment = environment;
        this.underlying = underlying;
        for(TextChannel channel : underlying.getTextChannels()) {
            textChannels.put(channel.getId(), new ScriptTextChannel(environment, channel));
        }
        for(VoiceChannel channel : underlying.getVoiceChannels()) {
            voiceChannels.put(channel.getId(), new ScriptVoiceChannel(channel));
        }
        for(Member user : underlying.getMembers()) {
            users.put(user.getUser().getId(), new ScriptUser(environment, user));
        }
        for(Role role : underlying.getRoles()) {
            roles.put(role.getId(), new ScriptRole(role));
        }
    }

    /**
     * Gets the ID of the guild.
     * @return The ID.
     */
    @Override
    public String getID() {
        return underlying.getId();
    }

    /**
     * Gets the creation time of the guild.
     * @return The creation time.
     */
    @Override
    public OffsetDateTime getCreationTime() {
        return underlying.getCreationTime();
    }

    /**
     * Gets the name of the guild.
     * @return The name.
     */
    public String getName() {
        return underlying.getName();
    }

    /**
     * Gets the guild icon.
     * @return The icon.
     */
    public String getIcon() {
        return underlying.getIconUrl();
    }

    /**
     * Gets the guild owner.
     * @return The owner.
     */
    public ScriptUser getOwner() {
        return new ScriptUser(environment, underlying.getOwner());
    }

    /**
     * Gets all text channels.
     * @return A collection of text channels.
     */
    public Collection<ScriptTextChannel> getTextChannels() {
        return Collections.unmodifiableCollection(textChannels.values());
    }

    /**
     * Gets a text channel via ID.
     * @param id The ID.
     * @return The channel, or null if it does not exist.
     */
    public ScriptTextChannel getTextChannel(String id) {
        return textChannels.get(id);
    }

    /**
     * Gets all voice channels.
     * @return A collection of voice channels.
     */
    public Collection<ScriptVoiceChannel> getVoiceChannels() {
        return Collections.unmodifiableCollection(voiceChannels.values());
    }

    /**
     * Gets a voice channel via ID.
     * @param id The ID.
     * @return The channel, or null if it does not exist.
     */
    public ScriptVoiceChannel getVoiceChannel(String id) {
        return voiceChannels.get(id);
    }

    /**
     * Gets all users.
     * @return A collection of users.
     */
    public Collection<ScriptUser> getUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    /**
     * Gets a user via ID, tag or mention.
     * @param input The ID, tag or mention.
     * @return The user, or null if they do not exist.
     */
    public ScriptUser getUser(String input) {
        Member member = UUser.getMember(environment.getGuild(), input);
        return member == null ? null : users.get(member.getUser().getId());
    }

    /**
     * Gets all roles.
     * @return A collection of roles.
     */
    public Collection<ScriptRole> getRoles() {
        return Collections.unmodifiableCollection(roles.values());
    }

    /**
     * Gets a role via ID.
     * @param id The ID.
     * @return The role, or null if it does not exist.
     */
    public ScriptRole getRole(String id) {
        return roles.get(id);
    }

}
