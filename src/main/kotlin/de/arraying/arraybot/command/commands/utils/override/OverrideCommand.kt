package de.arraying.arraybot.command.commands.utils.override

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.language.Message
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.internal.utils.PermissionUtil

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
class OverrideCommand : DefaultCommand("override",
        CommandCategory.UTILS,
        Permission.MESSAGE_WRITE) {

    /**
     * When the command is executed.
     */
    override fun onCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(arraybot.configuration.botAuthors.any {
            it == environment.author.idLong
        }) {
            Message.COMMANDS_OVERRIDE_DEVELOPER.send(channel).queue()
        } else {
            if(!PermissionUtil.checkPermission(channel, environment.member, Permission.MANAGE_SERVER)) {
                Message.COMMAND_PERMISSION.send(channel).queue()
                return
            }
            val id = environment.guild.idLong
            if(!arraybot.overrides.contains(id)) {
                arraybot.overrides.add(id)
                Message.COMMANDS_OVERRIDE_ENABLED.send(channel).queue()
            } else {
                arraybot.overrides.remove(id)
                Message.COMMANDS_OVERRIDE_DISABLED.send(channel).queue()
            }
        }
    }

}