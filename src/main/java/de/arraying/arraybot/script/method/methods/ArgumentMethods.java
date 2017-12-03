package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.method.templates.CollectionManagementMethods;
import de.arraying.arraybot.util.UArguments;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
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
public final class ArgumentMethods extends CollectionManagementMethods<String[]> {

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public ArgumentMethods(CommandEnvironment environment) {
        super(environment, "arguments");
    }

    /**
     * Creates new arguments.
     * @param input The input to split.
     * @param regex The regular expression to use when splitting.
     * @return The arguments ID.
     */
    @ZeusMethod
    public String args_new(String input, String regex) {
        return internalNew(input.split(regex));
    }

    /**
     * Gets the length of a set of arguments.
     * @param argumentId The ID of the arguments.
     * @return The length, or -1 if the arguments don't exist.
     */
    @ZeusMethod
    public Integer args_length(String argumentId) {
        String[] argument = collection.get(argumentId);
        if(argument != null) {
            return argument.length;
        }
        return -1;
    }

    /**
     * Gets the argument value.
     * @param argumentId The argument ID.
     * @param index The index, 0 based.
     * @return The value.
     * @throws ZeusException If the argument ID does not exist or there is an AIooB exception.
     */
    @ZeusMethod
    public String args_value(String argumentId, Integer index)
            throws ZeusException {
        String error;
        try {
            String[] arguments = this.collection.get(argumentId);
            if(arguments == null) {
                error = "That argument ID does not exist.";
            } else {
                return arguments[index];
            }
        } catch(ArrayIndexOutOfBoundsException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
            error = "Index out of bounds (0 based indexing).";
        }
        throw new ZeusException(error);
    }

    /**
     * Concatenates arguments to one big string.
     * @param argumentId The argument OD.
     * @param index The index to start at.
     * @return The string.
     * @throws ZeusException If the argument ID does not exist or the index is invalid.
     */
    @ZeusMethod
    public String args_concat(String argumentId, Integer index)
            throws ZeusException {
        String[] arguments = collection.get(argumentId);
        if(arguments == null) {
            throw new ZeusException("That argument ID does not exist.");
        }
        if(index >= arguments.length) {
            throw new ZeusException("Index is too large");
        }
        return UArguments.combine(arguments, index);
    }

}
