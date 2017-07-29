package de.arraying.arraybot.utils

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
enum class ULimit(val maxLength: Int) {

    MESSAGE(2000),
    PREFIX(128),
    IDENTIFIER(128),
    CUSTOM_COMMAND_VALUE(1900),
    ANNOUNCEMENT(1900),
    NICKNAME(32),
    REASON(1000),
    FILTER_PHRASE(1000),
    FILTER_MESSAGE(1900)

}