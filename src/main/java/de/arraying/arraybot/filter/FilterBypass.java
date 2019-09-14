package de.arraying.arraybot.filter;

import de.arraying.arraybot.data.database.categories.FilterBypassEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.templates.SetEntry;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;

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
public final class FilterBypass {

    private final FilterBypassType type;
    private final long value;

    /**
     * Creates a new filter bypass.
     * @param type The type.
     * @param value The value.
     */
    private FilterBypass(FilterBypassType type, long value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets the filter bypass type from Redis.
     * @param guild The guild.
     * @param id The unique bypass ID.
     * @return A bypass type, presuming the ID is valid.
     */
    public static FilterBypass fromRedis(Guild guild, int id) {
        long guildId = guild.getIdLong();
        FilterBypassEntry entry = (FilterBypassEntry) Category.FILTER_BYPASS.getEntry();
        String rawType = entry.fetch(entry.getField(FilterBypassEntry.Fields.TYPE), guildId, id);
        long value = Long.valueOf(entry.fetch(entry.getField(FilterBypassEntry.Fields.VALUE), guildId, id));
        return new FilterBypass(FilterBypassType.fromString(rawType), value);
    }

    /**
     * Gets all filter bypasses from Redis.
     * @param guild The guild.
     * @return A list of bypasses.
     */
    static List<FilterBypass> getAll(Guild guild) {
        SetEntry entries = (SetEntry) Category.FILTER_BYPASS_IDS.getEntry();
        List<FilterBypass> bypasses = new ArrayList<>();
        for(String value : entries.values(guild.getIdLong())) {
            bypasses.add(FilterBypass.fromRedis(guild, Integer.valueOf(value)));
        }
        return bypasses;
    }

    /**
     * Gets the bypass type.
     * @return The type.
     */
    public FilterBypassType getType() {
        return type;
    }

    /**
     * Gets the bypass value.
     * @return The value.
     */
    public long getValue() {
        return value;
    }

}
