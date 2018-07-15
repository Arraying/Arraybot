package de.arraying.arraybot.util;

import de.arraying.arraybot.Arraybot;

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
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class UDatatypes {

    public static boolean isInt(String input) {
        try {
            Integer.valueOf(input);
            return true;
        } catch(NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Checks whether a string is a long.
     * @param input The string.
     * @return True if it is, false otherwise.
     */
    public static boolean isLong(String input) {
        try {
            Long.valueOf(input);
            return true;
        } catch(NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Gets the string input as a double.
     * @param input The input.
     * @return The double, or null.
     */
    public static Double toDouble(String input) {
        try {
            return Double.valueOf(input);
        } catch(NumberFormatException exception) {
            return null;
        }
    }

    /**
     * Gets the shard ID corresponding to a guild ID.
     * @param guildId The guild ID.
     * @return The shard ID.
     */
    public static int getShardId(long guildId) {
        int shards = Arraybot.INSTANCE.getConfiguration().getBotShards();
        return (int) ((guildId >> 22) % shards);
    }

}
