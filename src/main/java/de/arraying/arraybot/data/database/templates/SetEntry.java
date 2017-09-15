package de.arraying.arraybot.data.database.templates;

import com.lambdaworks.redis.api.sync.RedisCommands;
import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.data.database.core.Entry;
import de.arraying.arraybot.util.UDatabase;

import java.util.Set;

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
@SuppressWarnings("unchecked")
public final class SetEntry implements Entry {

    private final Redis redis;
    private Category category;

    /**
     * Creates a new set entry.
     */
    public SetEntry() {
        this.redis = Redis.getInstance();
    }

    /**
     * Gets the entry type.
     * @return The type.
     */
    @Override
    public Type getType() {
        return Type.SET;
    }

    /**
     * Sets the category.
     * @param category The category.
     */
    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Deletes everything corresponding to the ID.
     * @param id The ID.
     */
    @Override
    public void delete(long id) {
        RedisCommands resource = redis.getResource();
        resource.del(UDatabase.getKey(category, id));
    }

    /**
     * Gets the entries of the set.
     * @param id The snowflake identifier ID.
     * @return A set of entries. Cannot be null.
     */
    public Set<String> values(long id) {
        RedisCommands resource = redis.getResource();
        return resource.smembers(UDatabase.getKey(category, id));
    }

    /**
     * Adds a member to the set.
     * @param id The snowflake identifier ID.
     * @param entry The entry. Cannot be null.
     */
    public void add(long id, Object entry) {
        RedisCommands resource = redis.getResource();
        resource.sadd(UDatabase.getKey(category, id), entry.toString());
    }

    /**
     * Removes an entry from the set,
     * @param id The snowflake identifier ID.
     * @param entry The entry. Cannot be null.
     */
    public void remove(long id, Object entry) {
        RedisCommands resource = redis.getResource();
        resource.srem(UDatabase.getKey(category, id), entry.toString());
    }

}
