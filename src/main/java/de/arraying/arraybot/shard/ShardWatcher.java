package de.arraying.arraybot.shard;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.manager.BotManager;
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
public final class ShardWatcher implements Runnable {

    private final Logger logger = LoggerFactory.getLogger("Shard-Watcher");
    private final Thread current;
    private final int waitDuration;
    private final long maxInactivityThreshold;

    /**
     * Creates a new shard watcher.
     * @param waitDuration The duration between death checks, in ms.
     */
    public ShardWatcher(int waitDuration, long maxInactivityThreshold) {
        Thread thread = new Thread(this);
        thread.setName(logger.getName());
        thread.start();
        current = thread;
        this.waitDuration = waitDuration;
        this.maxInactivityThreshold = maxInactivityThreshold;
    }

    /**
     * Runs the watcher.
     */
    @Override
    public void run() {
        logger.info("Starting shard monitoring...");
        while(!current.isInterrupted()) {
            try {
                Thread.sleep(waitDuration);
                BotManager manager = Arraybot.getInstance().getBotManager();
                for(ShardEntry shard : manager.getShards().values()) {
                    long difference = System.currentTimeMillis() - shard.getLastEvent();
                    if(difference > maxInactivityThreshold) {
                        logger.info("The shard {} is suspected to be dead. Executing a restart.", shard.getId());
                        if(!manager.restartShard(shard.getId())) {
                            logger.error("Failed the restart of shard {}.", shard.getId());
                        }
                    }
                    long ping = shard.getJDA().getPing();
                    if(ping > 500) {
                        logger.info("The shard {} is has a high ping ({}), may be slow/unresponsive.", shard.getId(), ping);
                    } else {
                        logger.info("The shard {} is running healthily with a ping of {}.", shard.getId(), ping);
                    }
                }
            } catch(InterruptedException exception) {
                logger.info("Hit an interrupted exception.");
                exception.printStackTrace();
            }
        }
    }

}
