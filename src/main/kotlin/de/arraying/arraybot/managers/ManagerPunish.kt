package de.arraying.arraybot.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.CGuild
import de.arraying.arraybot.cache.entities.CPunishment
import de.arraying.arraybot.commands.abstraction.PunishmentCommand
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.misc.CustomEmbedBuilder
import de.arraying.arraybot.scheduler.Scheduler
import de.arraying.arraybot.scheduler.SchedulerTask
import de.arraying.arraybot.utils.UTime
import de.arraying.arraybot.utils.Utils
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.exceptions.PermissionException

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
class ManagerPunish {

    /**
     * Punishes a user. Returns false if unsuccessful.
     */
    fun punish(guild: Guild, punished: Long, type: PunishmentCommand.PunishmentType, staff: Member, expiration: Long, reason: String): Boolean {
        val cache = Cache.guilds[guild.idLong]?: return false
        val id = ++cache.mod!!.punishmentCount
        val punishment = Arraybot.instance.managerSql.addPunishment(guild.idLong, id, punished, type.toString(), staff.user.idLong,
                expiration, false, reason)?: return false
        punishment.nullableUser = guild.getMemberById(punished).user
        punishment.nullableStaff = staff.user
        var good = true
        try {
            when(type) {
                PunishmentCommand.PunishmentType.KICK -> {
                    guild.getMemberById(punished) ?: return false
                    guild.controller.kick(punished.toString(), reason).queue({
                        log(guild, id)
                    }, {
                        good = false
                    })
                }
                PunishmentCommand.PunishmentType.TEMPMUTE -> {
                    val user = guild.getMemberById(punished)?: return false
                    good = mute(guild, cache, id, user)
                    handleTimedPunishment(guild, punishment)
                }
                PunishmentCommand.PunishmentType.MUTE -> {
                    val user = guild.getMemberById(punished)?: return false
                    good = mute(guild, cache, id, user)
                }
                PunishmentCommand.PunishmentType.SOFTBAN -> {
                    val user = guild.getMemberById(punished)?: return false
                    good = ban(guild, id, user.user.id, reason)
                }
                PunishmentCommand.PunishmentType.TEMPBAN -> {
                    good = ban(guild, id, punished.toString(), reason)
                    handleTimedPunishment(guild, punishment)
                }
                PunishmentCommand.PunishmentType.BAN -> {
                    good = ban(guild, id, punished.toString(), reason)
                }
                else -> good = false
            }
        } catch(exception: PermissionException) {
            good = false
        }
        if(good) {
            cache.punishments.put(id, punishment)
        }
        return good
    }

    /**
     * Removes a temporary punishment.
     */
    fun revokePunish(guild: Guild, punishmentId: Long, manual: Boolean = false) {
        val cache = Cache.guilds[guild.idLong]?: return
        val punishment = cache.punishments[punishmentId]?: return
        punishment.revoked = true
        if(punishment.type == PunishmentCommand.PunishmentType.TEMPMUTE
                || punishment.type == PunishmentCommand.PunishmentType.MUTE) {
            val user = guild.getMemberById(punishment.user)?: return
            val role = guild.getRoleById(cache.mod!!.muteRole)?: return
            if(!manual) {
                if(!user.roles.contains(role)) {
                    return
                }
                guild.controller.removeRolesFromMember(user, role).queue()
            }
        } else if(punishment.type == PunishmentCommand.PunishmentType.TEMPBAN
                || punishment.type == PunishmentCommand.PunishmentType.BAN) {
            if(!manual) {
                if(!guild.bans.complete().any {
                    it.id == punishment.user.toString()
                }) {
                    return
                }
                guild.controller.unban(punishment.user.toString()).queue()
            }
        } else {
            return
        }
        log(guild, punishmentId, true, manual)
    }

