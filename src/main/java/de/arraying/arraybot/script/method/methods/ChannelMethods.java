package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.script.method.templates.EntityCollectionMethods;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.core.entities.TextChannel;

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
@SuppressWarnings("unused")
public final class ChannelMethods extends EntityCollectionMethods<TextChannel> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public ChannelMethods(CommandEnvironment environment) {
        super(environment, "channels");
    }

    /**
     * Gets all roles by name.
     * @param name The name.
     * @param ignoreCase Whether to ignore the case.
     * @return The key.
     */
    @ZeusMethod
    public String channels_find(String name, Boolean ignoreCase) {
        return internalNew(environment.getGuild().getTextChannelsByName(name, ignoreCase));
    }

    /**
     * Gets the size of the roles.
     * @param key The key.
     * @return The number of roles, or -1 if the key is invalid.
     */
    @ZeusMethod
    public Integer channels_length(String key) {
        return length(key);
    }

    /**
     * Gets a role from the collection of roles.
     * @param key The key.
     * @param index The index.
     * @return The role ID or -1 if the key/index is invalid.
     */
    @ZeusMethod
    public Long channels_get(String key, Integer index) {
        return get(key, index);
    }

}
