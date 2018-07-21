package de.arraying.arraybot.data.database.categories;

import de.arraying.arraybot.data.database.core.Category;
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
public final class PunishmentEntry extends HashEntry<PunishmentEntry.Fields> {

    private final HashMap<Fields, EntryField> fields = new HashMap<>();

    /**
     * Creates a new punishment entry.
     */
    public PunishmentEntry() {
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
     * @return The parent category.
     */
    @Override
    public Category getParent() {
        return null;
    }

    /**
     * Sets the category.
     */
    @Override
    public void setCategory() {
        this.category = Category.PUNISHMENT;
    }

    public enum Fields {

        /**
         * The ID of the punishment.
         */
        PUNISHMENT_ID(new EntryField("punishment_id", UDefaults.DEFAULT_ID)),

        /**
         * The ID of the punished user.
         */
        USER(new EntryField("user", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The punishment type.
         */
        TYPE(new EntryField("type", UDefaults.DEFAULT_PUNISHMENT_TYPE)),

        /**
         * The ID of the punisher.
         */
        STAFF(new EntryField("staff", UDefaults.DEFAULT_SNOWFLAKE)),

        /**
         * The expiration of the punishment, in milliseconds.
         */
        EXPIRATION(new EntryField("expiration", -1)),

        /**
         * Whether or not the punishment has been revoked yet.
         */
        REVOKED(new EntryField("revoked", true)),

        /**
         * The punishment reason.
         */
        REASON(new EntryField("reason", UDefaults.DEFAULT_NULL));

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
