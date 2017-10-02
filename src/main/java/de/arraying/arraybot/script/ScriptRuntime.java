package de.arraying.arraybot.script;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.method.methods.*;
import de.arraying.arraybot.script.variable.VariableCollections;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntime;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
import de.arraying.zeus.variable.ZeusVariable;

import java.lang.reflect.Method;

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
public final class ScriptRuntime extends AbstractTask {

    private final CommandEnvironment environment;
    private final String[] code;
    private final ZeusVariable[] variables;

    /**
     * Creates a new script runtime.
     * @param environment The environment.
     * @param code The code.
     * @param variables An array of predefined variables.
     */
    public ScriptRuntime(CommandEnvironment environment, String[] code, ZeusVariable... variables) {
        super("Script-" + environment.getMessage().getIdLong());
        this.environment = environment;
        this.code = code;
        this.variables = variables;
    }

    /**
     * Executes the code.
     */
    @Override
    public void onExecution() {
        try {
            ZeusRuntimeBuilder builder = new ZeusRuntimeBuilder(ZeusRuntimeBuilder.Configuration.STANDARD);
            for(Method method : UZeus.getIgnored()) {
                builder.withoutMethods(method);
            }
            EmbedMethods embedMethods = new EmbedMethods(environment);
            StringListMethods stringListMethods = new StringListMethods(environment);
            builder.withEventListeners(new ScriptListener(this))
                    .withVariables(variables)
                    .withMethods(new ArgumentMethods(environment),
                            new ChannelMethods(environment),
                            new CommandMethods(environment, stringListMethods),
                            embedMethods,
                            new MessageMethods(environment, embedMethods),
                            new RoleMethods(environment),
                            new StorageMethods(environment),
                            stringListMethods,
                            new UserMethods(environment));
            for(VariableCollections variables : VariableCollections.values()) {
                builder = variables.getVariables().registerVariables(builder, environment);
            }
            ZeusRuntime runtime = builder.build();
            runtime.evaluate(code, error -> {
                String errorMessage = Message.ZEUS_ERROR.content(environment.getChannel(), false)
                        .replace("{line}", String.valueOf(error.getLineNumber()))
                        .replace("{error}", error.getMessage());
                environment.getChannel().sendMessage(errorMessage).queue();
            });
        } catch(ZeusException exception) {
            logger.error("An error occurred.", exception);
        }
    }

    /**
     * Gets the environment.
     * @return The command environment.
     */
    public CommandEnvironment getEnvironment() {
        return environment;
    }

}
