package de.arraying.arraybot.script.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.abstraction.AbstractMessenger;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
@SuppressWarnings({"unused", "RedundantThrows"})
public final class ScriptUser extends AbstractMessenger implements ScriptEntity {

    private final Member underlying;

    /**
     * Creates a new script user.
     * @param environment The command environment.
     * @param underlying The underlying member.
     */
    public ScriptUser(CommandEnvironment environment, Member underlying) {
        super(environment, true);
        this.underlying = underlying;
    }

    /**
     * Sends a message.
     * @param message The message.
     * @throws Exception If an error occurs.
     */
    @Override
    protected void abstractMessage(String message)
            throws Exception {
        underlying.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(message).queue());
    }

    /**
     * Sends a message.
     * @param embed The embed.
     * @throws Exception If an error occurs.
     */
    @Override
    protected void abstractEmbed(MessageEmbed embed)
            throws Exception {
        underlying.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(embed).queue());
    }

    /**
     * Gets the ID of the user.
     * @return The ID.
     */
    @Override
    public String getID() {
        return underlying.getUser().getId();
    }

    /**
     * Gets the creation time of the user.
     * @return The creation time.
     */
    @Override
    public OffsetDateTime getCreationTime() {
        return underlying.getUser().getTimeCreated();
    }

    /**
     * Gets the user's avatar URL.
     * @return The avatar URL.
     */
    public String getAvatar() {
        return underlying.getUser().getEffectiveAvatarUrl();
    }

    /**
     * Gets the name of the user.
     * @return The name.
     */
    public String getName() {
        return underlying.getUser().getName();
    }

    /**
     * Gets the nickname of the user.
     * @return The nickname.
     */
    public String getNickname() {
        return underlying.getNickname();
    }

    /**
     * Gets the roles of the user.
     * @return An array of roles.
     */
    public ScriptRole[] getRoles() {
        List<ScriptRole> roles = new ArrayList<>();
        for(Role jdaRole : underlying.getRoles()) {
            roles.add(new ScriptRole(jdaRole));
        }
        return roles.toArray(new ScriptRole[0]);
    }

}
