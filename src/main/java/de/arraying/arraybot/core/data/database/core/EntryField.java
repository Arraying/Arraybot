package de.arraying.arraybot.core.data.database.core;

import lombok.Data;
import lombok.NonNull;

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
public @Data class EntryField {

    private final String redisKey;
    private final @NonNull Object defaultValue;

    /**
     * Creates a new entry field.
     * @param redisKey The Redis key.
     * @param defaultValue The default value.
     */
    public EntryField(String redisKey, @NonNull Object defaultValue) {
        this.redisKey = redisKey;
        this.defaultValue = defaultValue;
    }

}
