package de.arraying.arraybot.script;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.method.methods.ArgumentMethods;
import de.arraying.arraybot.script.method.methods.EmbedMethods;
import de.arraying.arraybot.script.method.methods.MessageMethods;
import de.arraying.arraybot.script.method.methods.StorageMethods;
import de.arraying.arraybot.script.variable.VariableCollections;
import de.arraying.arraybot.script.variable.Variables;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntime;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
import net.dv8tion.jda.core.EmbedBuilder;
import org.slf4j.Logger;

import java.lang.reflect.Method;
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
public final class ScriptRuntime extends AbstractTask {

    private final CommandEnvironment environment;
    private final String[] code;


    /**
     * Creates a new script runtime.
     * @param environment The environment.
     * @param code The code.
     */
    public ScriptRuntime(CommandEnvironment environment, String[] code) {
        super("Script-" + environment.getMessage().getIdLong());
        this.environment = environment;
        this.code = code;
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
            builder.withEventListeners(new ScriptListener(this))
                    .withMethods(new ArgumentMethods(environment),
                            embedMethods,
                            new MessageMethods(environment, embedMethods),
                            new StorageMethods(environment));
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
