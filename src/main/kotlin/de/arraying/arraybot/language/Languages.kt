package de.arraying.arraybot.language

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Entry
import net.dv8tion.jda.core.entities.Guild
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import java.util.*
import java.util.regex.Pattern

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
class Languages {

    /**
     * The companion object. Everything in here is "static".
     */
    companion object {

        /**
         * Whether or not the program is done loading the languages.
         */
        var done = false
            set(value) {
                if (!field) {
                    field = value
                }
            }

        private val languages = HashMap<String, ResourceBundle>()
        private val defaultLanguage = Arraybot.getInstance().configuration.botLanguage
        private val filePattern = Pattern.compile("^Language_[a-z]{2,}\\.properties$")
        private val logger = LoggerFactory.getLogger("Languages")

        /**
         * Loads in and registers all languages.
         */
        @Throws(Exception::class)
        fun init() {
            if (done) {
                throw IllegalStateException("The languages have already been initialized.")
            }
            val directory = File("languages")
            if (!directory.exists()) {
                directory.mkdirs()
                throw IllegalArgumentException("The language directory did not exist, hence it has been created.")
            }
            val urls = arrayOf(directory.toURI().toURL())
            val classLoader = URLClassLoader(urls)
            for (file in directory.listFiles()) {
                var name = file.name
                val matcher = filePattern.matcher(name)
                if (!matcher.find()) {
                    continue
                }
                name = name.substring(name.indexOf("_") + 1, name.indexOf(".properties"))
                languages.put(name, ResourceBundle.getBundle("Language", Locale(name), classLoader))
                logger.info("Registered the language \"$name\".")
            }
            if (languages.isEmpty()) {
                throw IllegalArgumentException("No valid language were found.")
            }
            if (!languages.containsKey(Arraybot.getInstance().configuration.botLanguage)) {
                throw IllegalArgumentException("The default language specified does not exist.")
            }
            done = true
        }

        /**
         * Gets the correct language message for the guild.
         */
        fun get(id: Long, message: String): String {
            val entry = Entry.Category.GUILD.entry as? GuildEntry ?:
                  throw IllegalStateException("Expected guild entry to be instanceof GuildEntry.")
            val guildLanguage = entry.fetch(entry.getField(GuildEntry.Fields.LANGUAGE), id, null).toLowerCase()
            val locale = if (languages.containsKey(guildLanguage)) {
                guildLanguage
            } else {
                defaultLanguage
            }
            return try {
                if (locale.equals("de", true)) {
                    languages["de"]!!.getString(message)
                } else {
                    java.lang.String(languages[locale]!!.getString(message).toByteArray(charset("ISO-8859-1")), "UTF-8").toString()
                }
            } catch (exception: Exception) {
                try {
                    languages[defaultLanguage]!!.getString(message)
                } catch (exception: Exception) {
                    "Unknown string contact a developer if you see this message."
                }
            }
        }

        /**
         * Gets the correct language message for the guild.
         */
        fun get(guild: Guild, message: String): String {
            return get(guild.idLong, message)
        }

        /**
         * Checks if the message exists in the default language.
         */
        fun contains(message: String): Boolean {
            return languages[defaultLanguage]!!.containsKey(message)
        }

    }

}