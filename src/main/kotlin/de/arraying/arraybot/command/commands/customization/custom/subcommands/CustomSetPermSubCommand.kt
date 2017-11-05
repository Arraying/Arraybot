package de.arraying.arraybot.command.commands.customization.custom.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.CustomCommandEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.URole
import net.dv8tion.jda.core.Permission

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
class CustomSetPermSubCommand: SubCommand("setperm",
        aliases = arrayOf("setpermission", "sp")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        if(args.size < 3) {
            Message.COMMANDS_CUSTOM_PROVIDE_NAME.send(channel).queue()
            return
        }
        if(args.size < 4) {
            Message.COMMANDS_CUSTOM_PROVIDE_PERMISSION.send(channel).queue()
            return
        }
        val name = args[2].toLowerCase()
        val customCommands = Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
        val guildId = environment.guild.idLong
        if(!customCommands.contains(guildId, name)) {
            Message.COMMANDS_CUSTOM_NOT_EXISTS.send(channel).queue()
            return
        }
        val permissionRaw = args[3]
        val guild = environment.guild
        val permission: String
        val role = URole.getRole(guild, permissionRaw)
        permission = if(role != null) {
            role.id
        } else {
            try {
                Permission.valueOf(permissionRaw.toUpperCase()).toString()
            } catch(exception: Exception) {
                ""
            }
        }
        if(permission.isEmpty()) {
            Message.COMMANDS_CUSTOM_PERMISSION_INVALID.send(channel).queue()
            return
        }
        val customCommand = Category.CUSTOM_COMMAND.entry as CustomCommandEntry
        customCommand.push(customCommand.getField(CustomCommandEntry.Fields.PERMISSION), guildId, name, permission)
        Message.COMMANDS_CUSTOM_UPDATED.send(channel).queue()
    }

}