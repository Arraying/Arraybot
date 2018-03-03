package de.arraying.arraybot.script.abstraction;

import de.arraying.arraybot.script.entity.ScriptEntity;
import net.dv8tion.jda.core.entities.Channel;

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

    private final Channel underlying;

    /**
     * Creates a new abstract channel.
     * @param underlying The underlying channel.
     */
    protected AbstractChannel(Channel underlying) {
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
        return underlying.getCreationTime();
    }

    /**
     * Creates an invite.
     * @return The invite code.
     * @throws Exception If an error occurs.
     */
    public String createInvite()
            throws Exception {
        return underlying.createInvite().complete().getURL();
    }

    /**
     * Deletes the channel.
     * @throws Exception If an error occurs.
     */
    public void delete()
            throws Exception {
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
     * @throws Exception If an error occurs.
     */
    public void setBitrate(int bitrate)
            throws Exception {
        underlying.getManager().setBitrate(bitrate).queue();
    }

    /**
     * Sets the name.
     * @param name The name.
     * @throws Exception If an error occurs.
     */
    public void setName(String name)
            throws Exception {
        underlying.getManager().setName(name).queue();
    }

    /**
     * Sets the channel NSFW property.
     * @param nsfw The property.
     * @throws Exception If an error occurs.
     */
    public void setNSFW(boolean nsfw)
            throws Exception {
        underlying.getManager().setNSFW(nsfw).queue();
    }

    /**
     * Sets the channel position.
     * @param position The position.
     * @throws Exception If an error occurs.
     */
    public void setPosition(int position)
            throws Exception {
        underlying.getManager().setPosition(position).queue();
    }

    /**
     * Sets the channel topic.
     * @param topic The topic.
     * @throws Exception If an error occurs.
     */
    public void setTopic(String topic)
            throws Exception {
        underlying.getManager().setTopic(topic).queue();
    }

    /**
     * Sets the user limit.
     * @param userLimit The user limit.
     * @throws Exception If an error occurs.
     */
    public void setUserLimit(int userLimit)
            throws Exception {
        underlying.getManager().setUserLimit(userLimit).queue();
    }

}
