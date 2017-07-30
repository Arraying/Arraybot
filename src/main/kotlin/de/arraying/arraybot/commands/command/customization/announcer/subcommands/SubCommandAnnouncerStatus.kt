package de.arraying.arraybot.commands.command.customization.announcer.subcommands

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ISubCommand
import de.arraying.arraybot.language.Messages

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
class SubCommandAnnouncerStatus:
        ISubCommand {

    override val subCommandName = "status"

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val announcer = environment.cache?.announcer?: return
        if(!announcer.statusAnnouncer) {
            announcer.statusAnnouncer = true
            Messages.COMMAND_ANNOUNCER_STATUS_ENABLED.send(channel).queue()
        } else {
            announcer.statusAnnouncer = false
            Messages.COMMAND_ANNOUNCER_STATUS_DISABLED.send(channel).queue()
        }
    }

}