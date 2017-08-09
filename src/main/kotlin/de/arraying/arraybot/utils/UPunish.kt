package de.arraying.arraybot.utils

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.CPunishment
import de.arraying.arraybot.core.punishment.PunishmentType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member

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
object UPunish {

    /**
     * Bans a user.
     */
    fun ban(guild: Guild, member: Member, reason: String): Boolean {
        var success = false
        try {
            guild.controller.ban(member, 0, reason).queue {
                success = true
            }
        } catch(exception: IllegalArgumentException) {}
        return success
    }

    /**
     * Mutes a user.
     */
    fun mute(guild: Guild, member: Member): Boolean {
        val roleId = Cache.guilds[guild.idLong]?.mod?.muteRole?: return false
        val role = guild.getRoleById(roleId)?: return false
        var success = true
        try {
            guild.controller.addRolesToMember(member, role).queue({
                success = true
            })
        } catch(exception: IllegalArgumentException) {}
        return success
    }

    /**
     * Unbans a user.
     */
    fun unBan(guild: Guild, punishment: CPunishment) {
        if(guild.bans.complete().any {
            it.idLong == punishment.user
        }) {
            guild.controller.unban(punishment.user.toString()).queue()
        }
    }

    /**
     * Unmutes a user.
     */
    fun unMute(guild: Guild, punishment: CPunishment) {
        val user = guild.getMemberById(punishment.user)?: return
        val cache = Cache.guilds[guild.idLong]?: return
        val role = guild.getRoleById(cache.mod!!.muteRole)?: return
        if(user.roles.contains(role)) {
            guild.controller.removeRolesFromMember(user, role).queue()
        }
    }

    /**
     * When an invalid punishment type is invoked.
     */
    fun invalidInvocation(type: PunishmentType) {
        Arraybot.instance.logger.fatal("Invoked the $type punishment type; something went terribly wrong.")
    }

}