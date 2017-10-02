package de.arraying.arraybot.util;

import de.arraying.arraybot.util.objects.ActionPair;

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
public final class UPair {

    /**
     * The format that action pairs need to come in
     */
    private static final Pattern FORMAT = Pattern.compile("^\\d{17,20}(->\\d{17,20})?$");
    private static final Pattern SPLIT = Pattern.compile("->");

    /**
     * Whether or not the given input is valid for the action.
     * @param input The input.
     * @return True if it is, false otherwise.
     */
    public static boolean isValid(String input) {
        return FORMAT.matcher(input).find();
    }

    /**
     * Gets an action pair based on the input. The input MUST be valid.
     * @param input The input.
     * @return An action pair.
     */
    public static ActionPair<Long, Long> getAction(String input) {
        String[] result = SPLIT.split(input, 2);
        long a = Long.valueOf(result[0]);
        Long b = null;
        if(result.length > 1) {
            b = Long.valueOf(result[1]);
        }
        return new ActionPair<>(a, b);
    }

}
