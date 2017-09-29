package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.method.Methods;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.core.entities.TextChannel;

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
@SuppressWarnings("unused")
public final class ArgumentMethods extends Methods {

    private final Map<String, String[]> arguments = new ConcurrentHashMap<>();
    private int argumentIndex = -1;

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public ArgumentMethods(CommandEnvironment environment) {
        super(environment);
    }

    /**
     * Creates new arguments.
     * @param input The input to split.
     * @param regex The regular expression to use when splitting.
     * @return The arguments ID.
     */
    @ZeusMethod
    public String args_new(String input, String regex) {
        String id = UZeus.createId("arguments", environment.getMessage().getIdLong(), newArgumentIndex());
        arguments.put(id, input.split(regex));
        return id;
    }

    /**
     * Gets the length of a set of arguments.
     * @param argumentId The ID of the arguments.
     * @return The length, or -1 if the arguments don't exist.
     */
    @ZeusMethod
    public Integer args_length(String argumentId) {
        String[] argument = arguments.get(argumentId);
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
            String[] arguments = this.arguments.get(argumentId);
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
     * Gets a new unique arguments ID.
     * @return An integer.
     */
    private int newArgumentIndex() {
        return ++argumentIndex;
    }

}
