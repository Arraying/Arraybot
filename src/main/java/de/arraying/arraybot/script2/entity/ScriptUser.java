package de.arraying.arraybot.script2.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script2.abstraction.AbstractMessenger;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed;

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
public final class ScriptUser extends AbstractMessenger {

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

}
