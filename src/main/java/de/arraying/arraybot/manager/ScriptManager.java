package de.arraying.arraybot.manager;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.ScriptEvaluator;
import de.arraying.arraybot.script.entity.ScriptGuild;
import de.arraying.arraybot.script.entity.ScriptMessage;
import de.arraying.arraybot.script.entity.ScriptTextChannel;
import de.arraying.arraybot.script.entity.ScriptUser;
import de.arraying.arraybot.script.method.CommandMethods;
import de.arraying.arraybot.script.method.EmbedMethods;
import de.arraying.arraybot.script.method.ManagerMethods;
import de.arraying.arraybot.script.method.StorageMethods;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.regex.Pattern;

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

    private final Pattern pastebin = Pattern.compile("^(http(s)?://pastebin\\.com/raw/[0-9a-zA-Z]+)$");

    /**
     * Whether or not the provided script URL is valid.
     * @param value The value.
     * @return True if it is, false otherwise.
     */
    public boolean isValid(String value) {
        return pastebin.matcher(value).find();
    }

    /**
     * Executes the script.
     * @param scriptUrl The script URL.
     * @param environment The command environment.
     * @throws IOException If an exception occurs parsing the code.
     */
    public void executeScript(String scriptUrl, CommandEnvironment environment)
            throws Exception {
        String code = IOUtils.toString(new URL(scriptUrl), Charset.forName("utf-8"));
        new ScriptEvaluator(code)
                .variable("guild", new ScriptGuild(environment, environment.getGuild()))
                .variable("channel", new ScriptTextChannel(environment, environment.getChannel()))
                .variable("user", new ScriptUser(environment, environment.getMember()))
                .variable("message", new ScriptMessage(environment, environment.getMessage()))
                .variable("embeds", new EmbedMethods())
                .variable("commands", new CommandMethods(environment))
                .variable("manager", new ManagerMethods(environment))
                .variable("storage", new StorageMethods(environment))
                .variable("time", Instant.now())
                .evaluate();
    }

}
