package de.arraying.arraybot.listeners

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent
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
class ListenerPunishment:
        ListenerAdapter() {

    private val arraybot = Arraybot.instance

    /**
     * When someone is unbanned.
     */
    override fun onGuildUnban(event: GuildUnbanEvent) {
        val cache = Cache.guilds[event.guild.idLong]?: return
        cache.punishments.values.filter {
            it.user == event.user.idLong
                    && !it.revoked
        } .forEach {
            arraybot.managerPunish.revokePunish(event.guild, it.punishmentId, true)
        }
    }

    /**
     * When someone is unmuted.
     */
    override fun onGuildMemberRoleRemove(event: GuildMemberRoleRemoveEvent) {
        val cache = Cache.guilds[event.guild.idLong]?: return
        cache.punishments.values.filter {
            it.user == event.member.user.idLong
                    && event.roles.any {
                        role -> role.idLong == cache.mod!!.muteRole
                    }
                    && !it.revoked
        } .forEach {
            arraybot.managerPunish.revokePunish(event.guild, it.punishmentId, true)
        }
    }

}