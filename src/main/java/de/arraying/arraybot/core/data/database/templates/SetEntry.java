package de.arraying.arraybot.core.data.database.templates;

import de.arraying.arraybot.core.data.database.Redis;
import de.arraying.arraybot.core.data.database.core.Entry;
import de.arraying.arraybot.util.UDatabase;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashSet;
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
public final class SetEntry implements Entry {

    private final Redis redis;
    @Getter
    private final Category category;

    /**
     * Creates a new set entry.
     * @param category The category.
     */
    public SetEntry(Category category) {
        this.category = category;
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
     * Gets the entries of the set.
     * @param id The snowflake identifier ID.
     * @return A set of entries. Cannot be null.
     */
    public Set<String> values(long id) {
        Set<String> result = redis.getRedis().smembers(UDatabase.getKey(category, id));
        return result == null ? new HashSet<>() : result;
    }

    /**
     * Adds a member to the set.
     * @param id The snowflake identifier ID.
     * @param entry The entry. Cannot be null.
     */
    public void add(long id, @NonNull Object entry) {
        redis.getRedis().sadd(UDatabase.getKey(category, id), entry.toString());
    }

    /**
     * Removes an entry from the set,
     * @param id The snowflake identifier ID.
     * @param entry The entry. Cannot be null.
     */
    public void remove(long id, @NonNull Object entry) {
        redis.getRedis().srem(UDatabase.getKey(category, id), entry.toString());
    }

}
