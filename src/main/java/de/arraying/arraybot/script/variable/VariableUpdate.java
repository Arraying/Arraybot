package de.arraying.arraybot.script.variable;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.zeus.variable.ZeusVariable;

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
public final class VariableUpdate {

    private final ZeusVariable variable;
    private final CommandEnvironment environment;
    private final int lineNumber;

    /**
     * Creates a new variable update object.
     * @param variable The variable that has been updated.
     * @param environment The environment.
     * @param lineNumber The line number.
     */
    public VariableUpdate(ZeusVariable variable, CommandEnvironment environment, int lineNumber) {
        this.variable = variable;
        this.environment = environment;
        this.lineNumber = lineNumber;
    }

    /**
     * Gets the variable.
     * @return The variable.
     */
    public ZeusVariable getVariable() {
        return variable;
    }

    /**
     * Gets the environment.
     * @return The environment.
     */
    public CommandEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Gets the line number.
     * @return The line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

}
