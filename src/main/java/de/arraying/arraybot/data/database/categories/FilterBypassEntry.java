package de.arraying.arraybot.data.database.categories;

import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.core.EntryField;
import de.arraying.arraybot.data.database.templates.HashEntry;
import de.arraying.arraybot.data.database.templates.SetEntry;
import de.arraying.arraybot.filter.FilterBypassType;
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
public final class FilterBypassEntry extends HashEntry<FilterBypassEntry.Fields> {

    private final HashMap<Fields, EntryField> fields = new HashMap<>();

    /**
     * Creates a new filter bypass entry.
     */
    public FilterBypassEntry() {
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

    /**
     * Gets the parent.
     * @return The parent category,
     */
    @Override
    public Category getParent() {
        return Category.FILTER_BYPASS_IDS;
    }

    /**
     * Creates a filter bypass.
     * @param guild The guild.
     * @param id The ID.
     * @param type The type.
     * @param value The value.
     */
    public void createBypass(long guild, int id, FilterBypassType type, long value) {
        SetEntry parent = (SetEntry) getParent().getEntry();
        parent.add(id, id);
        push(getField(Fields.BYPASS_ID), guild, id, id);
        push(getField(Fields.TYPE), guild, id, type);
        push(getField(Fields.VALUE), guild, id, value);
    }

    public enum Fields {

        /**
         * The bypass' per guild unique ID.
         */
        BYPASS_ID(new EntryField("bypass_id", UDefaults.DEFAULT_ID)),

        /**
         * The bypass type.
         */
        TYPE(new EntryField("type", UDefaults.DEFAULT_BYPASS_TYPE)),

        /**
         * The bypass value.
         */
        VALUE(new EntryField("value", UDefaults.DEFAULT_SNOWFLAKE));

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
