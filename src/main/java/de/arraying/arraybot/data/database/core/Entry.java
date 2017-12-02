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
     * Sets the category.
     * This cannot be done in the constructor otherwise the category would be null.
     * @param category The category.
     */
    void setCategory(Category category);

    /**
     * Deletes everything corresponding to the ID.
     * @param id The ID.
     */
    void deleteGuild(long id);

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

}
