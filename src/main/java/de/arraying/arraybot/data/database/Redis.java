package de.arraying.arraybot.data.database;

import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.core.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;


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
    private Configuration configuration;
    private JedisPool pool;

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
    public Jedis getJedisResource() {
//        System.out.println(">> Asking to get a Jedis pool @  " + System.currentTimeMillis());
        Jedis resource = pool.getResource();
//        System.out.println(">> Got the resource @ " + System.currentTimeMillis());
//        String auth = configuration.getRedisAuth();
//        if(!auth.isEmpty()) {
//            resource.auth(auth);
//        }
//        resource.select(configuration.getRedisIndex());
//        System.out.println(">> Got a Jedis pool @ " + System.currentTimeMillis());
        return resource;
    }

    /**
     * Connects to the Redis server.
     * @param configuration The configuration object.
     * @throws Exception If an error occurs.
     */
    public void connect(Configuration configuration)
            throws Exception {
        if(pool != null) {
            return;
        }
        this.configuration = configuration;
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(500);
        config.setTestOnBorrow(false);
        config.setTestOnCreate(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);
        pool = new JedisPool(config, configuration.getRedisHost(), configuration.getRedisPort(),
                Protocol.DEFAULT_TIMEOUT, configuration.getRedisAuth(), configuration.getRedisIndex(), "Arraybot");
        for(Entry.Category category : Entry.Category.values()) {
            logger.info("Registered the category {} with the type {}.", category, category.getEntry().getType());
            category.getEntry().setCategory(category);
        }
    }

    /**
     * Marks a Jedis instance as finished and ready to be returned to the pool.
     * @param jedis The Jedis instance.
     */
    public void finish(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * Destroys the pool.
     */
    public void destory() {
        pool.destroy();
    }

}
