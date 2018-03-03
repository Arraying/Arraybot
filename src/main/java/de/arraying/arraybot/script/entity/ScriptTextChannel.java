package de.arraying.arraybot.script.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.abstraction.AbstractChannel;
import de.arraying.arraybot.script.abstraction.AbstractMessenger;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

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
public final class ScriptTextChannel extends AbstractChannel implements ScriptEntity {

    private final TextChannel underlying;
    private final AbstractMessenger messenger;

    /**
     * Creates a new script channel.
     * @param environment The command environment.
     * @param underlying The underlying text channel.
     */
    public ScriptTextChannel(CommandEnvironment environment, TextChannel underlying) {
        super(underlying);
        this.underlying = underlying;
        this.messenger = new AbstractMessenger(environment, false) {
            @Override
            protected void abstractMessage(String message) throws Exception { underlying.sendMessage(message).queue(); }
            @Override
            protected void abstractEmbed(MessageEmbed embed) throws Exception { underlying.sendMessage(embed).queue(); }
        };
    }

    /**
     * Gets the ID of the channel.
     * @return The ID.
     */
    @Override
    public String getID() {
        return underlying.getId();
    }

    /**
     * Sends a message.
     * @param message The message.
     * @throws Exception The exception.
     */
    public void message(String message)
            throws Exception {
        messenger.message(message);
    }

    /**
     * Sends a message.
     * @param embed The embed.
     * @throws Exception The exception.
     */
    public void message(MessageEmbed embed)
            throws Exception {
        System.out.println("owO");
        messenger.message(embed);
    }

}
