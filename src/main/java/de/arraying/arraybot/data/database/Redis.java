package de.arraying.arraybot.data.database;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.core.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
    private final Logger logger = LoggerFactory.getLogger("Redis");
    private RedisCommands sync;

    /**
     * Private constructor to prevent initialization.
     */
    private Redis() {
    }

    /**
     * The singleton getter. Thread safe.
     * @return The singleton instance.
     */
    public static Redis getInstance() {
        if(instance == null) {
            synchronized(mutex) {
                if(instance == null) {
                    instance = new Redis();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the Jedis resource from the pool.
     * This resource needs to be closed using Jedis#close upon completion.
     * @return The Jedis object.
     */
    public RedisCommands getResource() {
        return sync;
    }

    /**
     * Connects to the Redis server.
     * @param configuration The configuration object.
     * @throws Exception If an error occurs.
     */
    public void connect(Configuration configuration) throws Exception {
        if(sync != null) {
            return;
        }
        RedisURI.Builder uri = RedisURI.Builder.redis(configuration.getRedisHost(), configuration.getRedisPort())
                .withDatabase(configuration.getRedisIndex());
        if(!configuration.getRedisAuth().isEmpty()) {
            uri.withPassword(configuration.getRedisAuth());
        }
        RedisClient client = RedisClient.create(uri.build());
        StatefulRedisConnection<String, String> connection = client.connect();
        sync = connection.sync();
        for(Category category : Category.values()) {
            logger.info("Registered the category {} with the type {}.", category, category.getEntry().getType());
            category.getEntry().setCategory(category);
        }
    }

    /**
     * Purges all information related to the guild with that ID.
     * This is not a test. This is your emergency broadcast system announcing the commencement of the Annual Purge sanctioned by the U.S. Government.
     * Weapons of class 4 and lower have been authorized for use during the Purge.
     * All other weapons are restricted.
     * Government officials of ranking 10 have been granted immunity from the Purge and shall not be harmed.
     * Commencing at the siren, any and all crime, including murder, will be legal for 12 continuous hours. Police, fire, and emergency medical services will be unavailable until tomorrow morning until 7 a.m., when The Purge concludes.
     * Blessed be our New Founding Fathers and America, a nation reborn.
     * May God be with you all.
     * @param id The ID.
     */
    public void purge(long id) {
        for(Category category : Category.values()) {
            category.getEntry().delete(id);
        }
    }

}
