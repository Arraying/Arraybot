package de.arraying.arraybot.commands.commands.custom.entities.action

import java.util.regex.Pattern

/**
 * Copyright 2017 Arraying
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
object CustomCommandActionPair {

    private val idBasedRegex = Pattern.compile("^(([0-9]+)->([0-9]+))$")
    private val stringBasedRegex = Pattern.compile("^((.+)->([0-9]+))$")

    /**
     * Checks whether or not it is a valid action pair.
     */
    fun hasPair(idBased: Boolean, input: String): Boolean =
            if(idBased) {
                idBasedRegex.matcher(input).find()
            } else {
                stringBasedRegex.matcher(input).find()
            }

    /**
     * Gets the pair.
     */
    fun getPair(input: String): Pair<String, Long> {
        val pair = input.split("->")
        return Pair(pair[0], pair[1].toLong())
    }

}