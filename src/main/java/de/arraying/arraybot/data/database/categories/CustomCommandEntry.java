package de.arraying.arraybot.data.database.categories;

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
public final class CustomCommandEntry extends HashEntry<CustomCommandEntry.Fields> {

    private final HashMap<Fields, EntryField> fields = new HashMap<>();

    /**
     * Creates a new announcement entry.
     */
    public CustomCommandEntry() {
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
         * The syntax of the custom command.
         * Used to determine whether or not input is required.
         */
        SYNTAX(new EntryField("syntax", UDefaults.DEFAULT_CUSTOM_COMMAND_SYNTAX)),

        /**
         * The permission of the custom command.
         * This can be a role ID or a Permissions field.
         */
        PERMISSION(new EntryField("permission", UDefaults.DEFAULT_CUSTOM_COMMAND_PERMISSION)),

        /**
         * The custom command type.
         */
        TYPE(new EntryField("type", UDefaults.DEFAULT_CUSTOM_COMMAND_TYPE)),

        /**
         * The description of the custom command.
         */
        DESCRIPTION(new EntryField("description", UDefaults.DEFAULT_NULL)),

        /**
         * The value of the custom command.
         */
        VALUE(new EntryField("value", UDefaults.DEFAULT_STRING));

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
