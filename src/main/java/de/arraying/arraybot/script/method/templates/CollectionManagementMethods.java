package de.arraying.arraybot.script.method.templates;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.script.method.Methods;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class CollectionManagementMethods<T> extends Methods {

    protected Map<String, T> collection = new ConcurrentHashMap<>();
    private final String identifier;
    private int index = -1;

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     * @param identifier A general identifier category.
     */
    public CollectionManagementMethods(CommandEnvironment environment, String identifier) {
        super(environment);
        this.identifier = identifier;
    }

    /**
     * Creates a new entry value internally.
     * @param value The value.
     * @return A copy of the key.
     */
    protected String internalNew(T value) {
        String key = newKey();
        collection.put(key, value);
        return key;
    }

    /**
     * Generates a new unique key.
     * @return The key.
     */
    private String newKey() {
        return identifier + environment.getMessage().getIdLong() + (++index);
    }

}
