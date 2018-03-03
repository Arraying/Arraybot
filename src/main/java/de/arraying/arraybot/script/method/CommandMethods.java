package de.arraying.arraybot.script.method;

import de.arraying.arraybot.command.Command;
import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.command.Commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright 2018 Arraying
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
@SuppressWarnings({"WeakerAccess", "unused"})
public final class CommandMethods {

    private final CommandEnvironment environment;

    /**
     * Creates a new commands method wrapper.
     * @param environment The command environment.
     */
    public CommandMethods(CommandEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Executes a command.
     * @param name The name of the command.
     */
    public void executeCommand(String name) {
        executeCommand(name, new String[]{});
    }

    /**
     * Executes a command.
     * @param name The name of the command.
     * @param arguments The arguments, without the command name.
     */
    public void executeCommand(String name, String[] arguments) {
        Command command = Commands.INSTANCE.getCommands().getByKeyOrAlias(name.toLowerCase());
        if(command == null) {
            return;
        }
        List<String> parts = new LinkedList<>();
        parts.add(name);
        parts.addAll(Arrays.asList(arguments));
        Commands.INSTANCE.invokeThroughKotlin(command, environment, parts);
    }

}
