package de.arraying.arraybot.script.provider;

import de.arraying.prime.PrimeSourceProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

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
public final class StandardProvider implements PrimeSourceProvider {

    /**
     * The standard library pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("[a-zA-Z]+");

    /**
     * Gets the pattern.
     * @return The pattern.
     */
    @Override
    public Pattern getIncludePattern() {
        return PATTERN;
    }

    /**
     * Gets the source.
     * @param s The path.
     * @return The source.
     */
    @Override
    public String getSource(String s) {
        String toReturn = "";
        try {
            File standardLibrary = new File("stdlib");
            if(!standardLibrary.exists()
                    || !standardLibrary.isDirectory()) {
                return toReturn;
            }
            File file = new File(standardLibrary, s + ".js");
            if(!file.exists()) {
                return toReturn;
            }
            toReturn = new String(Files.readAllBytes(file.toPath()));
        } catch(IOException exception) {
            exception.printStackTrace();
        }
        return toReturn;
    }

}
