package de.arraying.arraybot.commands

import de.arraying.arraybot.commands.entities.Command
import java.util.*

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
class CommandComparator: Comparator<Command> {

    /**
     * Compares the two commands.
     */
    override fun compare(command1: Command?, command2: Command?): Int {
        if(command1 == null
                && command2 == null) {
            return 0
        }
        if(command1 == null) {
            return -69
        }
        if(command2 == null) {
            return 69
        }
        return command1.name.compareTo(command2.name)
    }

}