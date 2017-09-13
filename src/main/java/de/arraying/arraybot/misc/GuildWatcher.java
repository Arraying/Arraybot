package de.arraying.arraybot.misc;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.manager.BotManager;
import de.arraying.arraybot.shard.ShardEntry;

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
public final class GuildWatcher extends AbstractWatcher {

    private final BotManager manager;
    private int guilds;

    /**
     * Creates a new guild watcher.
     */
    public GuildWatcher() {
        super("Guild-Watcher", 360000);
        manager = Arraybot.getInstance().getBotManager();
        guilds = getGuildsSize();
    }

    /**
     * Executes the task.
     */
    @Override
    public void onTask() {
        int updatedSize = getGuildsSize();
        if(guilds != updatedSize) {
            guilds = updatedSize;
            postUpdate();
        }
    }

    /**
     * Gets the size of all guilds.
     * @return The size.
     */
    private int getGuildsSize() {
        int guilds = 0;
        for(ShardEntry entry : manager.getShards().values()) {
            guilds += entry.getJDA().getGuilds().size();
        }
        return guilds;
    }

    /**
     * Posts an updated guild count to the bot lists.
     */
    private void postUpdate() {
        //TODO Actually post the updates.
    }

}
