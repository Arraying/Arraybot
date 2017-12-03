package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.method.templates.ListCollectionMethods;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.backend.annotations.ZeusMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ListMethods extends ListCollectionMethods<Object> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public ListMethods(CommandEnvironment environment) {
        super(environment, "list");
    }

    /**
     * Creates a new list.
     * @return The key.
     */
    @ZeusMethod
    public String list_create() {
        return internalNew(new ArrayList<>());
    }

    /**
     * Gets the size of the list.
     * @param key The key.
     * @return The size or -1.
     */
    @ZeusMethod
    public Integer list_size(String key) {
        return size(key);
    }

    /**
     * Adds a value to the list.
     * @param key The key.
     * @param value The value.
     */
    @ZeusMethod
    public void list_add(String key, String value) {
        add(key, value);
    }

    /**
     * Removes a value from the list.
     * @param key The key.
     * @param value The value.
     */
    @ZeusMethod
    public void list_remove(String key, String value) {
        remove(key, value);
    }

    /**
     * Whether or not the value exists in the list.
     * @param key The key.
     * @param value The value.
     * @return True if it does, false otherwise.
     */
    @ZeusMethod
    public Boolean list_exists(String key, String value) {
        return exists(key, value);
    }

    /**
     * Gets the value from the list.
     * @param key The key.
     * @param index The index.
     * @return The value or "null".
     */
    public Object str_list_get(String key, Integer index) {
        Object returnValue;
        try {
            returnValue = get(key, index);
        } catch(ZeusException exception) {
            returnValue = UDefaults.DEFAULT_NULL;
        }
        return returnValue;
    }

    /**
     * Gets the collection.
     * @return The collection.
     */
    public Map<String, List<Object>> getCollection() {
        return collection;
    }

}
