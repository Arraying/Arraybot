package de.arraying.arraybot.commands.entities

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.commands.CommandEnvironment
import de.arraying.arraybot.commands.Commands
import de.arraying.arraybot.language.Language
import de.arraying.arraybot.language.Messages
import de.arraying.arraybot.utils.Utils
import de.arraying.arraybot.utils.UtilsLimit
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.utils.PermissionUtil

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
                              val permission: Permission,
                              open val subCommands: Array<SubCommand> = arrayOf(),
                              val aliases: Array<String> = arrayOf(),
                              protected val customPermissionChecking: Boolean = false):
        Command(name) {

    val descriptionPath = "command.$name.description"
    val syntaxPath = "command.$name.syntax"
    protected val arraybot = Arraybot.instance
    private var enabled = true
    private var message: String? = null

    /**
     * Whet happens if the default command is executed, no sub command.
     */
    abstract protected fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>)

    enum class CommandCategory {
        UTILS,
        FUN,
        MODERATION,
        CUSTOMIZATION,
        PREMIUM,
        DEVELOPER
    }

    /**
     * Executes a few checks to ensure the command will work.
     */
    internal fun checks() {
        if((!Language.contains(descriptionPath)
                || !Language.contains(syntaxPath))
                && Commands.commands.any {
            it.name == name
        }) {
            Commands.commands.filter {
                it.name == name
            } .forEach {
                Commands.commands.remove(it)
            }
            arraybot.logger.fatal("The command \"$name\" did not have a syntax or description defined, thus it has been unregistered.")
        }
    }

    /**
     * Enables the command.
     */
    fun enable() {
        enabled = true
    }

    /**
     * Disables the command.
     */
    fun disable(message: String? = null) {
        enabled = false
        this.message = message
    }

    /**
     * Preprocesses the command upon invocation to check all at once.
     */
    override suspend fun invoke(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val author = environment.author
        arraybot.logger.info("${author.name}#${author.discriminator} executed the command \"${environment.message.rawContent}\" on shard ${Utils.getShard(environment.guild.idLong)}.")
        Cache.commandsRun++
        if(!enabled) {
            Messages.COMMAND_DISABLED.send(channel).queue()
            if(message != null
                    && message!!.length < UtilsLimit.MESSAGE.maxLength) {
                channel.sendMessage(message).queue()
            }
            return
        }
        if(category == CommandCategory.DEVELOPER
                && author.idLong != arraybot.configuration.botAuthorId) {
            Messages.COMMAND_DEVELOPER.send(channel).queue()
            return
        }
        if(category == CommandCategory.PREMIUM
                && !Commands.canExecutePremiumCommands(environment.member, channel)) {
            Messages.COMMAND_PREMIUM.send(channel).queue()
            return
        }
        if(!PermissionUtil.checkPermission(channel, channel.guild.selfMember, Permission.MESSAGE_EMBED_LINKS)) {
            Messages.COMMAND_EMBED.send(channel).queue()
            return
        }

        if(!PermissionUtil.checkPermission(channel, environment.member, permission)
                && !customPermissionChecking) {
            sendPermissionMessage(channel, permission)
            return
        }
        if(subCommands.isNotEmpty()) {
            if(args.size >= 2) {
                val subCommandName = args[1].toLowerCase()
                for(subCommand in subCommands) {
                    if(subCommand.subCommandName.equals(subCommandName, true)) {
                        subCommand.onSubCommand(environment, args)
                        return
                    }
                }
                Messages.COMMAND_SUBCOMMAND.send(channel).queue()
                return
            }
        }
        onDefaultCommand(environment, args)
    }

    /**
     * Sends the permission message.
     */
    protected fun sendPermissionMessage(channel: TextChannel, perm: Permission) {
        var permission = Messages.COMMAND_PERMISSION.content(channel)
        permission = permission.replace("{permission}", perm.toString())
        channel.sendMessage(permission).queue()
    }

    /**
     * Sends the syntax message.
     */
    protected fun sendSyntax(channel: TextChannel) {
        var syntax = Messages.COMMAND_SYNTAX.content(channel)
        syntax = syntax.replace("{syntax}",
                Language.get(channel.guild, syntaxPath))
        channel.sendMessage(syntax).queue()
    }

}