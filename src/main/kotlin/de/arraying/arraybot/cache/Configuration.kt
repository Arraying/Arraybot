package de.arraying.arraybot.cache

import de.arraying.arraybot.misc.ArraybotException
import de.arraying.arraybot.misc.CustomEmbedBuilder
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*

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
data class Configuration(val botShards: Int,
                         val botAuthorId: Long,
                         val botToken: String,
                         val botBetaToken: String,
                         val botBeta: Boolean,
                         val botVersion: String,
                         val botPrefix: String,
                         val botDefaultLanguage: String,
                         val mySQLHost: String,
                         val mySQLDatabase: String,
                         val mySQLUsername: String,
                         val mySQLPassword: String,
                         val mySQLGuildsTable: String,
                         val mySQLCommandsTable: String,
                         val mySQLFilterTable: String,
                         val mySQLPunishmentsTable: String,
                         val mySQLDisabledTable: String,
                         val mySQLBlacklistTable: String,
                         val mySQLStatisticsTable: String,
                         val mySQLAnnouncerTable: String,
                         val mySQLAnnouncementsTable: String,
                         val mySQLAutoroleTable: String,
                         val mySQLLogsTable: String,
                         val mySQLModTable: String,
                         val mySQLFilterBypassTable: String,
                         val guildId: Long,
                         val guildPremiumId: Long,
                         val keyCarbonitex: String,
                         val keyDiscordPw: String,
                         val keyDiscordOrg: String,
                         val keyPastebin: String,
                         val miscAnnouncement: String) {

    companion object {

        val entries = LinkedHashMap<String, Pair<EntryType, Any>>()

        /**
         * Filling in different entries.
         */
        init {
            entries.put("botShards", Pair(EntryType.INT, 1))
            entries.put("botAuthorId", Pair(EntryType.LONG, 0))
            entries.put("botToken", Pair(EntryType.STRING, "none"))
            entries.put("botBetaToken", Pair(EntryType.STRING, "none"))
            entries.put("botBeta", Pair(EntryType.BOOLEAN, true))
            entries.put("botVersion", Pair(EntryType.STRING, "0.0.0"))
            entries.put("botPrefix", Pair(EntryType.STRING, "//"))
            entries.put("botDefaultLanguage", Pair(EntryType.STRING, "en"))
            entries.put("mySQLHost", Pair(EntryType.STRING, "localhost"))
            entries.put("mySQLDatabase", Pair(EntryType.STRING, "Arraybot5"))
            entries.put("mySQLUsername", Pair(EntryType.STRING, "Arraybot"))
            entries.put("mySQLPassword", Pair(EntryType.STRING, ""))
            entries.put("mySQLGuildsTable", Pair(EntryType.STRING, "ab5_guilds"))
            entries.put("mySQLCommandsTable", Pair(EntryType.STRING, "ab5_commands"))
            entries.put("mySQLFilterTable", Pair(EntryType.STRING, "ab5_filter"))
            entries.put("mySQLPunishmentsTable", Pair(EntryType.STRING, "ab5_punishments"))
            entries.put("mySQLDisabledTable", Pair(EntryType.STRING, "ab5_disabled"))
            entries.put("mySQLBlacklistTable", Pair(EntryType.STRING, "ab5_blacklist"))
            entries.put("mySQLStatisticsTable", Pair(EntryType.STRING, "ab5_statistics"))
            entries.put("mySQLAnnouncerTable", Pair(EntryType.STRING, "ab5_announcer"))
            entries.put("mySQLAnnouncementsTable", Pair(EntryType.STRING, "ab5_announcements"))
            entries.put("mySQLAutoroleTable", Pair(EntryType.STRING, "ab5_autorole"))
            entries.put("mySQLLogsTable", Pair(EntryType.STRING, "ab5_logs"))
            entries.put("mySQLModTable", Pair(EntryType.STRING, "ab5_mod"))
            entries.put("mySQLFilterBypassTable", Pair(EntryType.STRING, "ab5_filter_bypass"))
            entries.put("guildId", Pair(EntryType.LONG, 0))
            entries.put("guildPremiumId", Pair(EntryType.LONG, 0))
            entries.put("keyCarbonitex", Pair(EntryType.STRING, ""))
            entries.put("keyDiscordPw", Pair(EntryType.STRING, ""))
            entries.put("keyDiscordOrg", Pair(EntryType.STRING, ""))
            entries.put("keyPastebin", Pair(EntryType.STRING, ""))
            entries.put("miscAnnouncement", Pair(EntryType.STRING, ""))
        }

        /**
         * Sets up and manages the configuration of the bot.
         */
        fun setupConfiguration(): Configuration {
            val file = File("config.json")
            try {
                if(!file.createNewFile()) {
                    return loadConfiguration(file)
                } else {
                    createConfiguration(file)
                    throw Exception("The configuration file has been created, please fill it in.")
                }
            } catch(exception: JSONException) {
                throw ArraybotException("An error occurred setting up the configuration: ${exception.message}.")
            }
        }

        /**
         * Loads in the configuration from a file.
         */
        private fun loadConfiguration(file: File): Configuration {
            val jsonString = file.readText()
            val json = JSONObject(jsonString)
            val entryList = LinkedList<Any>()
            for((entryKey, entryValue) in entries) {
                when(entryValue.first) {
                    EntryType.STRING -> entryList.add(json.getString(entryKey))
                    EntryType.BOOLEAN -> entryList.add(json.getBoolean(entryKey))
                    EntryType.INT -> entryList.add(json.getInt(entryKey))
                    EntryType.LONG -> entryList.add(json.getLong(entryKey))
                }
            }
            val constructor = Configuration::class.java.declaredConstructors[0]
            val configuration = constructor.newInstance(*entryList.toTypedArray()) as Configuration
            if(!configuration.isValid()) {
                throw Exception("Some of the given configuration settings are invalid")
            }
            return configuration
        }

        /**
         * Loads in the required keys into the configuration file.
         */
        private fun createConfiguration(file: File) {
            val json = JSONObject()
            for((entryKey, entryValue) in entries) {
                json.put(entryKey, entryValue.second)
            }
            val writer = BufferedWriter(FileWriter(file))
            writer.write(json.toString())
            writer.close()
        }

    }

    /**
     * Returns whether or not the configuration is valid.
     */
    fun isValid(): Boolean {
        if(botShards > 200
                || (botBeta
                    && botBetaToken.isEmpty()
                ) || (!botBeta
                    && botToken.isEmpty()
                ) || botVersion.isEmpty()
                || botDefaultLanguage.isEmpty()
                || mySQLHost.isEmpty()
                || mySQLDatabase.isEmpty()
                || mySQLUsername.isEmpty()
                || mySQLGuildsTable.isEmpty()
                || mySQLCommandsTable.isEmpty()
                || mySQLFilterTable.isEmpty()
                || mySQLPunishmentsTable.isEmpty()
                || mySQLDisabledTable.isEmpty()
                || mySQLBlacklistTable.isEmpty()
                || mySQLStatisticsTable.isEmpty()
                || mySQLAnnouncementsTable.isEmpty()
                || mySQLLogsTable.isEmpty()
                || mySQLLogsTable.isEmpty()
                || mySQLModTable.isEmpty()
                || mySQLFilterBypassTable.isEmpty()
                || (!miscAnnouncement.isEmpty()
                    && miscAnnouncement.length > CustomEmbedBuilder.TEXT_MAX_LENGTH
                )) {
            return false
        }
        return true
    }

    enum class EntryType {

        STRING,
        BOOLEAN,
        INT,
        LONG

    }

}