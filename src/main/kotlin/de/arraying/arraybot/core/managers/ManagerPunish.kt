package de.arraying.arraybot.core.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.CPunishment
import de.arraying.arraybot.core.language.Messages
import de.arraying.arraybot.core.punishment.PunishmentType
import de.arraying.arraybot.core.scheduler.Scheduler
import de.arraying.arraybot.core.scheduler.SchedulerTask
import de.arraying.arraybot.misc.CustomEmbedBuilder
import de.arraying.arraybot.utils.UTime
import de.arraying.arraybot.utils.Utils
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.User

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
    fun punish(guild: Guild, punishedId: Long, type: PunishmentType, staff: Member, expiration: Long, reason: String): Boolean {
        val cache = Cache.guilds[guild.idLong]?: return false
        val punishedMember = guild.getMemberById(punishedId)?: return false
        val id = ++cache.mod!!.punishmentCount
        val userString = getStringUser(punishedId, guild)
        val staffString = getStringUser(staff.user)
        val success = type.punishment.invoke(guild, punishedId, punishedMember, reason)
        if(success) {
            val punishment = Arraybot.instance.managerSql.addPunishment(guild.idLong,
                    id,
                    punishedId,
                    userString,
                    type.toString(),
                    staff.user.idLong,
                    staffString,
                    expiration,
                    false,
                    reason)?: return false
            handleTimedPunishment(guild, punishment)
        }
        return success
    }

    /**
     * Removes a temporary punishment.
     */
    fun revokePunish(guild: Guild, punishmentId: Long, manual: Boolean = false) {
        val cache = Cache.guilds[guild.idLong]?: return
        val punishment = cache.punishments[punishmentId]?: return
        punishment.revoked = true
        if(punishment.type.punishment.revoke(guild, punishment, manual)) {
            log(guild, punishmentId, true, manual)
        }
    }

    /**
     * Gets the punishment log embed.
     */
    fun getPunishmentEmbed(guild: Guild, punishmentId: Long, finish: Boolean = false, manual: Boolean = false): CustomEmbedBuilder? {
        val cache = Cache.guilds[guild.idLong]?: return null
        val punishment = cache.punishments[punishmentId]?: return null
        val channel = guild.publicChannel
        val embed = Utils.getEmbed(channel)
        val title = Messages.PUNISH_EMBED_TITLE.content(channel)
                .replace("{id}", punishmentId.toString())
        embed.setAuthor(if(!finish) {
                        title.replace("{type}", Utils.setFirstUppercase(punishment.type))
                    } else {
                        title.replace("{type}", Utils.setFirstUppercase(when(punishment.type) {
                            PunishmentType.MUTE -> PunishmentType.UNMUTE
                            PunishmentType.TEMPMUTE -> PunishmentType.UNMUTE
                            PunishmentType.BAN -> PunishmentType.UNBAN
                            PunishmentType.TEMPBAN -> PunishmentType.UNBAN
                            else -> PunishmentType.UNKNOWN
                        }))
                    },
                null, null)
                .addField(Messages.PUNISH_EMBED_USER.content(channel),
                    punishment.userString,
                    false)
                .addField(Messages.PUNISH_EMBED_STAFF.content(channel),
                    if(!finish) {
                        punishment.staffString
                    } else {
                        if(!manual) {
                            Messages.PUNISH_EMBED_REVOCATION_AUTO.content(channel)
                        } else {
                            Messages.PUNISH_EMBED_REVOCATION_MANUAL.content(channel)
                        }
                    },
                false)
        if(!finish) {
            embed.addField(Messages.PUNISH_EMBED_REASON.content(channel),
                    punishment.reason,
                    false)
            if(punishment.expiration > 0) {
                try {
                    embed.addField(Messages.PUNISH_EMBED_EXPIRATION.content(channel),
                            UTime.getDisplayableTime(guild, punishment.expiration),
                            false)
                    embed.setFooter(Messages.PUNISH_EMBED_EXPIRATION_FOOTER.content(channel), null)
                } catch(exception: Exception) {
                    throw IllegalArgumentException("The punishment expiration must be of an integer.")
                }
            }
        }
        return embed
    }

    /**
     * Handles the timed punishment, scheduling it if it needs to be.
     */
    fun handleTimedPunishment(guild: Guild, punishment: CPunishment) {
        if(punishment.type != PunishmentType.TEMPMUTE
                || punishment.type != PunishmentType.TEMPBAN) {
            return
        }
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
    private fun getStringUser(user: User): String {
        return user.name + "#" + user.discriminator
    }

    /**
     * Gets a displayable user if possible.
     */
    private fun getStringUser(id: Long, guild: Guild): String {
        val user: User = Arraybot.instance.managerBot.shards.values
                .firstOrNull {
                    it.getUserById(id) != null
                }
                ?.getUserById(id)?: return "${Messages.MISC_NONE.content(guild.publicChannel)} ($id)"
        return user.name + "#" + user.discriminator
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