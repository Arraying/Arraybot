package de.arraying.arraybot.script.method.templates;

import de.arraying.arraybot.command.CommandEnvironment;
import net.dv8tion.jda.core.entities.ISnowflake;

import java.util.List;

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
public class EntityCollectionMethods<T extends ISnowflake> extends CollectionManagementMethods<List<T>> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     * @param identifier A general identifier category.
     */
    public EntityCollectionMethods(CommandEnvironment environment, String identifier) {
        super(environment, identifier);
    }

    /**
     * Gets the size of all entries.
     * @param key The key.
     * @return The size or -1 if the key is invalid.
     */
    protected Integer length(String key) {
        List<T> list = collection.get(key);
        return list == null ? -1 : list.size();
    }

    /**
     * Gets an entry from the collection.
     * @param key The key.
     * @param index The index.
     * @return The entry's ID, or -1 if the key/index is invalid.
     */
    protected Long get(String key, Integer index) {
        List<T> list = collection.get(key);
        if(list == null
                || index < 0) {
            return -1L;
        }
        T entity = collection.get(key).get(index);
        return entity == null ? -1 : entity.getIdLong();
    }

}
