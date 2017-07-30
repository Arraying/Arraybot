package de.arraying.arraybot.commands

import de.arraying.arraybot.Arraybot
import de.arraying.arraybot.cache.Cache
import de.arraying.arraybot.cache.entities.CGuild
import de.arraying.arraybot.commands.abstraction.DefaultCommand
import de.arraying.arraybot.commands.other.CommandComparator
import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.iface.ICommand
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.utils.PermissionUtil
import java.util.*

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

    internal val commands = TreeSet<DefaultCommand>(CommandComparator())
    private val arraybot = Arraybot.instance

    /**
     * Gets commands by name.
     */
    fun getCommands(commandName: String, cache: CGuild): Array<ICommand> {
        val name = commandName.toLowerCase()
        val commandList = ArrayList<ICommand>()
        this.commands.stream()
                .filter {
                    it.name == name
                    && !cache.disabled.contains(name)
                } .forEach {
                    commandList.add(it)
                }
        if(cache.customCommands.containsKey(name)) {
            commandList.add(cache.customCommands[name]!!)
        }
        return commandList.toTypedArray()
    }

    /**
     * Fun gets all commands.
     */
    fun getCommandList(member: Member, channel: TextChannel, all: Boolean = true): Array<ICommand> {
        val cache = Cache.guilds[channel.guild.idLong]!!
        val commandList = TreeSet<ICommand>(CommandComparator())
        Commands.commands
                .filter {
                    !(!all
                            && (
                            !PermissionUtil.checkPermission(member, it.permission)
                                    || !canExecutePremiumCommands(member, channel))
                            )
                    && !cache.disabled.contains(it.name.toLowerCase())
                } .mapTo(commandList) {
            it
        }
        cache.customCommands.values
                .filter {
                    !(!all
                            && !it.permission.hasPermission(member))
                } .mapTo(commandList) {
            it
        }
        return commandList.toTypedArray()
    }

    /**
     * Checks if the member can execute Premium commands.
     */
    fun canExecutePremiumCommands(member: Member, channel: TextChannel): Boolean = (isPremiumGuild(channel) || member.user.idLong == Cache.author!!.idLong)


    /**
     * Checks if an event is a command, if it is, executes it.
     */
    suspend fun executeCommand(environment: CommandEnvironment) {
        val guild = environment.guild
        val channel = environment.channel
        val author = environment.author
        if(!PermissionUtil.checkPermission(channel, guild.selfMember, Permission.MESSAGE_WRITE)
                || author.isBot
                || Cache.blacklist.containsKey(author.idLong)) {
            return
        }
        arraybot.managerSql.checkSync(guild.idLong)
        val cache = Cache.guilds[guild.idLong]
        val prefix = cache!!.prefix
        var message = environment.message.rawContent.replace(" +".toRegex(), " ").trim()
        if(message.startsWith(arraybot.configuration.botPrefix, true)) {
            message = message.substring(arraybot.configuration.botPrefix.length)
        } else if(message.startsWith(prefix, true)) {
            message = message.substring(prefix.length)
        } else {
            return
        }
        val parts: Array<String> = message.split(" ").toTypedArray()
        val commandName = parts[0].toLowerCase()
        val commandsToInvoke = ArrayList<ICommand>()
        Commands.commands
                .stream()
                .filter {
                    (it.name == commandName
                            || it.aliases.contains(commandName))
                    && !cache.disabled.contains(commandName)
                } .forEach {
                    commandsToInvoke.add(it)
                }
        cache.customCommands.values
                .stream()
                .filter {
                    it.name == commandName
                } .forEach {
                    commandsToInvoke.add(it)
                }
        for(commandToInvoke in commandsToInvoke) {
            commandToInvoke.invoke(environment, parts)
        }
    }

    /**
     * Checks if the guild is a premium compatible guild.
     */
    private fun isPremiumGuild(channel: TextChannel): Boolean {
        if(channel.guild.idLong == Cache.guild!!.idLong) {
            return true
        }
        return Cache.guild!!.getMembersWithRoles(Cache.premiumRole).any {
            PermissionUtil.checkPermission(channel, it, Permission.MANAGE_CHANNEL)
        }
    }

}