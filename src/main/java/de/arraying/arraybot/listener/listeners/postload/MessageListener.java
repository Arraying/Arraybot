package de.arraying.arraybot.listener.listeners.postload;

import de.arraying.arraybot.command.Commands;
import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.data.database.Redis;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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
public final class MessageListener extends PostLoadListener {

    /**
     * Does not need initialization.
     */
    @Override
    public void init() {}

    /**
     * When a message occurs.
     * Used to log the amount of messages.
     * @param event The event.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Redis.getInstance().getRedis().incr("messages");
    }

    /**
     * When a guild message occurs.
     * Used to invoke the command executor.
     * @param event The event.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(event != null) {
            Commands.INSTANCE.executeCommand(new CommandEnvironment(event.getMessage()));
        }
    }

}
