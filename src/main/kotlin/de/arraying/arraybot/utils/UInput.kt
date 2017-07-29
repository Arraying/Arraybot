package de.arraying.arraybot.utils

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
object UInput {

    private val idRegex = Pattern.compile("^(\\d{17,20})$")
    private val userMentionRegex = Pattern.compile("^(<@!?\\d{17,20}>)$")
    private val textChannelMentionRegex = Pattern.compile("^(<#?\\d{17,20}>)$")

    /**
     * Checks if the input is valid.
     */
    fun isValid(inputType: InputType, input: String, idAllowed: Boolean = false): Boolean {
        return when(inputType) {
            InputType.USER -> isUser(input, idAllowed)
            InputType.TEXT_CHANNEL -> isTextChannel(input, idAllowed)
            InputType.BOTH -> isUser(input, idAllowed) || isTextChannel(input, idAllowed)
        }
    }

    /**
     * Gets the result of the input.
     * isValid MUST be checked beforehand!!!
     */
    fun retrieve(input: String): Long {
        return if(idRegex.matcher(input).find()) {
            input.toLong()
        } else {
            input.replace("\\D".toRegex(), "").toLong()
        }
    }

    /**
     * Checks is an input contains a valid user.
     */
    private fun isUser(input: String, idAllowed: Boolean): Boolean {
        return userMentionRegex.matcher(input).find() || (idAllowed && idRegex.matcher(input).find())
    }

    /**
     * Checks if the input contains a valid text channel.
     */
    private fun isTextChannel(input: String, idAllowed: Boolean): Boolean {
        return textChannelMentionRegex.matcher(input).find() || (idAllowed && idRegex.matcher(input).find())
    }

    enum class InputType {

        USER,
        TEXT_CHANNEL,
        BOTH

    }

}