package de.arraying.arraybot.util;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

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
public final class UScript {

    /**
     * Error in chat.
     * @param exception The exception.
     */
    public static void error(CommandEnvironment environment, Exception exception) {
        Message.SCRIPT_ERROR.send(environment.getChannel(), exception.getClass().getName(), exception.getMessage()).queue();
    }

    /**
     * Gets the contents of a URL.
     * @param url The URL.
     * @return The value.
     */
    public static String valueOf(String url) {
        try {
            return IOUtils.toString(new URL(url), Charset.forName("utf-8"));
        } catch(IOException exception) {
            exception.printStackTrace();
            return "";
        }
    }

    /**
     * Gets the ID part of a URL.
     * @param url The URL (MUST be a valid URL)
     * @return The ID.
     */
    public static String getID(String url) {
        return url.substring(url.lastIndexOf('/'));
    }

}