package de.arraying.arraybot.listener;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.listener.listeners.postload.GuildListener;
import de.arraying.arraybot.listener.listeners.postload.MemberListener;
import de.arraying.arraybot.listener.listeners.postload.MessageListener;
import de.arraying.arraybot.listener.listeners.postload.PunishmentListener;
import de.arraying.arraybot.request.BotListRequest;
import de.arraying.arraybot.threadding.AbstractTask;
import net.dv8tion.jda.core.JDA;

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
public final class Listener {

    /**
     * An array of all listeners that will be registered after the shard has loaded.
     */
    public static final PostLoadListener[] POST_LOAD_LISTENERS = { new GuildListener(), new MemberListener(), new MessageListener(), new PunishmentListener() };

    /**
     * The updater class that will handle updates.
     */
    public static final class Updater extends AbstractTask {

        private final JDA shard;

        /**
         * Creates a new guild count updater.
         * @param shard The shard that needs updating.
         */
        public Updater(JDA shard) {
            super("Guild-Updater");
            this.shard = shard;
        }

        /**
         * Updates the server counts.
         */
        @Override
        public void onExecution() {
            BotListRequest[] requests = Arraybot.INSTANCE.getConfiguration().getRequests();
            if(requests == null) {
                logger.warn("Could not POST an updated guild count due to there being no endpoints.");
                return;
            }
            for(BotListRequest request : requests) {
                request.send(shard);
            }
        }

    }

}
