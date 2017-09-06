package de.arraying.arraybot.command

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.abstraction.DefaultCommand
import de.arraying.arraybot.command.other.CommandCollection
import de.arraying.arraybot.command.other.CommandCompatator
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Entry
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.util.UDefaults
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.utils.PermissionUtil
import org.slf4j.LoggerFactory
import java.util.TreeSet

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

    val commands = TreeSet<DefaultCommand>(CommandCompatator())
    private val logger = LoggerFactory.getLogger("Command-Manager")
    private val defaultPrefix = Arraybot.getInstance().configuration.botPrefix
    /**
     * Registers all commands.
     */
    fun registerCommands() {
        for(collectedCommand in CommandCollection.values()) {
            val command = collectedCommand.command
            commands.add(command)
            logger.info("Registered the command \"${command.name}\".")
            command.checks()
        }
    }

    /**
     * Unregisters a command.
     */
    fun unregisterCommand(name: String) {
        commands.filter {
            it.name == name
        } .forEach {
            commands.remove(it)
            logger.info("Unregistered the command \"${it.name}\".")
        }
    }

    /**
     * Executes the command in a new coroutine.
     */
    fun executeCommand(environment: CommandEnvironment) {
        launch(CommonPool) {
            execute(environment)
        }
    }

    /**
     * Starts the command executor.
     */
    suspend private fun execute(environment: CommandEnvironment) {
        val guild = environment.guild
        val channel = environment.channel
        val author = environment.author
        val blacklist = Redis.getInstance().getEntry(Entry.Category.BLACKLIST) as? SetEntry ?: return
        if(!PermissionUtil.checkPermission(channel, guild.selfMember, Permission.MESSAGE_WRITE)
                || author.isBot
                || blacklist.values(UDefaults.DEFAULT_BLACKLIST.toLong()).contains(author.id)) {
            return
        }
        val prefixEntry = Redis.getInstance().getEntry(Entry.Category.GUILD) as? GuildEntry ?: return
        val guildPrefix = prefixEntry.fetch(prefixEntry.getField(GuildEntry.Fields.PREFIX), guild.idLong, null)
        var message = environment.message.rawContent.replace(" +".toRegex(), " ").trim()
        if(message.startsWith(defaultPrefix, true)) {
            message = message.substring(defaultPrefix.length)
        } else if(message.startsWith(guildPrefix, true)) {
            message = message.substring(guildPrefix.length)
        } else {
            return
        }
        val args = message.split(" ")
        val commandName = args[0].toLowerCase()
        val command = commands.filter {
            it.name == commandName
            || it.aliases.any {
                alias -> alias == commandName
            }
        } .firstOrNull()
        command?.invoke(environment, args)
    }

}