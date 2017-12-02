package de.arraying.arraybot.util;

import de.arraying.arraybot.data.database.core.Category;

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
public final class UDatabase {

    /**
     * The Redis key for the commands executed.
     */
    public static final String COMMANDS_KEY = "commands";

    /**
     * The Redis key for the messages received.
     */
    public static final String MESSAGES_KEY = "messages";

    /**
     * Combines things to form one big key.
     * @param category The category.
     * @param primaryKey The primary key.
     * @return A string key.
     */
    public static String getKey(Category category, Object primaryKey) {
        return getKey(category, primaryKey, null);
    }

    /**
     * Combines things to form one big key.
     * @param category The category.
     * @param primaryKey The primary key.
     * @param secondaryKey The secondary key. Can be null.
     * @return A string key.
     */
    public static String getKey(Category category, Object primaryKey, Object secondaryKey) {
        String key = category.getPrefix() + "_" + primaryKey.toString();
        if(secondaryKey != null) {
            key = key + "_" + secondaryKey.toString();
        }
        return key;
    }

}
