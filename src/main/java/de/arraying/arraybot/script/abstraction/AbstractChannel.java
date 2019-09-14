package de.arraying.arraybot.script.abstraction;

import de.arraying.arraybot.script.entity.ScriptEntity;
import net.dv8tion.jda.api.entities.GuildChannel;

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
@SuppressWarnings("unused")
public abstract class AbstractChannel implements ScriptEntity {

    private final GuildChannel underlying;

    /**
     * Creates a new abstract channel.
     * @param underlying The underlying channel.
     */
    protected AbstractChannel(GuildChannel underlying) {
        this.underlying = underlying;
    }

    /**
     * Gets the ID.
     * @return The ID.
     */
    @Override
    public String getID() {
        return underlying.getId();
    }

    /**
     * Gets the creation time.
     * @return The creation time.
     */
    @Override
    public OffsetDateTime getCreationTime() {
        return underlying.getTimeCreated();
    }

    /**
     * Creates an invite.
     * @return The invite code.
     */
    public String createInvite() {
        return underlying.createInvite().complete().getUrl();
    }

    /**
     * Deletes the channel.
     */
    public void delete() {
        underlying.delete().queue();
    }

    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        return underlying.getName();
    }

    /**
     * Gets the position.
     * @return The position.
     */
    public int getPosition() {
        return underlying.getPosition();
    }

    /**
     * Sets the bitrate.
     * @param bitrate The bitrate.
     */
    public void setBitrate(int bitrate) {
        underlying.getManager().setBitrate(bitrate).queue();
    }

    /**
     * Sets the name.
     * @param name The name.
     */
    public void setName(String name) {
        underlying.getManager().setName(name).queue();
    }

    /**
     * Sets the channel NSFW property.
     * @param nsfw The property.
     */
    public void setNSFW(boolean nsfw) {
        underlying.getManager().setNSFW(nsfw).queue();
    }

    /**
     * Sets the channel position.
     * @param position The position.
     */
    public void setPosition(int position) {
        underlying.getManager().setPosition(position).queue();
    }

    /**
     * Sets the channel topic.
     * @param topic The topic.
     */
    public void setTopic(String topic) {
        underlying.getManager().setTopic(topic).queue();
    }

    /**
     * Sets the user limit.
     * @param userLimit The user limit.
     */
    public void setUserLimit(int userLimit) {
        underlying.getManager().setUserLimit(userLimit).queue();
    }

}
