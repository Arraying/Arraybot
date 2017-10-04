package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.command.Commands;
import de.arraying.arraybot.script.method.Methods;
import de.arraying.zeus.backend.annotations.ZeusMethod;

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
@SuppressWarnings({"unused", "WeakerAccess"})
public class CommandMethods extends Methods {

    private final StringListMethods stringListMethods;

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     * @param stringListMethods The string lists.
     */
    public CommandMethods(CommandEnvironment environment, StringListMethods stringListMethods) {
        super(environment);
        this.stringListMethods = stringListMethods;
    }

    /**
     * Whether or not a command with that name exists.
     * @param name The name.
     * @return True if it does, false otherwise.
     */
    @ZeusMethod
    public Boolean is_command(String name) {
        return Commands.INSTANCE.getCommands().getByKeyOrAlias(name.toLowerCase()) != null;
    }

    /**
     * Executes a command without arguments.
     * @param name The name of the command.
     */
    @ZeusMethod
    public void exec_command(String name) {
        exec_command(name, stringListMethods.str_list_create());
    }

    /**
     * Executes a command.
     * @param name The name of the command.
     * @param key The key of the argument list.
     */
    @ZeusMethod
    public void exec_command(String name, String key) {
        List<String> list = stringListMethods.getCollection().get(key);
        if(key == null) {
            return;
        }
        Commands.INSTANCE.getCommands().getByKeyOrAlias(name.toLowerCase())
                .invoke(environment, list, null);
    }

}
