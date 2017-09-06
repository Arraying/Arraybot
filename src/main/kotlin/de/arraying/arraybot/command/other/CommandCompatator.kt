package de.arraying.arraybot.command.other

import de.arraying.arraybot.command.abstraction.DefaultCommand

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
class CommandCompatator: Comparator<DefaultCommand> {

    /**
     * Compares the two commands.
     */
    override fun compare(one: DefaultCommand?, two: DefaultCommand?): Int {
        if(one == null
                && two == null) {
            return 0
        }
        if(one == null) {
            return -69
        }
        if(two == null) {
            return 69
        }
        return one.name.compareTo(two.name)
    }

}