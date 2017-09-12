package de.arraying.arraybot.data.database;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.core.Entry;
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
        //RedisClient client = RedisClient.create(String.format("redis://%s@%s:%s/0", configuration.getRedisAuth(), configuration.getRedisHost(), configuration.getRedisPort() + ""));
        StatefulRedisConnection<String, String> connection = client.connect();
        sync = connection.sync();
        for(Entry.Category category : Entry.Category.values()) {
            logger.info("Registered the category {} with the type {}.", category, category.getEntry().getType());
            category.getEntry().setCategory(category);
        }
    }

}