    /**
     * Gets the punishment log embed.
     */
    private fun getPunishmentEmbed(guild: Guild, punishmentId: Long, finish: Boolean = false, manual: Boolean = false): CustomEmbedBuilder? {
        val cache = Cache.guilds[guild.idLong]?: return null
        val punishment = cache.punishments[punishmentId]?: return null
        val channel = guild.publicChannel
        val embed = Utils.getEmbed(channel)
        var title = Messages.PUNISH_EMBED_TITLE.content(channel)
                .replace("{id}", punishmentId.toString())
        if(!finish) {
            title = title.replace("{type}", Utils.setFirstUppercase(punishment.type))
        } else {
            title = title.replace("{type}", Utils.setFirstUppercase(if(punishment.type == PunishmentCommand.PunishmentType.TEMPMUTE
                                                                            || punishment.type == PunishmentCommand.PunishmentType.MUTE) {
                        PunishmentCommand.PunishmentType.UNMUTE
                    } else if(punishment.type == PunishmentCommand.PunishmentType.TEMPBAN
                        || punishment.type == PunishmentCommand.PunishmentType.BAN){
                        PunishmentCommand.PunishmentType.UNBAN
                    } else {
                        PunishmentCommand.PunishmentType.UNKNOWN
                    }))
        }
        embed.setAuthor(title, null, null)
        val userString: String =
        if(punishment.nullableUser != null) {
            getDisplayableUser(punishment.nullableUser!!)
        } else {
            getDisplayableUser(punishment.user, guild)
        }
        embed.addField(Messages.PUNISH_EMBED_USER.content(channel),
                userString,
                false)
        val staffString: String =
        if(!finish) {
            if(punishment.nullableStaff != null) {
                getDisplayableUser(punishment.nullableStaff!!)
            } else {
                getDisplayableUser(punishment.staff, guild)
            }
        } else {
            if(!manual) {
                Messages.PUNISH_EMBED_REVOCATION_AUTO.content(channel)
            } else {
                Messages.PUNISH_EMBED_REVOCATION_MANUAL.content(channel)
            }
        }
        embed.addField(Messages.PUNISH_EMBED_STAFF.content(channel),
                staffString,
                false)
        if(!finish) {
            embed.addField(Messages.PUNISH_EMBED_REASON.content(channel),
                    punishment.reason,
                    false)
        }
        if(punishment.expiration > 0
                && !finish) {
            try {
                embed.addField(Messages.PUNISH_EMBED_EXPIRATION.content(channel),
                        UTime.getDisplayableTime(guild, punishment.expiration),
                        false)
                embed.setFooter(Messages.PUNISH_EMBED_EXPIRATION_FOOTER.content(channel), null)
            } catch(exception: Exception) {
                throw IllegalArgumentException("The punishment expiration must be of an integer.")
            }
        }
        return embed
    }

    /**
     * Handles the timed punishment, scheduling it if it needs to be.
     */
    fun handleTimedPunishment(guild: Guild, punishment: CPunishment) {
        val expiration = punishment.expiration
        val current = System.currentTimeMillis()
        if(expiration <= 0) {
            return
        }
        if(expiration <= current) {
            revokePunish(guild, punishment.punishmentId)
            return
        }
        val task = object: SchedulerTask(expiration - current, 69, true) {
            override fun onTask() {
                revokePunish(guild, punishment.punishmentId)
            }
        }
        Scheduler.schedule(task)
    }

    /**
     * Gets a displayable user if possible.
     */
    private fun getDisplayableUser(user: User): String {
        return user.name + "#" + user.discriminator
    }

    /**
     * Gets a displayable user if possible.
     */
    private fun getDisplayableUser(id: Long, guild: Guild): String {
        val user: User = Arraybot.instance.managerBot.shards.values
                .firstOrNull {
                    it.getUserById(id) != null
                }
                ?.getUserById(id)?: return "${Messages.MISC_NONE.content(guild.publicChannel)} ($id)"
        return user.name + "#" + user.discriminator
    }

    /**
     * Mutes a member. Returns false if unsuccessful.
     */
    private fun mute(guild: Guild, cache: CGuild, id: Long, user: Member): Boolean {
        val role = guild.getRoleById(cache.mod!!.muteRole)?: return false
        var good = true
        guild.controller.addRolesToMember(user, role).queue({
            log(guild, id)
        }, {
            good = false
        })
        return good
    }

    /**
     * Bans a member. Returns false if unsuccessful.
     */
    private fun ban(guild: Guild, id: Long, user: String, reason: String): Boolean {
        var good = true
        guild.controller.ban(user, 0, reason).queue({
            log(guild, id)
        }, {
            good = false
        })
        return good
    }

    /**
     * Logs the punishment action.
     */
    private fun log(guild: Guild, punishmentId: Long, finish: Boolean = false, manual: Boolean = false) {
        val cache = Cache.guilds[guild.idLong]?: return
        if(!cache.logs!!.modEnabled) {
            return
        }
        val embed = getPunishmentEmbed(guild, punishmentId, finish, manual)?: return
        val channel = guild.getTextChannelById(cache.logs!!.modChannel)?: return
        channel.sendMessage(embed.build()).queue()
    }

}