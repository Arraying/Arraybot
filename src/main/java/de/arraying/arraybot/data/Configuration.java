package de.arraying.arraybot.data;

import de.arraying.arraybot.request.BotListRequest;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONField;

import java.io.*;

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
public final class Configuration {

    @JSONField(key = "bot-token") private String botToken;
    @JSONField(key = "bot-beta-token") private String botBetaToken;
    @JSONField(key = "bot-shards") private int botShards;
    @JSONField(key = "bot-authors") private Long[] botAuthors;
    @JSONField(key = "bot-prefix") private String botPrefix;
    @JSONField(key = "bot-version") private String botVersion;
    @JSONField(key = "bot-language") private String botLanguage;
    @JSONField(key = "bot-beta") private boolean botBeta;
    @JSONField(key = "guild-id") private long guildId;
    @JSONField(key = "premium-id") private long premiumId;
    @JSONField(key = "redis-host") private String redisHost;
    @JSONField(key = "redis-port") private int redisPort;
    @JSONField(key = "redis-auth") private String redisAuth;
    @JSONField(key = "redis-index") private int redisIndex;
    @JSONField(key = "requests") private BotListRequest[] requests;
    @JSONField(key = "announcement") private String announcement;
    @JSONField(key = "start-command") private String startCommand;

    /**
     * Empty constructor.
     */
    public Configuration() {}

    /**
     * Gets the Configuration.
     * @param file The configuration file.
     * @throws ConfigurationException If an exception occurs.
     */
    public static Configuration getConfiguration(File file) throws ConfigurationException {
        if(file.exists()) {
            return load(file);
        } else {
            create(file);
            throw new ConfigurationException("The configuration has been created and must be filled in.");
        }
    }

    /**
     * Loads the configuration.
     * @param file The file to use as a configuration file.
     * @return Returns a data configuration.
     * @throws ConfigurationException If an exception occurs.
     */
    private static Configuration load(File file) throws ConfigurationException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            String jsonString = stringBuilder.toString();
            JSON json = new JSON(jsonString);
            Configuration config = json.marshal(Configuration.class);
            if(!config.isValid()) {
                throw new IllegalStateException("The configuration file is invalid.");
            }
            return config;
        } catch(IOException exception) {
            throw new ConfigurationException(exception.getMessage());
        }
    }

    /**
     * Creates the configuration.
     * @param file The file to use as a configuration file.
     * @throws ConfigurationException If an I/O error occurs.
     */
    private static void create(File file) throws ConfigurationException {
        JSON json = new JSON()
                .put("fill", "in");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.marshal());
            writer.close();
        } catch(IOException exception) {
            throw new ConfigurationException(exception.getMessage());
        }
    }

    /**
     * Checks whether the configuration is valid.
     * @return True if it is valid, else false.
     */
    private boolean isValid() {
        return ((botBeta && botBetaToken != null && !botBetaToken.isEmpty())
                || (!botBeta && botToken != null && !botToken.isEmpty()))
                && botShards > 0
                && botAuthors != null && botAuthors.length > 0
                && botPrefix != null &&!botPrefix.isEmpty()
                && botVersion != null && !botVersion.isEmpty()
                && botLanguage != null && !botLanguage.isEmpty()
                && redisHost != null && !redisHost.isEmpty()
                && (redisPort > 1024 && redisPort < 49151)
                && (redisIndex >= 0 && redisIndex <= 16)
                && guildId != UDefaults.DEFAULT_SNOWFLAKE
                && premiumId != UDefaults.DEFAULT_SNOWFLAKE;
    }

    /**
     * Gets the bot token.
     * @return The bot token.
     */
    public String getBotToken() {
        return botToken;
    }

    /**
     * Gets the bot beta token.
     * @return The bot beta token.
     */
    public String getBotBetaToken() {
        return botBetaToken;
    }

    /**
     * Gets the number of shards.
     * @return The shard number. >= 1.
     */
    public int getBotShards() {
        return botShards;
    }

    /**
     * Gets an array of author IDs.
     * @return The authors.
     */
    public Long[] getBotAuthors() {
        return botAuthors;
    }

    /**
     * Gets the default bot prefix.
     * @return The bot prefix.
     */
    public String getBotPrefix() {
        return botPrefix;
    }

    /**
     * Gets the bot version.
     * @return The bot version.
     */
    public String getBotVersion() {
        return botVersion;
    }

    /**
     * Gets the default bot language.
     * @return The bot language.
     */
    public String getBotLanguage() {
        return botLanguage;
    }

    /**
     * Whether or not to use the beta token.
     * @return True = beta, false = regular.
     */
    public boolean isBotBeta() {
        return botBeta;
    }

    /**
     * Gets the ID of the guild.
     * @return The ID.
     */
    public long getGuildId() {
        return guildId;
    }

    /**
     * Gets the ID of the Premium role.
     * @return The role.
     */
    public long getPremiumId() {
        return premiumId;
    }

    /**
     * Gets the Redis host.
     * @return The Redis host.
     */
    public String getRedisHost() {
        return redisHost;
    }

    /**
     * Gets the Redis port.
     * @return The Redis port.
     */
    public int getRedisPort() {
        return redisPort;
    }

    /**
     * Gets the Redis password.
     * @return The Redis password.
     */
    public String getRedisAuth() {
        return redisAuth;
    }

    /**
     * Gets the Redis index.
     * @return The Redis index.
     */
    public int getRedisIndex() {
        return redisIndex;
    }

    /**
     * Gets all request objects.
     * @return An array of requests.
     */
    public BotListRequest[] getRequests() {
        return requests;
    }

    /**
     * Gets the announcement.
     * @return The announcement.
     */
    public String getAnnouncement() {
        return announcement;
    }

    /**
     * Gets the start command.
     * @return The start command.
     */
    public String getStartCommand() {
        return startCommand;
    }

    /**
     * In case something bad happens, or something breaks. This will get thrown if pineapple is put on pizza.
     */
    public static class ConfigurationException extends Exception {

        /**
         * Creates a new configuration exception.
         * @param message The exception message.
         */
        ConfigurationException(String message) {
            super(message);
        }

    }

}
