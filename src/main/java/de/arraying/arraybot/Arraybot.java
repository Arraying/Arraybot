package de.arraying.arraybot;

import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.templates.SetEntry;
import de.arraying.arraybot.manager.*;
import de.arraying.arraybot.startup.Starter;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.arraybot.util.objects.Splash;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

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
public enum Arraybot {

    /**
     * The singleton instance.
     */
    INSTANCE;

    private final Logger logger = LoggerFactory.getLogger("Arraybot");
    private final Set<Long> overrides = new HashSet<>();
    private Configuration configuration;
    private BotManager botManager;
    private PunishmentManager punishmentManager;
    private ScriptManager scriptManager;
    private StorageManager storageManager;
    private FileManager fileManager;
    private boolean initialized = false;

    /**
     * The main method. Executed when the program runs.
     * @param args The parameters passed into the program at runtime.
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("Main");
        Arraybot.INSTANCE.init();
    }

    /**
     * Initializes the actual bot.
     */
    private void init() {
        if(initialized) {
            throw new IllegalStateException("The program has already been initialized.");
        }
        logger.info("Starting up Arraybot...");
        logger.info("Loading in the configuration...");
        try {
            configuration = Configuration.getConfiguration(new File("config.json"));
            logger.info("The configuration has been loaded.");
        } catch(Configuration.ConfigurationException exception) {
            logger.error("There was an error loading/creating the configuration.", exception);
            return;
        }
        new Splash(new File("splash.txt")).print(logger, configuration.getBotVersion(), JDAInfo.VERSION, "Arraying, xaanit");
        Starter.start();
        initialized = true;
    }

    /**
     * Blacklists a user.
     * @param entry Should be a user ID.
     */
    @SuppressWarnings("unused")
    public void blacklist(Object entry) {
        SetEntry blacklist = (SetEntry) Category.BLACKLIST.getEntry();
        blacklist.add(UDefaults.DEFAULT_BLACKLIST, entry);
    }

    /**
     * Un-blacklists a user.
     * @param entry Should be the user ID.
     */
    @SuppressWarnings("unused")
    public void unBlacklist(Object entry) {
        SetEntry blacklist = (SetEntry) Category.BLACKLIST.getEntry();
        blacklist.remove(UDefaults.DEFAULT_BLACKLIST, entry);
    }

    /**
     * Gets the logger.
     * @return The logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets all guild overrides.
     * @return A set of overrides.
     */
    public Set<Long> getOverrides() {
        return overrides;
    }

    /**
     * Gets the configuration.
     * @return The configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Gets the bot manager.
     * @return The bot manager.
     */
    public BotManager getBotManager() {
        return botManager;
    }

    /**
     * Gets the punishment manager.
     * @return The punishment manager.
     */
    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    /**
     * Gets the script manager.
     * @return The script manager.
     */
    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    /**
     * Gets the storage manager.
     * @return The storage manager.
     */
    public StorageManager getStorageManager() {
        return storageManager;
    }

    /**
     * Gets the file manager.
     * @return The file manager.
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * Sets the bot manager.
     * @param manager The manager.
     */
    public synchronized void setBotManager(BotManager manager) {
        if(botManager == null) {
            this.botManager = manager;
        }
    }

    /**
     * Sets the punishment manager.
     * @param manager The manager.
     */
    public synchronized void setPunishmentManager(PunishmentManager manager) {
        if(punishmentManager == null) {
            this.punishmentManager = manager;
        }
    }

    /**
     * Sets the script manager.
     * @param manager The manager.
     */
    public synchronized void setScriptManager(ScriptManager manager) {
        if(scriptManager == null) {
            this.scriptManager = manager;
        }
    }

    /**
     * Sets the storage manager.
     * @param manager The manager.
     */
    public synchronized void setStorageManager(StorageManager manager) {
        if(storageManager == null) {
            this.storageManager = manager;
        }
    }

    /**
     * Sets the file manager.
     * @param manager The manager.
     */
    public synchronized void setFileManager(FileManager manager) {
        if(fileManager == null) {
            this.fileManager = manager;
        }
    }

}
