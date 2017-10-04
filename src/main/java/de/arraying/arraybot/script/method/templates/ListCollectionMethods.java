package de.arraying.arraybot.script.method.templates;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.zeus.backend.ZeusException;

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
public class ListCollectionMethods<T> extends CollectionManagementMethods<List<T>> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     * @param identifier A general identifier category.
     */
    public ListCollectionMethods(CommandEnvironment environment, String identifier) {
        super(environment, identifier);
    }

    /**
     * Gets the size of the list.
     * @param key The key.
     * @return The size or -1 if the key is invalid.
     */
    protected Integer size(String key) {
        List<T> entries = collection.get(key);
        return entries == null ? -1 : entries.size();
    }

    /**
     * Adds to the list.
     * @param key The key.
     * @param value The value.
     */
    protected void add(String key, T value) {
        List<T> entries = collection.get(key);
        if(entries != null) {
            entries.add(value);
        }
    }

    /**
     * Removes from the list.
     * @param key The key.
     * @param value The value.
     */
    protected void remove(String key, T value) {
        List<T> entries = collection.get(key);
        if(entries != null) {
            entries.remove(value);
        }
    }

    /**
     * Whether or not a value is in a list.
     * @param key The key.
     * @param value The value.
     * @return True if it is, false if it is not or the key is invalid.
     */
    protected Boolean exists(String key, T value) {
        List<T> entries = collection.get(key);
        return entries != null && entries.contains(value);
    }

    /**
     * Gets a value from the list.
     * @param key The key.
     * @param index The index.
     * @return A value.
     * @throws ZeusException If the key/index is invalid or the value is null.
     */
    protected T get(String key, Integer index)
            throws ZeusException {
        List<T> entries = collection.get(key);
        if(entries == null) {
            throw new ZeusException("A list with that key does not exist.");
        }
        if(index < 0) {
            throw new ZeusException("The index cannot be smaller than 0.");
        }
        T entry = entries.get(index);
        if(entry == null) {
            throw new ZeusException("The list does not contain a value with that index.");
        }
        return entry;
    }

}
