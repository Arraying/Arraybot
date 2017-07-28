package de.arraying.arraybot.managers

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.listeners.ListenerCommand
import de.arraying.arraybot.listeners.ListenerFilter
import de.arraying.arraybot.listeners.ListenerJDA
import de.arraying.arraybot.listeners.ListenerPunishment
import de.arraying.arraybot.misc.ArraybotException
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game
import net.dv8tion.jda.core.exceptions.RateLimitedException
import net.dv8tion.jda.core.utils.SimpleLog
import java.util.*
import javax.security.auth.login.LoginException

@Suppress("INTERFACE_STATIC_METHOD_CALL_FROM_JAVA6_TARGET")
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
class ManagerBot {

    val shards = TreeMap<Int, JDA>()
    private val arraybot = Arraybot.instance
    private val logger = arraybot.logger
    private val eventListeners = arrayOf(ListenerCommand(), ListenerJDA(), ListenerPunishment(), ListenerFilter())
    private val shardTotal = arraybot.configuration.botShards

    /**
     * Initializes the bot
     */
    suspend fun start() {
        logger.info("Logging in a total of $shardTotal JDA instance(s)...")
        try {
            if(shardTotal > 1) {
                val shardList = (0..shardTotal-1).map {
                    shardNumber ->
                        async(CommonPool) {
                            getShardedBuilder(shardNumber, shardTotal).buildBlocking()
                        }
                }
                shardList.forEach {
                    val jda = it.await()
                    shards.put(jda.shardInfo.shardId, jda)
                    handleObjects(jda)
                }
            } else {
                val jda = getBuilder().buildBlocking()
                shards.put(0, jda)
                handleObjects(jda)
            }
        } catch(exception: Exception) {
            handleJDAExceptions(exception)
        }
    }

    /**
     * Become a brutal murderer and shamelessly unleash your fury upon the bot and utterly annihilate the active
     * shards, causing bot blood splatters all over the nearby wall, and only slightly improving your frustration.
     */
    suspend fun kill() {
        if(arraybot.initialised) {
            throw ArraybotException("The bot instances may only be killed during startup.")
        }
        for(shard in shards.values) {
            shard.shutdown()
        }
    }

    /**
     * Restarts a shard.
     */
    suspend fun restartShard(shard: Int): Boolean {
        try {
            if(!shards.containsKey(shard)) {
                return false
            }
            val shardInstance = shards[shard]!!
            shardInstance.removeEventListener(*eventListeners)
            shardInstance.shutdown()
            SimpleLog.LEVEL = SimpleLog.Level.OFF
            shards.remove(shard)
            val jda: JDA
            if(shardTotal > 1) {
                jda = getShardedBuilder(shard, shardTotal).buildBlocking()
            } else {
                jda = getBuilder().buildBlocking()
            }
            shards.put(shard, jda)
            handleObjects(jda)
            SimpleLog.LEVEL = SimpleLog.Level.INFO
        } catch(exception: Exception) {
            handleJDAExceptions(exception)
            return false
        }
        return true
    }

    /**
     * Gets the default JDA Builder.
     */
    suspend private fun getBuilder(): JDABuilder {
        val config = Arraybot.instance.configuration
        val builder = JDABuilder(AccountType.BOT)
                .setToken(if(config.botBeta) config.botBetaToken else config.botToken)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setGame(Game.of("${config.botPrefix} help || v${config.botVersion}"))
                .addEventListener(*eventListeners)
        return builder
    }

    /**
     * Gets the sharded JDA Builder.
     */
    suspend private fun getShardedBuilder(shard: Int, shardTotal: Int): JDABuilder {
        return getBuilder().useSharding(shard, shardTotal)
    }

    /**
     * Handles objects.
     */
    suspend private fun handleObjects(jda: JDA) {
        val author = jda.getUserById(arraybot.configuration.botAuthorId)
        val guild = jda.getGuildById(arraybot.configuration.guildId)
        if(author != null) {
            Cache.author = author
            Cache.authorIconUrl = author.effectiveAvatarUrl
        }
        if(guild != null) {
            Cache.guild = guild
            val role = guild.getRoleById(arraybot.configuration.guildPremiumId)
            if(role != null) {
                Cache.premiumRole = role
            }
        }
    }

    /**
     * Handles the different JDA exceptions.
     */
    suspend private fun handleJDAExceptions(exception: Exception) {
        when(exception) {
            is LoginException -> logger.fatal("An error occurred logging in the bot: ${exception.message}.")
            is RateLimitedException -> logger.fatal("The bot has been ratelimited while logging in.")
            is InterruptedException -> logger.fatal("The bot got interrupted while building non asynchronously.")
        }
    }

}