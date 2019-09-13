package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.listener.Listener;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.threadding.impl.AnnouncementsTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;

import java.io.IOException;

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
        new Remover(event.getGuild().getIdLong(), false).create();
        update(event.getJDA());
    }

    /**
     * Updates the server count on the bot lists.
     * @param shard The shard.
     */
    private void update(JDA shard) {
        new Listener.Updater(shard).create();
    }

    public static class Remover extends AbstractTask {

        private final int delay = 120000;
        private long id;

        /**
         * Creates a new guild remover.
         * @param id The ID of the guild.
         */
        public Remover(long id, boolean start) {
            super("Guild-Remover");
            this.id = id;
            if(!start) {
                try {
                    Arraybot.INSTANCE.getFileManager().addToRemovalQueue(id);
                } catch(IOException exception) {
                    logger.error("An error occurred adding to the removal queue.", exception);
                }
            }
        }


        /**
         * Removes the guild.
         */
        @Override
        public void onExecution() {
            try {
                Thread.sleep(delay);
            } catch(InterruptedException exception) {
                logger.error("Encountered interrupted exception, ", exception);
            }
            if(!Arraybot.INSTANCE.getBotManager().isGuild(id)) {
                Redis.INSTANCE.purge(id);
                AnnouncementsTask.stopTask(id);
            }
            try {
                Arraybot.INSTANCE.getFileManager().removeFromRemovalQueue(id);
            } catch(IOException exception) {
                logger.error("An error occurred removing from the removal queue.", exception);
            }
        }

    }

}
