package de.arraying.arraybot.manager;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.Cache;
import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.listener.Listener;
import de.arraying.arraybot.listener.listeners.PostLoadListener;
import de.arraying.arraybot.listener.listeners.preload.ReadyListener;
import de.arraying.arraybot.punishment.PunishmentObject;
import de.arraying.arraybot.punishment.PunishmentType;
import de.arraying.arraybot.shard.ShardEntry;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.util.UDatatypes;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.arraybot.util.UPunishment;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.utils.SessionControllerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    private final Map<Integer, ShardEntry> shards = new TreeMap<>();
    private final Configuration configuration = Arraybot.getInstance().getConfiguration();
    private final Logger logger = LoggerFactory.getLogger("Bot-Manager");
    private final SessionControllerAdapter sessionControllerAdapter = new SessionControllerAdapter();

    /**
     * Gets all the shards.
     * @return A map of shards.
     */
    public Map<Integer, ShardEntry> getShards() {
        return Collections.unmodifiableMap(shards);
    }

    /**
     * Starts the bot.
     * @throws Exception If an error occurs.
     */
    public synchronized void start() throws Exception {
        int shardTotal = configuration.getBotShards();
        logger.info("Logging in a total of {} shards.", shardTotal);
        if(shardTotal < 1) {
            throw new Exception("The shard value must >= 1.");
        }
        if(shardTotal == 1) {
            load(-1, shardTotal);
        } else {
            for(int i = 0; i < shardTotal; i++) {
                load(i, shardTotal);
                Thread.sleep(5000);
            }
        }
    }

    /**
     * Restarts a shard. Must be executed in something other than main thread.
     * @param shard The shard ID to restart.
     * @return True if successful, false otherwise.
     */
    public synchronized boolean restartShard(int shard) {
        if(!shards.containsKey(shard)) {
            logger.info("Received a restart request for a nonexistent shard ({}).", shard);
            return false;
        }
        JDA jda = shards.get(shard).getJDA();
        List<Object> listeners = jda.getRegisteredListeners();
        jda.removeEventListener((Object[]) listeners.toArray(new Object[listeners.size()]));
        jda.shutdown();
        shards.remove(shard);
        try {
            load(shard, configuration.getBotShards());
        } catch(Exception exception) {
            logger.error("There was an error restarting the shard.", exception);
            return false;
        }
        return true;
    }

    /**
     * Marks the specified shard as ready.
     * @param shard The shard.
     */
    public synchronized void ready(int shard) {
        ShardEntry entry = shards.get(shard);
        if(entry == null) {
            logger.error("Received a ready even for a nonexistent shard ({}).", shard);
            return;
        }
        logger.info("The shard {} has been flagged as ready and is commencing with event receiving.", shard);
        entry.onEvent(System.currentTimeMillis());
        long guildId = configuration.getGuildId();
        long premiumId = configuration.getPremiumId();
        if(UDatatypes.getShardId(guildId) == shard) {
            Guild guild = entry.getJDA().getGuildById(guildId);
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
        entry.getJDA().addEventListener((Object[]) Listener.POST_LOAD_LISTENERS);
        new Listener.Updater(entry.getJDA()).create();
        setAuthorUrl(entry.getJDA());
        PunishmentManager punishmentManager = Arraybot.getInstance().getPunishmentManager();
        new AbstractTask("Punishments") {

            /**
             * Iterates through all guilds and handles punishments.
             */
            @Override
            public void onExecution() {
                for(Guild guild : entry.getJDA().getGuilds()) {
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
        long hub = configuration.getGuildId();
        int id = UDatatypes.getShardId(hub);
        ShardEntry entry = shards.get(id);
        return entry.getJDA().getGuildById(hub);
    }

    /**
     * Whether or not a provided ID is a valid guild.
     * @param id The ID of the guild.
     * @return True if it is, false otherwise.
     */
    public boolean isGuild(long id) {
        int index = UDatatypes.getShardId(id);
        ShardEntry entry = shards.get(index);
        if(entry == null) {
            throw new IllegalStateException("Shard entry returned null, something is seriously wrong.");
        }
        return entry.getJDA().getGuildById(id) != null;
    }

    /**
     * Loads the bot.
     * @param shard The current shard.
     * @param total The shard total.
     * @throws Exception In case an error occurs.
     */
    private synchronized void load(int shard, int total) throws Exception {
        if(total == 1) {
            logger.info("Asynchronously firing the JDA instance login, using index {}.", Arraybot.SINGLE_SHARD_INDEX);
            JDA jda = getBuilder().buildAsync();
            int index = Arraybot.SINGLE_SHARD_INDEX;
            shards.put(index, new ShardEntry(jda, index));
        } else {
            logger.info("Asynchronously firing up shard number {}.", shard);
            JDA jda = getShardedBuilder(shard, total).buildAsync();
            shards.put(shard, new ShardEntry(jda, shard));
        }
    }

    /**
     * Gets the JDA builder.
     * @return A JDABuilder.
     */
    private JDABuilder getBuilder() {
        return new JDABuilder(AccountType.BOT)
                .setToken(configuration.isBotBeta() ? configuration.getBotBetaToken() : configuration.getBotToken())
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListener(new ReadyListener())
                .setCorePoolSize(8)
                .setGame(Game.listening(configuration.getBotPrefix() + "help || v" + configuration.getBotVersion()));
    }

    /**
     * Gets the sharded version of the JDA builder.
     * @param shard The current shard ID, 0 based indexing.
     * @param total The shard total.
     * @return A JDA builder.
     */
    private JDABuilder getShardedBuilder(int shard, int total) {
        return getBuilder().useSharding(shard, total).setSessionController(sessionControllerAdapter);
    }

    /**
     * Sets the author's URL.
     * @param shard The shard.
     */
    private void setAuthorUrl(JDA shard) {
        User author = shard.getUserById(configuration.getBotAuthors()[0]);
        if(author != null) {
            Cache.getInstance().setAuthorIconUrl(author.getAvatarUrl());
        }
    }

}
