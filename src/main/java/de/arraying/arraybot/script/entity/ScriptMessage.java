package de.arraying.arraybot.script.entity;

import de.arraying.arraybot.command.CommandEnvironment;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.time.OffsetDateTime;
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
@SuppressWarnings("unused")
public final class ScriptMessage {

    private final CommandEnvironment environment;
    private final Message underlying;

    /**
     * Creates a new script message.
     * @param environment The command environment.
     * @param underlying The underlying message.
     */
    public ScriptMessage(CommandEnvironment environment, Message underlying) {
        this.environment = environment;
        this.underlying = underlying;
    }

    public String getId() {
        return underlying.getId();
    }

    /**
     * Gets the user.
     * @return The message user.
     */
    public ScriptUser getUser() {
        return new ScriptUser(environment, underlying.getMember());
    }

    /**
     * Gets the text channel.
     * @return The channel.
     */
    public ScriptTextChannel getChannel() {
        return new ScriptTextChannel(environment, underlying.getTextChannel());
    }

    /**
     * Gets the content of the message.
     * @return The raw content.
     */
    public String getContent() {
        return underlying.getContentRaw();
    }

    public OffsetDateTime getEditTime() {
        return underlying.getEditedTime();
    }

    /**
     * Gets the embeds of a message.
     * @return The embeds.
     */
    public List<MessageEmbed> getEmbeds() {
        return underlying.getEmbeds();
    }

    /**
     * Toggles the pin for the message.
     * @return True if the message has been pinned.
     */
    public boolean pin(){
        if(underlying.isPinned()) {
            underlying.unpin().queue();
        } else {
            underlying.pin().queue();
        }
        return underlying.isPinned();
    }

}
