package de.arraying.arraybot.util;

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
public final class UParameter {

    /**
     * Gets the parameter value of given input string.
     * For example, "-parameter hello" would return hello,
     * if the parameter was set to -parameter.
     * @param input The string to take the value from.
     * @param parameter The parameter.
     * @return The parameter value, or "".
     */
    public static String getParameterValue(final String input, final String parameter) {
        String substring = input.substring(input.indexOf(parameter) + parameter.length());
        int spaceIndex = substring.indexOf(" ");
        if (spaceIndex != -1) {
            substring = substring.substring(spaceIndex + 1);
        }
        spaceIndex = substring.indexOf(" ");
        if (spaceIndex != -1) {
            substring = substring.substring(0, spaceIndex);
        }
        return substring;
    }

}
