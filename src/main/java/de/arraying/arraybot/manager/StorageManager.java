package de.arraying.arraybot.manager;

import de.arraying.arraybot.parameter.DataStorage;
import de.arraying.arraybot.parameter.storage.CustomCommandStorage;

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
public final class StorageManager {

    private final DataStorage<CustomCommandStorage> customCommandStorageDataStorage = new DataStorage<>(CustomCommandStorage.class);

    /**
     * Gets the custom command storage.
     * @return The storage.
     */
    public DataStorage<CustomCommandStorage> getCustomCommandStorageDataStorage() {
        return customCommandStorageDataStorage;
    }

}
