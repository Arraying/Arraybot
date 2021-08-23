package de.arraying.arraybot.manager;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.Cache;
import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.listener.Listener;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.listener.listeners.preload.ReadyListener;
import de.arraying.arraybot.punishment.PunishmentObject;
import de.arraying.arraybot.punishment.PunishmentType;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.threadding.impl.AnnouncementsTask;
import de.arraying.arraybot.util.UDatatypes;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.arraybot.util.UPunishment;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.*;

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
public final class BotManager {

    private ShardManager shardManager;

    private final Configuration configuration = Arraybot.INSTANCE.getConfiguration();
    private final Logger logger = LoggerFactory.getLogger("Bot-Manager");

    /**
     * Starts the bot.
     */
    public synchronized void start() {
        try {
            shardManager = createShardManager();
        } catch(LoginException exception) {
            logger.error("Invalid token!", exception);
        }
    }

    /**
     * Gets the shard manager.
     * @return The shard manager.
     */
    public ShardManager getShardManager() {
        return shardManager;
    }

    public synchronized void ready(JDA shard) {
        logger.info("The shard {} has been flagged as ready and is commencing with event receiving.", shard.getShardInfo().getShardId());
        long guildId = configuration.getGuildId();
        long premiumId = configuration.getPremiumId();
        if(UDatatypes.getShardId(guildId) == shard.getShardInfo().getShardId()) {
            Guild guild = shard.getGuildById(guildId);
            if(guild == null) {
                logger.error("Invalid home guild ID ({}).", guildId);
                System.exit(1);
            }
            if(guild.getRoleById(configuration.getPremiumId()) == null) {
                logger.error("Invalid Premium role ID ({}).", premiumId);
                System.exit(1);
            }
        }
        for(PostLoadListener listener : Listener.POST_LOAD_LISTENERS) {
            listener.init();
        }
        shard.addEventListener((Object[]) Listener.POST_LOAD_LISTENERS);
        new Listener.Updater(shard).create();
        setAuthorUrl(shard);
        PunishmentManager punishmentManager = Arraybot.INSTANCE.getPunishmentManager();
        new AbstractTask("Ready " + shard.getShardInfo().getShardString()) {

            /**
             * Iterates through all guilds and handles punishments.
             */
            @Override
            public void onExecution() {
                for(Guild guild : shard.getGuilds()) {
                    GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
                    boolean doAnnouncements = Boolean.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.ANNOUNCEMENT_ANNOUNCER), guild.getIdLong(), null));
                    if(doAnnouncements) {
                        AnnouncementsTask.addTask(guild.getIdLong());
                    }
                    for(PunishmentObject punishment : punishmentManager.getAllPunishments(guild)) {
                        if(!punishment.isRevoked()) {
                            try {
                                if(punishment.getType() == PunishmentType.BAN) {
                                    if(!UPunishment.isBan(guild, punishment.getUser())) {
                                        punishmentManager.revoke(guild, punishment, UDefaults.DEFAULT_UNKNOWN_SNOWFLAKE);
                                    }
                                } else if(punishment.getType() == PunishmentType.MUTE) {
                                    Member muted = guild.getMemberById(punishment.getUser());
                                    if(muted == null) {
                                        return;
                                    }
                                    if(!UPunishment.isMute(muted)) {
                                        punishmentManager.revoke(guild, punishment, UDefaults.DEFAULT_UNKNOWN_SNOWFLAKE);
                                    }
                                }
                            } catch(PermissionException exception) {
                                logger.info("Could not handle punishments for the guild {} due to permission exception.", guild.getIdLong());
                            }
                        }
                        punishmentManager.schedulePunishmentRevocation(guild, punishment);
                    }
                }
            }

        }.create();
    }


    /**
     * Gets the hub guild.
     * @return The guild. May be null, but generally should not be.
     */
    public Guild getHub() {
        try {
            long hub = configuration.getGuildId();
            int id = UDatatypes.getShardId(hub);
            JDA shard = shardManager.getShardById(id);
            assert shard != null;
            return shard.getGuildById(hub);
        } catch(IllegalArgumentException exception) {
            logger.error("Could not get hub guild.");
            return null;
        }
    }

    /**
     * Whether or not a provided ID is a valid guild.
     * @param id The ID of the guild.
     * @return True if it is, false otherwise.
     */
    public boolean isGuild(long id) {
        try {
            int shardId = UDatatypes.getShardId(id);
            JDA shard = shardManager.getShardById(shardId);
            assert shard != null;
            return shard.getGuildById(id) != null;
        } catch(IllegalArgumentException exception) {
            return false;
        }
    }

    /**
     * Gets the default shard manager.
     * @return The default shard manager.
     * @throws LoginException If the bot could not log in.
     */
    private ShardManager createShardManager()
            throws LoginException {
        List<GatewayIntent> intents = Arrays.asList(
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_BANS,
            GatewayIntent.GUILD_MESSAGES
        );
        return DefaultShardManagerBuilder.create(intents)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS)
                .setToken(configuration.isBotBeta() ? configuration.getBotBetaToken() : configuration.getBotToken())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setShardsTotal(configuration.getBotShards())
                .addEventListeners(new ReadyListener())
                .setUseShutdownNow(true)
                .setActivity(Activity.listening(configuration.getBotPrefix() + "help || v" + configuration.getBotVersion()))
                .build();
    }

    /**
     * Sets the author's URL.
     * @param shard The shard.
     */
    private void setAuthorUrl(JDA shard) {
        User author = shard.getUserById(configuration.getBotAuthors()[0]);
        if(author != null) {
            Cache.INSTANCE.setAuthorIconUrl(author.getAvatarUrl());
        }
    }

}
