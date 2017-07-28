package de.arraying.arraybot.commands.commands.customization.announcer.subcommands

import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.entities.SubCommand
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
class SubCommandAnnouncerLeave:
        SubCommand("leave") {

    /**
     * Invokes the subcommand.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val announcer = environment.cache?.announcer?: return
        if(!announcer.leaveAnnouncer) {
            announcer.leaveAnnouncer = true
            Messages.COMMAND_ANNOUNCER_LEAVE_ENABLED.send(channel).queue()
        } else {
            announcer.leaveAnnouncer = false
            Messages.COMMAND_ANNOUNCER_LEAVE_DISABLED.send(channel).queue()
        }
    }

}