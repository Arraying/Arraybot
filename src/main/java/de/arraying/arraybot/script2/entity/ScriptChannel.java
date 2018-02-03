package de.arraying.arraybot.script2.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script2.abstraction.AbstractMessenger;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.time.OffsetDateTime;

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
public final class ScriptChannel extends AbstractMessenger implements ScriptEntity {

    private final TextChannel underlying;

    /**
     * Creates a new script channel.
     * @param environment The command environment.
     * @param underlying The underlying text channel.
     */
    public ScriptChannel(CommandEnvironment environment, TextChannel underlying) {
        super(environment, false);
        this.underlying = underlying;
    }

    /**
     * Gets the ID of the channel.
     * @return The ID.
     */
    @Override
    public long getID() {
        return underlying.getIdLong();
    }

    /**
     * Gets the creation time of the channel.
     * @return The creation time.
     */
    @Override
    public OffsetDateTime getCreationTime() {
        return underlying.getCreationTime();
    }

    /**
     * Sends a message to the channel.
     * @param message The message.
     * @throws Exception If an error occurs.
     */
    @Override
    protected void abstractMessage(String message)
            throws Exception {
        underlying.sendMessage(message).queue();
    }

    /**
     * Sends a message to the channel.
     * @param embed The embed.
     * @throws Exception If an error occurs.
     */
    @Override
    protected void abstractEmbed(MessageEmbed embed)
            throws Exception {
        underlying.sendMessage(embed).queue();
    }
}
