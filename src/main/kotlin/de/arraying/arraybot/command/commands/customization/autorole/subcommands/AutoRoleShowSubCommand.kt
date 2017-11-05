package de.arraying.arraybot.command.commands.customization.autorole.subcommands

import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.templates.SubCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.UDefaults

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
class AutoRoleShowSubCommand: SubCommand("show") {

    /**
     * When the sub command is executed.
     */
    override fun onSubCommand(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val guild = environment.guild
        val guildId = guild.idLong
        val guildEntry = Category.GUILD.entry as GuildEntry
        val autorole = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.AUTOROLE_ROLE), guildId, null).toLong()
        if(autorole == UDefaults.DEFAULT_SNOWFLAKE.toLong()) {
            Message.COMMANDS_AUTOROLE_SHOW_NONE.send(channel).queue()
            return
        }
        val role = guild.getRoleById(autorole)
        if(role == null) {
            Message.COMMANDS_AUTOROLE_SHOW_INVALID.send(channel).queue()
            return
        }
        Message.COMMANDS_AUTOROLE_SHOW.send(channel, role.name, role.id).queue()
    }

}