package de.arraying.arraybot.data.database.core;


import de.arraying.arraybot.data.database.categories.*;
import de.arraying.arraybot.data.database.templates.KVEntry;
import de.arraying.arraybot.data.database.templates.SetEntry;

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
     * Sets the category.
     * This cannot be done in the constructor otherwise the category would be null.
     * @param category The category.
     */
    void setCategory(Category category);

    /**
     * Deletes everything corresponding to the ID.
     * @param id The ID.
     */
    void delete(long id);

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
        ANNOUNCEMENT_IDS("ai", new SetEntry()),

        /**
         * An announcement for a specific guild.
         * Hash.
         */
        ANNOUNCEMENT("a", new AnnouncementEntry()),

        /**
         * A collection of all blacklisted user IDs.
         * Set.
         */
        BLACKLIST("b", new SetEntry()),

        /**
         * A collection of all custom command names in a guild.
         * Set.
         */
        CUSTOM_COMMAND_NAMES("cn", new SetEntry()),

        /**
         * A custom command for a specific guild.
         * Hash.
         */
        CUSTOM_COMMAND("c", new CustomCommandEntry()),

        /**
         * A disabled command name for a specific guild.
         * Set.
         */
        DISABLED_COMMAND("d", new SetEntry()),

        /**
         * A collection of filtered phrases.
         * Set.
         */
        FILTER("f", new SetEntry()),

        /**
         * A collection of all filter bypass IDs.
         * Set.
         */
        FILTER_BYPASS_IDS("fbi", new SetEntry()),

        /**
         * A filter bypass for a specific guild.
         * Hash.
         */
        FILTER_BYPASS("fb", new FilterBypassEntry()),

        /**
         * A specific guild containing all properties.
         * Hash.
         */
        GUILD("g", new GuildEntry()),

        /**
         * A collection of punishment IDs.
         * Set.
         */
        PUNISHMENT_IDS("pi", new SetEntry()),

        /**
         * A punishment for a specific guild.
         * Hash.
         */
        PUNISHMENT("p", new PunishmentEntry()),

        /**
         * Any misc. things that need to be stored.
         * KV.
         */
        MISC("m", new KVEntry());

        private final String prefix;
        private final Entry entry;

        /**
         * Sets the prefix for the category.
         * @param prefix The prefix.
         * @param entry The entry to use.
         */
        Category(String prefix, Entry entry) {
            this.prefix = prefix;
            this.entry = entry;
        }

        /**
         * Gets the prefix.
         * @return The prefix.
         */
        public String getPrefix() {
            return prefix;
        }


        /**
         * Gets the entry.
         * @return The entry.
         */
        public Entry getEntry() {
            return entry;
        }

    }


}
