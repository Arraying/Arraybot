package de.arraying.arraybot.manager;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.entity.ScriptGuild;
import de.arraying.arraybot.script.entity.ScriptMessage;
import de.arraying.arraybot.script.entity.ScriptTextChannel;
import de.arraying.arraybot.script.entity.ScriptUser;
import de.arraying.arraybot.script.method.CommandMethods;
import de.arraying.arraybot.script.method.EmbedMethods;
import de.arraying.arraybot.script.method.ManagerMethods;
import de.arraying.arraybot.script.method.StorageMethods;
import de.arraying.arraybot.script.provider.GistProvider;
import de.arraying.arraybot.script.provider.PastebinProvider;
import de.arraying.arraybot.script.provider.StandardProvider;
import de.arraying.prime.Prime;
import de.arraying.prime.PrimeSourceProvider;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.Arrays;
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
public final class ScriptManager {

    /**
     * All providers.
     */
    private static final PrimeSourceProvider[] PROVIDERS = new PrimeSourceProvider[] {
            new GistProvider(),
            new PastebinProvider(),
            new StandardProvider(),
    };

    /**
     * A Prime instance just for testing, this should never evaluate.
     */
    private static final Prime PRIME_TEST;

    static {
        Prime.Builder builder = new Prime.Builder();
        Arrays.stream(PROVIDERS).forEach(builder::withProvider);
        PRIME_TEST = builder.build("print('Something went really wrong...')");
    }

    /**
     * Whether or not the provided script URL is valid.
     * @param value The value.
     * @return True if it is, false otherwise.
     */
    public boolean isValid(String value) {
        return Prime.Util.getMatches(PRIME_TEST, value) != null;
    }

    /**
     * Executes the script.
     * @param scriptUrl The script URL.
     * @param environment The command environment.
     * @param error The error consumer.
     * @throws IOException If an exception occurs parsing the code.
     */
    public void executeScript(String scriptUrl, CommandEnvironment environment, Consumer<Exception> error)
            throws Exception {
        String code = IOUtils.toString(new URL(scriptUrl), Charset.forName("utf-8"));
        Prime.Builder primeBuilder = new Prime.Builder()
                .withVariable("guild", new ScriptGuild(environment, environment.getGuild()))
                .withVariable("channel", new ScriptTextChannel(environment, environment.getChannel()))
                .withVariable("user", new ScriptUser(environment, environment.getMember()))
                .withVariable("message", new ScriptMessage(environment, environment.getMessage()))
                .withVariable("embeds", new EmbedMethods())
                .withVariable("commands", new CommandMethods(environment))
                .withVariable("manager", new ManagerMethods(environment))
                .withVariable("storage", new StorageMethods(environment))
                .withVariable("time", Instant.now());
        Arrays.stream(PROVIDERS).forEach(primeBuilder::withProvider);
        Prime prime = primeBuilder.build(code);
        prime.evaluate(error);
    }

}
