package de.arraying.arraybot.listeners

import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.commands.other.CommandEnvironment
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class ListenerCommand:
        ListenerAdapter() {

    /**
     * When a message is sent.
     */
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent?) {
        if(event == null
                || !event.guild.isAvailable) {
            return
        }
        launch(CommonPool) {
            Commands.executeCommand(CommandEnvironment(event.message))
            Cache.messagesSent++
        }
    }

    /**
     * When a message is updated.
     */
    override fun onGuildMessageUpdate(event: GuildMessageUpdateEvent?) {
        if(event == null
                || !event.guild.isAvailable) {
            return
        }
        launch(CommonPool) {
            Commands.executeCommand(CommandEnvironment(event.message))
        }
    }

}