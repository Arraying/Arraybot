package de.arraying.arraybot.command

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.templates.CustomCommand
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.util.UDefaults
import de.arraying.arraybot.util.objects.MultiKeyMap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.utils.PermissionUtil
import org.slf4j.LoggerFactory

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
object Commands {

    val commands = MultiKeyMap<String, DefaultCommand>()
    private val logger = LoggerFactory.getLogger("Command-Manager")
    private val defaultPrefix = Arraybot.getInstance().configuration.botPrefix
    /**
     * Registers all commands.
     */
    fun registerCommands() {
        for (collectedCommand in CommandCollection.values()) {
            val command = collectedCommand.command
            commands.add(command, command.name, command.aliases)
            logger.info("Registered the command \"${command.name}\".")
            command.checks()
        }
    }

    /**
     * Unregisters a command.
     */
    fun unregisterCommand(name: String) {
        commands.remove(name)
        logger.info("Unregistered the command \"$name\".")
    }

    /**
     * Starts the command executor.
     */
    fun executeCommand(environment: CommandEnvironment) {
        val guild = environment.guild
        val channel = environment.channel
        val author = environment.author
        if (!PermissionUtil.checkPermission(channel, guild.selfMember, Permission.MESSAGE_WRITE)) {
            return
        }
        val prefixEntry = Category.GUILD.entry as? GuildEntry ?: return
        val guildPrefix = prefixEntry.fetch(prefixEntry.getField(GuildEntry.Fields.PREFIX), guild.idLong, null)
        var message = environment.message.rawContent.replace(" +".toRegex(), " ").trim()
        message = when {
            message.startsWith(defaultPrefix, true) -> message.substring(defaultPrefix.length)
            message.startsWith(guildPrefix, true) -> message.substring(guildPrefix.length)
            else -> return
        }
        val blacklist = Category.BLACKLIST.entry as? SetEntry ?: return
        if(blacklist.values(UDefaults.DEFAULT_BLACKLIST.toLong()).contains(author.id)) {
            return
        }
        val args = message.split(" ")
        val commandName = args[0].toLowerCase()
        val command = commands.getByKeyOrAlias(commandName)
        launch(CommonPool) {
            command?.invoke(environment, args)
        }
        val entry = Category.CUSTOM_COMMAND_NAMES.entry as SetEntry
        val guildId = guild.idLong
        if(entry.contains(guildId, commandName)) {
            val customCommand = CustomCommand.fromRedis(guildId, commandName, channel)
            launch(CommonPool) {
                customCommand.invoke(environment, args)
            }
        }
    }

    /**
     * Gets all commands for the member.
     */
    fun getCommands(channel: TextChannel): Array<Command> {
        val id = channel.guild.idLong
        val commands = ArrayList<Command>()
        val entry = Category.DISABLED_COMMAND.entry as SetEntry
        val disabled = entry.values(id)
        this.commands.values.filterTo(commands) {
            !disabled.contains(it.name)
                    && it.category != DefaultCommand.CommandCategory.DEVELOPER
        }
        commands.addAll(CustomCommand.getAll(id, channel))
        return commands.toTypedArray()
    }

}