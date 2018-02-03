package de.arraying.arraybot.manager;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.ScriptRuntime;
import de.arraying.arraybot.script2.ScriptEvaluator;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
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
            throws IOException {
        String content = IOUtils.toString(new URL(scriptUrl), Charset.forName("utf-8"));
        String[] code = content.split("\n");
        executeStringRaw(code, environment);
    }

    /**
     * Executes the script.
     * @param scriptUrl The script URL.
     * @param environment The command environment.
     * @throws IOException If an exception occurs parsing the code.
     */
    public void executeScript2(String scriptUrl, CommandEnvironment environment)
            throws Exception {
        String code = IOUtils.toString(new URL(scriptUrl), Charset.forName("utf-8"));
        new ScriptEvaluator(code)
                .variable("user", environment.getAuthor())
                .variable("messenger", new MessageMethods(environment))
                .evaluate();
    }

    /**
     * Executes the script with a raw parameter.
     * @param code The code.
     * @param environment The command environment.
     */
    public void executeStringRaw(String[] code, CommandEnvironment environment) {
        new ScriptRuntime(environment, code).create();
    }

}
