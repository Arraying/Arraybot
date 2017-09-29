package de.arraying.arraybot.script;

import de.arraying.arraybot.script.variable.VariableCollections;
import de.arraying.arraybot.script.variable.VariableUpdate;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.event.ZeusEventListener;
import de.arraying.zeus.event.events.VariableReassignEvent;

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
public final class ScriptListener extends ZeusEventListener {

    private final ScriptRuntime runtime;

    /**
     * Creates a new script listener.
     * @param runtime The script runtime.
     */
    ScriptListener(ScriptRuntime runtime) {
        this.runtime = runtime;
    }

    /**
     * When a variable is reassigned. This event is used to easily change things.
     * @param event The event.
     */
    @Override
    public void onVariableReassign(VariableReassignEvent event) {
        boolean change;
        for(VariableCollections variables : VariableCollections.values()) {
            change = variables.getVariables().onChange(new VariableUpdate(event.getVariable(), runtime.getEnvironment(), event.getLineNumber()));
            if(change) {
                return;
            }
        }
    }

}
