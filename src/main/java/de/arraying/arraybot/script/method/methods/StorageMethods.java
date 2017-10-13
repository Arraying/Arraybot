package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.data.database.categories.VariablesEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.script.method.Methods;
import de.arraying.zeus.backend.annotations.ZeusMethod;

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
@SuppressWarnings("unused")
public final class StorageMethods extends Methods {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public StorageMethods(CommandEnvironment environment) {
        super(environment);
    }

    /**
     * Gets a variable by identifier.
     * @param identifier The identifier.
     * @return A variable value, as a string.
     */
    @ZeusMethod
    public String vs_get(String identifier) {
        VariablesEntry entry = (VariablesEntry) Category.VARIABLES.getEntry();
        return entry.fetch(entry.getField(identifier), environment.getGuild().getIdLong(), null);
    }

    /**
     * Sets a variable.
     * @param identifier The identifier.
     * @param value The new value.
     * @return The new value.
     */
    @ZeusMethod
    public String vs_set(String identifier, Object value) {
        String newValue = value.toString();
        VariablesEntry entry = (VariablesEntry) Category.VARIABLES.getEntry();
        entry.push(entry.getField(identifier), environment.getGuild().getIdLong(), null, newValue);
        return newValue;
    }

}
