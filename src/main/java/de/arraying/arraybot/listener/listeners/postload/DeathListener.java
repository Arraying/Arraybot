package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.listener.Listener;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.manager.BotManager;
import de.arraying.arraybot.shard.ShardEntry;
import de.arraying.arraybot.util.UShard;
import net.dv8tion.jda.core.events.Event;

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
public final class DeathListener extends PostLoadListener {

    private BotManager manager;

    /**
     * Initializes the bot manager.
     */
    @Override
    public void init() {
        manager = Arraybot.getInstance().getBotManager();
    }

    /**
     * When any event occurs.
     * @param event The event.
     */
    @Override
    public void onGenericEvent(Event event) {
        int shardId = UShard.getShardId(event.getJDA());
        if(manager == null) {
            return;
        }
        ShardEntry shard = manager.getShards().get(shardId);
        if(shard == null) {
            Listener.LISTENER_LOGGER.error("Received event for nonexistent shard ({}).", shardId);
            return;
        }
        shard.onEvent(System.currentTimeMillis());
    }

}
