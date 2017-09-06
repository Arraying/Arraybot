package de.arraying.arraybot.data.database.categories;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.database.core.EntryField;
import de.arraying.arraybot.data.database.templates.HashEntry;
import de.arraying.arraybot.util.UDefaults;

import java.util.HashMap;

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
public final class GuildEntry extends HashEntry<GuildEntry.Fields> {

    private final HashMap<Fields, EntryField> fields = new HashMap<>();

    /**
     * Creates a new guild entry.
     */
    public GuildEntry() {
        super(Category.GUILD);
        for(Fields field : Fields.values()) {
            fields.put(field, field.field);
        }
    }
    /**
     * Gets a field by key.
     * @param key The key.
     * @return A field.
     */
    @Override
    public EntryField getField(Fields key) {
        return fields.get(key);
    }

    public enum Fields {

        /**
         * The guild's prefix.
         */
        PREFIX(new EntryField("prefix", Arraybot.getInstance().getConfiguration().getBotPrefix())),

        /**
         * The guild's bot language.
         */
        LANGUAGE(new EntryField("language", Arraybot.getInstance().getConfiguration().getBotLanguage())),

        /**
         * The join announcer toggle.
         */
        JOIN_ANNOUNCER(new EntryField("join_announcer", false)),

        /**
         * The join channel ID.
         */
        JOIN_CHANNEL(new EntryField("join_channel", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The join message.
         */
        JOIN_MESSAGE(new EntryField("join_message", UDefaults.DEFAULT_NULL)),

        /**
         * The leave announcer toggle.
         */
        LEAVE_ANNOUNCER(new EntryField("leave_announcer", false)),

        /**
         * The leave channel ID.
         */
        LEAVE_CHANNEL(new EntryField("leave_channel", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The leave message.
         */
        LEAVE_MESSAGE(new EntryField("leave_message", UDefaults.DEFAULT_NULL)),

        /**
         * The announcements toggle.
         */
        ANNOUNCEMENT_ANNOUNCER(new EntryField("announcement_announcer", false)),

        /**
         * The announcements channel.
         */
        ANNOUNCEMENT_CHANNEL(new EntryField("announcement_channel", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The autorole toggle.
         */
        AUTOROLE_ENABLED(new EntryField("autorole_enabled", false)),

        /**
         * Whether or not autorole should occur on first message rather
         * than on guild join.
         */
        AUTOROLE_MESSAGE(new EntryField("autorole_message", false)),

        /**
         * The autorole role ID.
         */
        AUTOROLE_ROLE(new EntryField("autorole_role", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The filter toggle.
         */
        FILTER_ENABLED(new EntryField("filter_enabled", false)),

        /**
         * The filter regular expression toggle.
         */
        FILTER_REGEX(new EntryField("filter_regex", false)),

        /**
         * The filter silent message toggle.
         */
        FILTER_SILENT(new EntryField("filter_silent", false)),

        /**
         * The filter private message toggle.
         */
        FILTER_PRIVATE(new EntryField("filter_private", false)),

        /**
         * The filter message.
         */
        FILTER_MESSAGE(new EntryField("filter_message", UDefaults.DEFAULT_NULL)),

        /**
         * The mute role.
         */
        MUTE_ROLE(new EntryField("mute_permission", UDefaults.DEFAULT_MUTE_PERMISSION)),

        /**
         * The mute permission.
         * This can be a role ID or Permissions object.
         */
        MUTE_PERMISSION(new EntryField("mute_role", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The incrementing latest announcement ID.
         */
        COUNT_ANNOUNCEMENT(new EntryField("count_announcements", UDefaults.DEFAULT_COUNT)),

        /**
         * The incrementing latest bypass ID.
         */
        COUNT_BYPASS(new EntryField("count_bypass", UDefaults.DEFAULT_COUNT)),

        /**
         * The incrementing latest filter ID.
         */
        COUNT_FILTER(new EntryField("count_filter", UDefaults.DEFAULT_COUNT)),

        /**
         * The incrementing latest punishment ID.
         */
        COUNT_PUNISHMENT(new EntryField("count_punishment", UDefaults.DEFAULT_COUNT));
        
        private final EntryField field;

        /**
         * Sets the entry field.
         * @param field The field.
         */
        Fields(EntryField field) {
            this.field = field;
        }
        
    }
    
}
