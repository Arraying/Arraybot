package de.arraying.arraybot.data.database.templates;

import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.data.database.core.Entry;
import de.arraying.arraybot.data.database.core.EntryField;
import de.arraying.arraybot.util.UDatabase;

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
public abstract class HashEntry<T> implements Entry {

    private final Redis redis;
    private final Category category;

    /**
     * Creates a new hash entry.
     * @param category The category.
     */
    public HashEntry(Category category) {
        this.redis = Redis.getInstance();
        this.category = category;
    }

    /**
     * Gets a entry field via key.
     * @param key The key.
     * @return The entry field.
     */
    public abstract EntryField getField(T key);

    /**
     * Gets the entry type.
     * @return The type.
     */
    @Override
    public Type getType() {
        return Type.HASH;
    }

    /**
     * Gets a hash value.
     * @param field The field.
     * @param id The primary key.
     * @param secondaryKey The secondary key.
     * @return The value. It is never null.
     */
    public String fetch(EntryField field, long id, Object secondaryKey) {
        String result = redis.getRedis().hget(UDatabase.getKey(category, id, secondaryKey), field.getRedisKey());
        return result == null ? setDefault(field, id, secondaryKey) : result;
    }

    /**
     * Sets a hash value.
     * @param field The field.
     * @param id The primary key.
     * @param secondaryKey The secondary key.
     * @param value The value.
     */
    public void push(EntryField field, long id, Object secondaryKey, Object value) {
        redis.getRedis().hset(UDatabase.getKey(category, id, secondaryKey), field.getRedisKey(), value.toString());
    }

    /**
     * Sets a field to its default value.
     * @param field The field.
     * @param id The primary key.
     * @param secondaryKey The secondary key.
     * @return The value.
     */
    private String setDefault(EntryField field, long id, Object secondaryKey) {
        if(field == null) {
            throw new IllegalArgumentException("The provided field is null.");
        }
        push(field, id, secondaryKey, field.getDefaultValue());
        return field.getDefaultValue().toString();
    }

    /**
     * Gets the category.
     * @return The category.
     */
    @Override
    public Category getCategory() {
        return category;
    }

}
