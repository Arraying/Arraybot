package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.listener.Listener;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.threadding.AbstractTask;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;

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
public final class GuildListener extends PostLoadListener {

    /**
     * Does not need initialization.
     */
    @Override
    public void init() {
    }

    /**
     * When the bot is added to a guild.
     * This may be triggered due to downtime.
     * @param event The event.
     */
    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        update(event.getJDA());
    }

    /**
     * When the bot is removed from a guild.
     * @param event The event.
     */
    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        new Remover(event.getGuild().getIdLong()).create();
        update(event.getJDA());
    }

    /**
     * Updates the server count on the bot lists.
     * @param shard The shard.
     */
    private void update(JDA shard) {
        new Listener.Updater(shard).create();
    }

    private class Remover extends AbstractTask {

        private long id;

        /**
         * Creates a new guild remover.
         * @param id The ID of the guild.
         */
        private Remover(long id) {
            super("Guild-Remover");
            this.id = id;
        }


        /**
         * Removes the guild.
         */
        @Override
        public void onExecution() {
            Redis.getInstance().purge(id);
        }

    }

}
