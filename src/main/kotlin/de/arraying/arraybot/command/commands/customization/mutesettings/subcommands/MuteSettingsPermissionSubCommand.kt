package de.arraying.arraybot.command.commands.customization.mutesettings.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.CustomPermission

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
class MuteSettingsPermissionSubCommand: SubCommand("permission",
        aliases = arrayOf("perm")) {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val guildEntry = Category.GUILD.entry as GuildEntry
        if(args.size < 3) {
            val permissionRaw = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.MUTE_PERMISSION), guildId, null)
            val permission = CustomPermission(permissionRaw)
            Message.COMMANDS_MUTESETTINGS_PERMISSION.send(channel, permission.toString(channel)).queue()
            return
        }
        val permission = CustomPermission.getPermissionFromString(guild, args[2])
        if(permission == null) {
            Message.ROLE_OR_PERMISSION_INVALID.send(channel).queue()
            return
        }
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.MUTE_PERMISSION), guildId, null, permission.value)
        Message.COMMANDS_MUTESETTINGS_UPDATED.send(channel).queue()
    }

}