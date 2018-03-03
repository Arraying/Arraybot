package de.arraying.arraybot.script;

import java.util.HashSet;

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
public final class ClassFilter implements jdk.nashorn.api.scripting.ClassFilter {

    private final HashSet<String> whitelisted = new HashSet<>();

    /**
     * Whether or not the specified class should be exposed to the script.
     * @param name The fully qualified class name.
     * @return True if it should, false otherwise.
     */
    @Override
    public boolean exposeToScripts(String name) {
        return whitelisted.contains(name);
    }

    /**
     * Whitelists a class for Nashorn usage.
     * @param clazz The class.
     */
    void whitelist(Class<?> clazz) {
        whitelisted.add(clazz.getName());
    }

}
