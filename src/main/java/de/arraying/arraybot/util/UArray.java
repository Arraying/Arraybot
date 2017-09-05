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
public class UArray {

    /**
     * Checks if there is anything in an array.
     * @param array The array.
     * @param wanted The wanted thing.
     * @param <T> The type of the wanted thing and array.
     * @return True if there is, false otherwise.
     */
    public static <T> boolean any(T[] array, T wanted) {
        for(T entry : array) {
            if(entry.equals(wanted)) {
                return true;
            }
        }
        return false;
    }

}
