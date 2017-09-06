package de.arraying.arraybot.data.database;

import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.core.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.LinkedHashMap;


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
public final class Redis {

    private static Redis instance;
    private static final Object mutex = new Object();
    private final LinkedHashMap<Entry.Category, Entry> categories = new LinkedHashMap<>();
    private final Logger logger = LoggerFactory.getLogger("Redis");
    private Jedis redis;

    /**
     * Private constructor to prevent initialization.
     */
    private Redis() {}

    /**
     * The singleton getter. Thread safe.
     * @return The singleton instance.
     */
    public static Redis getInstance() {
        if(instance == null) {
            synchronized(mutex) {
                instance = new Redis();
            }
        }
        return instance;
    }

    /**
     * Gets the Jedis object.
     * @return The Jedis object.
     */
    public Jedis getRedis() {
        return redis;
    }

    /**
     * Connects to the Redis server.
     * @param configuration The configuration object.
     * @return True if successful, false otherwise.
     */
    public boolean connect(Configuration configuration) {
        if(redis != null) {
            return false;
        }
        try {
            redis = new Jedis(configuration.getRedisHost(), configuration.getRedisPort());
            String auth = configuration.getRedisAuth();
            if(!auth.isEmpty()) {
                redis.auth(auth);
            }
            redis.select(configuration.getRedisIndex());
        } catch(Exception exception) {
            logger.error("An error occurred connecting to the Redis server:  {}.", exception.toString());
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Registers a new database in the database collection.
     * @param entries An array of entries.
     */
    public void newEntry(Entry... entries) {
        for(Entry entry : entries) {
            categories.put(entry.getCategory(), entry);
            logger.info("Registered the Redis entry {} using the {} type.", entry.getCategory(), entry.getType());
        }
    }

    /**
     * Gets a database by category.
     * @param category The category.
     * @return A valid database, or null if the category is not registered.
     */
    public Entry getEntry(Entry.Category category) {
        return categories.get(category);
    }

}
