package de.arraying.arraybot.parameter;

import java.lang.reflect.Constructor;
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
public class DataStorage<T> {

    private final Map<Long, T> storage = new ConcurrentHashMap<>();
    private final Class<T> clazz;

    /**
     * Creates a new data storage.
     * @param clazz The class corresponding to type T.
     */
    public DataStorage(Class<T> clazz) {
        if(!canBeInstantiated(clazz)) {
            throw new IllegalArgumentException("Inputted class does not have an empty constructor.");
        }
        this.clazz = clazz;
    }

    /**
     * Creates a new data storage entry.
     * @param id The ID of the entry.
     */
    public void create(Long id) {
        try {
            T t = clazz.newInstance();
            storage.put(id, t);
        } catch(InstantiationException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets a data storage entry.
     * @param id The ID of the entry.
     * @return The entry, or null if it does not exist.
     */
    public T get(Long id) {
        return storage.get(id);
    }

    /**
     * Removes a data storage entry.
     * @param id The ID of the entry.
     */
    public void remove(Long id) {
        storage.remove(id);
    }


    /**
     * Checks whether the class can be instantiated with an empty constructor.
     * @param clazz The class.
     * @return True if it can, false otherwise.
     */
    private boolean canBeInstantiated(Class<T> clazz) {
        for(Constructor<?> constructor : clazz.getConstructors()) {
            if(constructor.getParameterCount() == 0) {
                return true;
            }
        }
        return false;
    }

}
