package de.arraying.arraybot.language

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import net.dv8tion.jda.core.entities.Guild
import java.io.File
import java.lang.IllegalArgumentException
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
class Language {

    private val filePattern = Pattern.compile("^Language_[a-z]{2,4}_[A-Z]{2,4}\\.properties$")
    private val arraybot = Arraybot.instance

    /**
     * Loads in and registers all language.
     */
    init {
        val directory = File("languages")
        if(!directory.exists()) {
            directory.mkdirs()
            throw IllegalArgumentException("The language directory did not exist, hence it has been created.")
        }
        val urls = arrayOf(directory.toURI().toURL())
        val classLoader = URLClassLoader(urls)
        for(file in directory.listFiles()) {
            var name = file.name
            val matcher = filePattern.matcher(name)
            if(!matcher.find()) {
                continue
            }
            name = name.substring(9, name.indexOf(".properties"))
            val parts = name.split("_")
            val language = parts[0]
            val region = parts[1]
            languages.put(language, ResourceBundle.getBundle("Language", Locale(language, region), classLoader))
            arraybot.logger.info("Registered the language \"$language\" with the region of \"$region\".")
        }
        if(languages.isEmpty()) {
            throw IllegalArgumentException("No valid language were found.")
        }
        if(!languages.containsKey(arraybot.configuration.botDefaultLanguage)) {
            throw IllegalArgumentException("The default language specified does not exist.")
        }
    }

    companion object {

        val languages = HashMap<String, ResourceBundle>()
        private val defaultLanguage = Arraybot.instance.configuration.botDefaultLanguage

        /**
         * Gets the correct language message for the guild.
         */
        fun get(id: Long, message: String): String {
            val cachedGuild = Cache.guilds[id]
            val locale: String =
                    if(cachedGuild == null
                            || cachedGuild.language == null
                            || !languages.containsKey(cachedGuild.language!!))
                        defaultLanguage
                    else
                        cachedGuild.language!!
            try {
                return if (locale.equals("de", true))
                    languages["de"]!!.getString(message)
                else
                    java.lang.String(languages[locale]!!.getString(message).toByteArray(charset("ISO-8859-1")), "UTF-8").toString()
            } catch(exception: Exception) {
                try {
                    return languages[defaultLanguage]!!.getString(message)
                } catch(exception: Exception) {
                    return "Unknown string, ${Cache.author!!.name}#${Cache.author!!.discriminator} screwed up. Send this to him: " +
                            "\"$message\"."
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