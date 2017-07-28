package de.arraying.arraybot.utils

import de.arraying.arraybot.misc.ArraybotException
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
object UtilsUser {

    private val idRegex = Pattern.compile("^(\\d{17,20})$")
    private val mentionRegex = Pattern.compile("^(<@!?\\d{17,20}>)$")

    /**
     * Checks is an input contains a valid user.
     */
    fun isUser(input: String, idAllowed: Boolean = false): Boolean {
        return mentionRegex.matcher(input).find() || (idAllowed && idRegex.matcher(input).find())
    }

    /**
     * Gets the user ID.
     */
    fun getUser(input: String): Long {
        if(!isUser(input, true)) {
            throw ArraybotException("The provided input is not a valid user.")
        }
        return if(idRegex.matcher(input).find()) {
                input.toLong()
            } else {
                input.replace("\\D".toRegex(), "").toLong()
            }
    }

}