package de.arraying.arraybot.command.abstraction

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.Commands
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.language.Languages
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.misc.Limits
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.utils.PermissionUtil
import org.apache.commons.lang.WordUtils
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
abstract class DefaultCommand(val name: String,
                              val category: CommandCategory,
                              val permission: Permission,
                              open val subCommands: Array<SubCommand> = arrayOf(),
                              val aliases: Array<String> = arrayOf(),
                              private val customPermissionChecking: Boolean = false) {

    protected val arraybot = Arraybot.getInstance()!!
    private val logger = LoggerFactory.getLogger("Command-${WordUtils.capitalize(name)}")
    private val descriptionPath = "commands.$name.description"
    private val syntaxPath = "commands.$name.syntax"
    private var status: String? = null

    /**
     * When the command gets invoked an all the processing steps are done.
     */
    protected abstract fun onCommand(environment: CommandEnvironment, args: List<String>)

    /**
     * Internal command checks.
     */
    internal fun checks() {
        if ((!Languages.contains(descriptionPath)
                || !Languages.contains(syntaxPath))
                && Commands.commands.containsKey(name)) {
            Commands.unregisterCommand(name)
            logger.error("The command has been unregistered disabled as it does not have a description or syntax defined.")
        }
    }

    /**
     * Enables the command.
     */
    @Suppress("unused")
    fun enable() {
        status = null
    }

    /**
     * Disables the command.
     */
    @Suppress("unused")
    fun disable(reason: String) {
        status = reason
    }

    enum class CommandCategory {

        /**
         * Commands that change the bot's settings in the guild.
         */
        CUSTOMIZATION,

        /**
         * Developer commands that can only be executed by developer(s).
         * They're evil. Hands off.
         */
        DEVELOPER,

        /**
         * Entertainment commands.
         */
        FUN,

        /**
         * Commands that are related to guild moderation.
         */
        MODERATION,

        /**
         * Utility commands.
         */
        UTILS

    }

    /**
     * Invokes the command.
     */
    suspend fun invoke(environment: CommandEnvironment, args: List<String>) {
        println("Invoke method: " + System.currentTimeMillis())
        val channel = environment.channel
        val author = environment.author
        launch(CommonPool) {
            logger.info("${author.idLong} executed the command in the guild ${channel.guild.idLong}.")
            val resource = Redis.getInstance().resource
            resource.incr("commands")
            println("Command incrementation: " + System.currentTimeMillis())
        }
        status?.let {
            if (!it.isEmpty()
                    && it.length <= Limits.MESSAGE.limit) {
                channel.sendMessage(it).queue()
            }
        }
        println("Disabled check: " + System.currentTimeMillis())
        if (category == CommandCategory.DEVELOPER
                && !arraybot.configuration.botAuthors.any {
            it == author.idLong
        }) {
            Message.COMMAND_UNAVAILABLE_DEVELOPER.send(channel).queue()
            return
        }
        println("Developer check: " + System.currentTimeMillis())
        if (!PermissionUtil.checkPermission(channel, channel.guild.selfMember, Permission.MESSAGE_EMBED_LINKS)) {
            Message.COMMAND_UNAVAILABLE_EMBED.send(channel).queue()
            return
        }
        println("Permission check: " + System.currentTimeMillis())
        if (!PermissionUtil.checkPermission(channel, environment.member, permission)
                && !customPermissionChecking) {
            val message = Message.COMMAND_PERMISSION.content(channel)
                    .replace("{permission}", permission.getName())
            channel.sendMessage(message).queue()
            return
        }
        if (!subCommands.isEmpty()
                && args.size >= 2) {
            val subCommandName = args[1].toLowerCase()
            val subCommand = subCommands.filter {
                it.name == subCommandName
                        || it.aliases.any { alias ->
                    alias == subCommandName
                }
            }.firstOrNull()
            if (subCommand == null) {
                Message.COMMAND_SUBCOMMAND_UNKNOWN.send(channel).queue()
            } else {
                subCommand.onSubCommand(environment, args)
            }
            return
        }
        println("Subcommand check: " + System.currentTimeMillis())
        onCommand(environment, args)
    }

}