package de.arraying.arraybot.script.provider;

import de.arraying.arraybot.util.UScript;
import de.arraying.prime.PrimeSourceProvider;

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
public final class GistProvider implements PrimeSourceProvider {

    /**
     * The GitHub Gist pattern.
     */
    private static final Pattern PATTERN = Pattern.compile("http(s)?://gist\\.github\\.com/[0-9a-zA-Z]+/[0-9a-zA-Z]+/raw");

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
        return UScript.valueOf(s);
    }

}
