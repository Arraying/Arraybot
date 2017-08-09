package de.arraying.arraybot.listeners

import de.arraying.arraybot.Arraybot
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
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
class ListenerSQL:
        ListenerAdapter() {

    private val arraybot = Arraybot.instance

    /**
     * When the bot is added to a guild.
     */
    override fun onGuildJoin(event: GuildJoinEvent?) {
        if(event == null) {
            return
        }
        arraybot.managerSql.addGuild(event.guild.idLong)
    }

}