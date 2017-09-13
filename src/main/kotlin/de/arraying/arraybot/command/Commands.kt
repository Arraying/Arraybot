package de.arraying.arraybot.command

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.abstraction.DefaultCommand
import de.arraying.arraybot.command.other.CommandCollection
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.data.database.categories.GuildEntry
import de.arraying.arraybot.data.database.core.Entry
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.misc.MultiKeyMap
import de.arraying.arraybot.util.UDefaults
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
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
        println("Inside execution: " + System.currentTimeMillis())
        val guild = environment.guild
        val channel = environment.channel
        val author = environment.author
        if (!PermissionUtil.checkPermission(channel, guild.selfMember, Permission.MESSAGE_WRITE)) {
            return
        }
        println("Pre prefix: " + System.currentTimeMillis())
        val prefixEntry = Entry.Category.GUILD.entry as? GuildEntry ?: return
        val guildPrefix = prefixEntry.fetch(prefixEntry.getField(GuildEntry.Fields.PREFIX), guild.idLong, null)
        var message = environment.message.rawContent.replace(" +".toRegex(), " ").trim()
        println("Pre prefix matching: " + System.currentTimeMillis())
        message = when {
            message.startsWith(defaultPrefix, true) -> message.substring(defaultPrefix.length)
            message.startsWith(guildPrefix, true) -> message.substring(guildPrefix.length)
            else -> return
        }
        println("Pre blacklist: " + System.currentTimeMillis())
        val blacklist = Entry.Category.BLACKLIST.entry as? SetEntry ?: return
        if(blacklist.values(UDefaults.DEFAULT_BLACKLIST.toLong()).contains(author.id)) {
            return
        }
        println("Pre message: " + System.currentTimeMillis())
        val args = message.split(" ")
        val commandName = args[0].toLowerCase()
        val command = commands.getByKeyOrAlias(commandName)
        println("Fetched command: " + System.currentTimeMillis())
        launch(CommonPool) {
            println("Pre invocation: " + System.currentTimeMillis())
            command?.invoke(environment, args)
        }
    }

}