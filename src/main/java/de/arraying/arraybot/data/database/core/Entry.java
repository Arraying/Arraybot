package de.arraying.arraybot.data.database.core;


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
public interface Entry {

    /**
     * Gets the entry type.
     * @return A field in the Type enumeration.
     */
    Type getType();

    /**
     * Gets the entry category.
     * @return A field in the Category enumeration.
     */
    Category getCategory();

    enum Type {

        /**
         * A hash (similar to an object)
         * Each key has multiple fields.
         */
        HASH,

        /**
         * A standard key-value like storage.
         */
        KV,

        /**
         * A set. Like Java's set, this set can not contain duplicates.
         */
        SET

    }

    enum Category {

        /**
         * A collection of all announcement IDs for a guild.
         * Set.
         */
        ANNOUNCEMENT_IDS("ai"),

        /**
         * An announcement for a specific guild.
         * Hash.
         */
        ANNOUNCEMENT("a"),

        /**
         * A collection of all blacklisted user IDs.
         * Set.
         */
        BLACKLIST("b"),

        /**
         * A collection of all custom command names in a guild.
         * Set.
         */
        CUSTOM_COMMAND_NAMES("cn"),

        /**
         * A custom command for a specific guild.
         * Hash.
         */
        CUSTOM_COMMAND("c"),

        /**
         * A disabled command name for a specific guild.
         * Set.
         */
        DISABLED_COMMAND("d"),

        /**
         * A collection of all filtered phrase IDs.
         * Set.
         */
        FILTER_IDS("fi"),

        /**
         * A collection of filtered phrases.
         * Set.
         */
        FILTER("f"),

        /**
         * A collection of all filter bypass IDs.
         * Set.
         */
        FILTER_BYPASS_IDS("fbi"),

        /**
         * A filter bypass for a specific guild.
         * Hash.
         */
        FILTER_BYPASS("fb"),

        /**
         * A specific guild containing all properties.
         * Hash.
         */
        GUILD("g"),

        /**
         * A collection of punishment IDs.
         * Set.
         */
        PUNISHMENT_IDS("pi"),

        /**
         * A punishment for a specific guild.
         * Hash.
         */
        PUNISHMENT("p"),

        /**
         * Any misc. things that need to be stored.
         * KV.
         */
        MISC("m");

        private final String prefix;

        /**
         * Sets the prefix for the category.
         * @param prefix The prefix.
         */
        Category(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Gets the prefix.
         * @return The prefix.
         */
        public String getPrefix() {
            return prefix;
        }
    }


}
