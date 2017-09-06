package de.arraying.arraybot.startup.startups;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.data.database.categories.*;
import de.arraying.arraybot.data.database.core.Entry;
import de.arraying.arraybot.data.database.templates.KVEntry;
import de.arraying.arraybot.data.database.templates.SetEntry;
import de.arraying.arraybot.startup.StartupTask;

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
public final class StartupRedis extends StartupTask {

    private final Arraybot arraybot = Arraybot.getInstance();

    /**
     * Creates the Redis startup task.
     */
    public StartupRedis() {
        super("Redis-Startup");
    }

    /**
     * Runs the actual startup task.
     * @throws Exception If an error occurs.
     */
    @Override
    public void onTask() throws Exception {
        logger.info("Attempting to start up Redis...");
        Redis redis = Redis.getInstance();
        if(!redis.connect(arraybot.getConfiguration())) {
            return;
        }
        logger.info("Successfully connected to the Redis server.");
        logger.info("Registering all Redis entries...");
        redis.newEntry(new SetEntry(Entry.Category.ANNOUNCEMENT_IDS),
                new AnnouncementEntry(),
                new SetEntry(Entry.Category.BLACKLIST),
                new SetEntry(Entry.Category.CUSTOM_COMMAND_NAMES),
                new CustomCommandEntry(),
                new SetEntry(Entry.Category.DISABLED_COMMAND),
                new SetEntry(Entry.Category.FILTER_IDS),
                new SetEntry(Entry.Category.FILTER),
                new SetEntry(Entry.Category.FILTER_BYPASS_IDS),
                new FilterBypassEntry(),
                new GuildEntry(),
                new SetEntry(Entry.Category.PUNISHMENT_IDS),
                new PunishmentEntry(),
                new KVEntry(Entry.Category.MISC)
        );
        logger.info("All Redis entries have been registered.");
    }

}
