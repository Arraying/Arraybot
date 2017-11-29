package de.arraying.arraybot.command.templates

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.command.Command
import de.arraying.arraybot.command.CommandEnvironment
import de.arraying.arraybot.command.Commands
import de.arraying.arraybot.data.database.Redis
import de.arraying.arraybot.data.database.core.Category
import de.arraying.arraybot.data.database.templates.SetEntry
import de.arraying.arraybot.language.Languages
import de.arraying.arraybot.language.Message
import de.arraying.arraybot.util.Limits
import de.arraying.arraybot.util.UDatabase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.utils.PermissionUtil
import org.apache.commons.lang.StringUtils
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
abstract class DefaultCommand(override final val name: String,
                              val category: CommandCategory,
                              open val permission: Permission,
                              open val subCommands: Array<SubCommand> = arrayOf(),
                              val aliases: Array<String> = arrayOf(),
                              private val customPermissionChecking: Boolean = false,
                              private val beta: Boolean = false): Command {

    protected val arraybot = Arraybot.getInstance()!!
    private val logger = LoggerFactory.getLogger("Command-${WordUtils.capitalize(name)}")
    private val descriptionPath = "commands_${name}_description"
    private val syntaxPath = "commands_${name}_syntax"
    private var status: String? = null

    /**
     * When the command gets invoked an all the processing steps are done.
     */
    protected abstract fun onCommand(environment: CommandEnvironment, args: List<String>)

    /**
     * Internal command checks.
     */
    internal fun checks() {
        if((!Languages.has(descriptionPath)
                || !Languages.has(syntaxPath))
                && Commands.commands.containsKey(name)) {
            Commands.unregisterCommand(name)
            logger.error("The command has been unregistered as it does not have a description and/or syntax defined.")
        }
        if(!StringUtils.isAllLowerCase(name)) {
            Commands.unregisterCommand(name)
            logger.error("The command has been unregistered due to the name not being all lowercase. Consistency, please.")
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
    override suspend fun invoke(environment: CommandEnvironment, args: List<String>) {
        val channel = environment.channel
        val author = environment.author
        launch(context = CommonPool) {
            logger.info("${author.idLong} executed the command in the guild ${channel.guild.idLong}.")
            val resource = Redis.getInstance().resource
            resource.incr(UDatabase.COMMANDS_KEY)
        }
        status?.let {
            if (!it.isEmpty()
                    && it.length <= Limits.MESSAGE.limit) {
                channel.sendMessage(it).queue()
            }
        }
        if(category == CommandCategory.DEVELOPER
                && !isDeveloper(author.idLong)) {
            Message.COMMAND_UNAVAILABLE_DEVELOPER.send(channel).queue()
            return
        }
        if(beta
                && !isPremium(environment)) {
            Message.COMMAND_PREMIUM.send(channel).queue()
            return
        }
        if(!PermissionUtil.checkPermission(channel, channel.guild.selfMember, Permission.MESSAGE_EMBED_LINKS)) {
            Message.COMMAND_UNAVAILABLE_EMBED.send(channel).queue()
            return
        }
        if(!customPermissionChecking) {
            if(!PermissionUtil.checkPermission(channel, environment.member, permission)
                    && !(
                        arraybot.overrides.contains(environment.guild.idLong)
                        && isDeveloper(author.idLong)
                    )) {
                Message.COMMAND_PERMISSION.send(channel).queue()
                return
            }
        }
//        if(!PermissionUtil.checkPermission(channel, environment.member, permission)
//                && !customPermissionChecking) {
//            Message.COMMAND_PERMISSION.send(channel).queue()
//            return
//        }
        val entry = Category.DISABLED_COMMAND.entry as SetEntry
        if(entry.contains(environment.guild.idLong, name)) {
            logger.info("Did not execute command as it was disabled in the guild ${environment.guild.idLong}")
            return
        }
        if(!subCommands.isEmpty()
                && args.size >= 2) {
            val subCommandName = args[1].toLowerCase()
            val subCommand = subCommands.firstOrNull {
                it.name == subCommandName
                        || it.aliases.any { alias ->
                    alias == subCommandName
                }
            }
            if(subCommand == null) {
                Message.COMMAND_SUBCOMMAND_UNKNOWN.send(channel).queue()
            } else {
                subCommand.onSubCommand(environment, args)
            }
            return
        }
        onCommand(environment, args)
    }

    /**
     * Gets the description for the command.
     */
    fun getDescription(language: String): String {
        return Languages.getEntry(descriptionPath, language)
    }

    /**
     * Gets the syntax for the command. Without prefix.
     */
    fun getSyntax(language: String): String {
        return Languages.getEntry(syntaxPath, language)
    }

    /**
     * Checks if the guild is able to execute premium commands.
     */
    protected fun isPremium(environment: CommandEnvironment): Boolean {
        val hub = arraybot.botManager.hub
        if(hub == null) {
            logger.error("The hub guild returned null.")
            return false
        }
        if(environment.guild.idLong == hub.idLong) {
            return true
        }
        val roleId = arraybot.configuration.premiumId
        return environment.guild.members.any {
            it.hasPermission(Permission.MANAGE_SERVER)
            && hub.getMemberById(it.user.idLong) != null
            && hub.getMemberById(it.user.idLong).roles.any {
                role -> role.idLong == roleId
            }
        }
    }

    private fun isDeveloper(id: Long) = arraybot.configuration.botAuthors.any { it == id }

}