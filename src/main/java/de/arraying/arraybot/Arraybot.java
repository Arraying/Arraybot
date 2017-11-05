package de.arraying.arraybot;

import de.arraying.arraybot.data.Configuration;
import de.arraying.arraybot.manager.BotManager;
import de.arraying.arraybot.manager.PunishmentManager;
import de.arraying.arraybot.manager.ScriptManager;
import de.arraying.arraybot.manager.StorageManager;
import de.arraying.arraybot.startup.Starter;
import de.arraying.arraybot.util.objects.Splash;
import net.dv8tion.jda.core.JDAInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
public final class Arraybot {

    /**
     * Gets the default single shard (running 1 total shards) index.
     */
    public static final int SINGLE_SHARD_INDEX = 0;

    private static Arraybot instance;
    private static final Object mutex = new Object();
    private final Logger logger = LoggerFactory.getLogger("Arraybot");
    private Configuration configuration;
    private BotManager botManager;
    private PunishmentManager punishmentManager;
    private ScriptManager scriptManager;
    private StorageManager storageManager;
    private boolean initialized = false;

    /**
     * The main method. Executed when the program runs.
     * @param args The parameters passed into the program at runtime.
     */
    public static void main(String[] args) {
        Thread.currentThread().setName("Main");
        Arraybot.getInstance().init();
    }

    /**
     * Private constructor to prevent initialization.
     */
    private Arraybot() {
    }

    /**
     * The singleton getter. Thread safe.
     * @return The singleton instance.
     */
    public static Arraybot getInstance() {
        if(instance == null) {
            synchronized(mutex) {
                if(instance == null) {
                    instance = new Arraybot();
                }
            }
        }
        return instance;
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
     * Gets the logger.
     * @return The logger.
     */
    public Logger getLogger() {
        return logger;
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

}
