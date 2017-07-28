package de.arraying.arraybot.commands.commands.custom.collections

import de.arraying.arraybot.commands.commands.custom.parameters.CustomCommandParameter
import de.arraying.arraybot.commands.commands.custom.parameters.CustomCommandParameterDelete
import de.arraying.arraybot.commands.commands.custom.parameters.CustomCommandParameterSilent
import de.arraying.arraybot.commands.commands.custom.parameters.CustomCommandParameterToId

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
enum class CustomCommandParameterCollection(val parameter: CustomCommandParameter) {

    DELETE(CustomCommandParameterDelete()),
    SILENT(CustomCommandParameterSilent()),
    TO_ID(CustomCommandParameterToId())

}