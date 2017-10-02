package de.arraying.arraybot.script.variable;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
import de.arraying.zeus.utils.ZeusVariableUtil;
import de.arraying.zeus.variable.VariableType;
import de.arraying.zeus.variable.ZeusVariable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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
public abstract class Variables {

    private final Map<String, Consumer<VariableUpdate>> updateActions = new ConcurrentHashMap<>();



    /**
     * Registers all variables.
     * @param builder The current runtime builder.
     * @param environment The command environment to have access to objects.
     * @return A builder with registered entries.
     * @throws ZeusException If an error occurs.
     */
    public abstract ZeusRuntimeBuilder registerVariables(ZeusRuntimeBuilder builder, CommandEnvironment environment)
            throws ZeusException;

    /**
     * The change event.
     * @param update The update object.
     * @return True if an event was found and executed, false otherwise.
     */
    public boolean onChange(VariableUpdate update) {
        Consumer<VariableUpdate> consumer = updateActions.get(update.getVariable().identifier());
        if(consumer == null) {
            return false;
        }
        consumer.accept(update);
        return true;
    }

    /**
     * Registers a new variable change event consumer.
     * @param name The name of the variable.
     * @param consumer The action.
     */
    protected void registerVariableEvent(String name, Consumer<VariableUpdate> consumer) {
        updateActions.put(name, consumer);
    }

    /**
     * Quickly creates a constant.
     * @param name The name.
     * @param value The value.
     * @return The constant.
     * @throws ZeusException If an error occurs.
     */
    protected ZeusVariable createConstant(String name, Object value)
            throws ZeusException {
        return ZeusVariableUtil.createVariable(VariableType.CONSTANT, name, value);
    }

    /**
     * Quickly creates a mutable variable.
     * @param name The name.
     * @param value The value.
     * @return The mutable variable.
     * @throws ZeusException If an error occurs.
     */
    protected ZeusVariable createMutable(String name, Object value)
            throws ZeusException {
        return ZeusVariableUtil.createVariable(VariableType.MUTABLE, name, value);
    }

}
