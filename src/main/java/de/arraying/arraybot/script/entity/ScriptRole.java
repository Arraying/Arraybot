package de.arraying.arraybot.script.entity;

import net.dv8tion.jda.api.entities.Role;

import java.awt.*;
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
public final class ScriptRole implements ScriptEntity {

    private final Role underlying;

    /**
     * Creates a new script role.
     * @param underlying The underlying role.
     */
    ScriptRole(Role underlying) {
        this.underlying = underlying;
    }

    /**
     * Gets the ID of the role.
     * @return The ID.
     */
    @Override
    public String getID() {
        return underlying.getId();
    }

    /**
     * Gets the creation time of the role.
     * @return The creation time.
     */
    @Override
    public OffsetDateTime getCreationTime() {
        return underlying.getTimeCreated();
    }

    /**
     * Gets the name of the role.
     * @return The name of the role.
     */
    public String getName() {
        return underlying.getName();
    }

    /**
     * The colour of the role.
     * @return The colour.
     */
    public Color getColor() {
        return underlying.getColor();
    }

    /**
     * The position of the role.
     * @return The position.
     */
    public int getPosition() {
        return underlying.getPosition();
    }

    /**
     * Whether or not the role is hoisted.
     * @return True if it is, false otherwise.
     */
    public boolean isHoisted() {
        return underlying.isHoisted();
    }

    /**
     * Whether or not the role is mentionable.
     * @return True if it is, false otherwise.
     */
    public boolean isMentionable() {
        return underlying.isMentionable();
    }

}
