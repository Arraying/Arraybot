package de.arraying.arraybot.cache.storage

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class DataStorage<T> {

    private val storage = HashMap<Long, T>()

    /**
     * Temporarily stores into the storage.
     */
    fun store(id: Long, data: T) {
        if(!storage.containsKey(id)) {
            storage.put(id, data)
        }
    }

    /**
     * Retrieves data from the storage.
     */
    fun retrieve(id: Long): T? {
        return storage[id]
    }

    /**
     * Removes the data from the storage when it is no longer needed.
     */
    fun prune(id: Long) {
        if(storage.containsKey(id)) {
            storage.remove(id)
        }
    }

}