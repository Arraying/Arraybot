package de.arraying.arraybot.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class MultiKeyMap<K, V> extends ConcurrentHashMap<K, V> {

    private final Map<K, K> keys = new ConcurrentHashMap<>();

    /**
     * Adds a new entry to the map.
     * @param key The primary key. This key must be unique.
     * @param value The value to add.
     * @param aliases An optional array of aliases. These must also be unique, or they may be overridden.
     */
    public void add(V value, K key,  K[] aliases) {
        put(key, value);
        for(K alias : aliases) {
            keys.put(alias, key);
        }
    }

    /**
     * Gets an entry from the map.
     * @param key The key to use. This can be an alias.
     * @return The value, or null if it does not exist.
     */
    public V getByKeyOrAlias(K key) {
        V value = get(key);
        if(value == null
                && keys.containsKey(key)) {
            value = get(keys.get(key));
        }
        return value;
    }

}
