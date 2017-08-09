package de.arraying.arraybot.utils

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.core.language.Messages
import de.arraying.arraybot.misc.CustomEmbedBuilder
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.TextChannel
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

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
object Utils {

    private val arraybot = Arraybot.instance

    /**
     * Gets the arraybot default embed.
     */
    fun getEmbed(channel: TextChannel): CustomEmbedBuilder {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val embedBuilder = CustomEmbedBuilder()
                .setAuthor("Arraybot", "http://arraybot.xyz",
                        if(shouldImage(channel.guild))
                            "http://i.imgur.com/yA9rB2j.png"
                        else
                            null)
                .setColor(Color(34, 150, 245))
                .setFooter(Messages.COMMAND_EMBED_FOOTER.content(channel)
                        .replace("{year}", year.toString())
                        .replace("{author}", Cache.author!!.name+"#"+Cache.author!!.discriminator),
                        if(shouldImage(channel.guild))
                            Cache.authorIconUrl
                        else
                            null)
        if(shouldImage(channel.guild)) {
            embedBuilder.setThumbnail("http://i.imgur.com/yA9rB2j.png")
        }
        return embedBuilder
    }

    /**
     * Checks if the inputted string is a number.
     */
    fun isInt(input: String): Boolean {
        try {
            Integer.valueOf(input)
            return true
        } catch(exception: NumberFormatException) {
            return false
        }
    }

    /**
     * Checks if the inputted string is a long.
     */
    fun isLong(input: String): Boolean {
        try {
            input.toLong()
            return true
        } catch(exception: NumberFormatException) {
            return false
        }
    }

    /**
     * Gets a guild, null if non existent.
     */
    fun getGuild(id: Long): Guild? {
        val shardId = getShard(id)
        val shard = arraybot.managerBot.shards[shardId.toInt()]?: return null
        return shard.getGuildById(id)
    }

    /**
     * Gets the shard ID for the specified guild ID.
     */
    fun getShard(id: Long): Long {
        return (id shr 22) % arraybot.configuration.botShards
    }

    /**
     * Returns if the guild is cached or not.
     */
    fun isCached(id: Long): Boolean {
        return Cache.guilds.containsKey(id)
    }

    /**
     * Gets the redundantly cached guilds.
     */
    fun getRedundantlyCachedGuilds(): Array<Long> {
        if(arraybot.configuration.botBeta) {
            return emptyArray()
        }
        val guilds = ArrayList<Long>(Cache.guilds.keys)
        arraybot.managerBot.shards.values
                .flatMap {
                    it.guilds
                } .filter {
                    guilds.contains(it.idLong)
                } .forEach {
                    guilds.remove(it.idLong)
                }
        return guilds.toTypedArray()
    }

    /**
     * Sets the first letter uppercase, the rest lowercase.
     */
    fun setFirstUppercase(input: Any): String {
        return input.toString()[0].toUpperCase() +
                input.toString().substring(1).toLowerCase()
    }

    /**
     * Checks if images should be in the embed (performance wise)
     */
    private fun shouldImage(guild: Guild): Boolean {
        return guild.explicitContentLevel == Guild.ExplicitContentLevel.OFF
    }

}