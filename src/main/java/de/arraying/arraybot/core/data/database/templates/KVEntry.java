package de.arraying.arraybot.core.data.database.templates;

import de.arraying.arraybot.core.data.database.Redis;
import de.arraying.arraybot.core.data.database.core.Entry;
import de.arraying.arraybot.util.UDatabase;
import lombok.Getter;
import lombok.NonNull;

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
public final class KVEntry implements Entry {

    private final Redis redis;
    @Getter
    private final Category category;

    /**
     * Creates a new key/value entry.
     * @param category The category.
     */
    public KVEntry(Category category) {
        this.category = category;
        this.redis = Redis.getInstance();
    }

    /**
     * Gets the entry type.
     * @return The type.
     */
    @Override
    public Type getType() {
        return Type.KV;
    }

    /**
     * Gets a value from Redis.
     * @param key The key.
     * @return The value, can be null.
     */
    public String get(@NonNull String key) {
        return redis.getRedis().get(UDatabase.getKey(category, key));
    }

    /**
     * Sets a value in Redis.
     * @param key The key.
     * @param value The value. Cannot be null.
     */
    public void set(String key, @NonNull Object value) {
        redis.getRedis().set(UDatabase.getKey(category, key), value.toString());
    }

}
