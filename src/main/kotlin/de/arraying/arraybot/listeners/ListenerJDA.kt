package de.arraying.arraybot.listeners

import de.arraying.arraybot.Arraybot
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.DisconnectEvent
import net.dv8tion.jda.core.events.ExceptionEvent
import net.dv8tion.jda.core.events.StatusChangeEvent
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
class ListenerJDA:
        ListenerAdapter() {

    private val arraybot = Arraybot.instance

    /**
     * When an error occurs and the WS gets disconnected.
     */
    override fun onDisconnect(event: DisconnectEvent?) {
        if(event == null) {
            arraybot.logger.fatal("Disconnected event is null.")
            return
        }
        if(event.isClosedByServer) {
            arraybot.logger.warn("Disconnected by the websocket (server). Close frame code: ${event.serviceCloseFrame.closeCode}, close code: ${event.closeCode}.")
        } else {
            arraybot.logger.warn("Disconnected by the websocket (client). Close frame code: ${event.clientCloseFrame.closeCode}, close code: ${event.closeCode}.")
        }
    }

    /**
     * When an error occurs.
     */
    override fun onException(event: ExceptionEvent?) {
        if(event == null) {
            arraybot.logger.warn("Exception event is null.")
            return
        }
        arraybot.logger.warn("JDA has encountered an exception: ${event.cause.message}.")
    }

    /**
     * When a status changes.
     */
    override fun onStatusChange(event: StatusChangeEvent?) {
        if(event == null) {
            arraybot.logger.fatal("Status change event is null.")
            return
        }
        if(event.oldStatus != JDA.Status.CONNECTED) {
            return
        }
        val shard: JDA.ShardInfo? = event.jda.shardInfo
        val shardId = shard?.shardId ?: 0
        arraybot.logger.fatal("The shard $shardId has changed to ${event.status}.")
    }

}