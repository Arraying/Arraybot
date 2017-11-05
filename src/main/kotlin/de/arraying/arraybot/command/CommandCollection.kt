package de.arraying.arraybot.command

import de.arraying.arraybot.command.commands.customization.custom.CustomCommand
import de.arraying.arraybot.command.commands.customization.custom.subcommands.*
import de.arraying.arraybot.command.commands.developer.eval.EvalCommand
import de.arraying.arraybot.command.commands.developer.reload.ReloadCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadAppSubCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadLanguagesSubCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadShardsSubCommand
import de.arraying.arraybot.command.commands.developer.script.ScriptCommand
import de.arraying.arraybot.command.commands.developer.shards.ShardsCommand
import de.arraying.arraybot.command.commands.moderation.punishment.PunishmentCommand
import de.arraying.arraybot.command.commands.moderation.punishment.PunishmentRevocationCommand
import de.arraying.arraybot.command.commands.utils.commands.CommandsCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsDisableSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsEnableSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsInfoSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsListSubCommand
import de.arraying.arraybot.command.commands.utils.help.HelpCommand
import de.arraying.arraybot.command.commands.utils.ping.PingCommand
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.punishment.PunishmentType
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
enum class CommandCollection(val command: DefaultCommand) {

    /**
     * The command that permanently bans users.
     */
    BAN(PunishmentCommand("ban", Permission.BAN_MEMBERS, PunishmentType.BAN)),

    /**
     * The command that shows information concerning commands.
     */
    COMMANDS(CommandsCommand(arrayOf(
            CommandsDisableSubCommand(),
            CommandsEnableSubCommand(),
            CommandsInfoSubCommand(),
            CommandsListSubCommand()
    ))),

    /**
     * The custom command management command.
     */
    CUSTOM(CustomCommand(arrayOf(
            CustomCreateSubCommand(),
            CustomDeleteSubCommand(),
            CustomListSubCommand(),
            CustomSetDescriptionSubCommand(),
            CustomSetPermSubCommand(),
            CustomSetSynaxSubCommand(),
            CustomSetTypeSubCommand(),
            CustomSetValueSubCommand()
    ))),

    /**
     * The help command giving a basic overview of the bot, but not listing commands.
     */
    HELP(HelpCommand()),

    /**
     * The command that kicks a user from the guild.
     */
    KICK(PunishmentCommand("kick", Permission.KICK_MEMBERS, PunishmentType.KICK)),

    /**
     * The command that applies the muted role to a user.
     */
    MUTE(PunishmentCommand("mute", Permission.MESSAGE_WRITE, PunishmentType.MUTE)),

    /**
     * The command to evaluate code.
     */
    EVAL(EvalCommand()),

    /**
     * The command to check the WebSocket ping.
     */
    PING(PingCommand()),

    /**
     * The command that is responsible for manually reloading certain modules.
     */
    RELOAD(ReloadCommand(arrayOf(
            ReloadAppSubCommand(),
            ReloadLanguagesSubCommand(),
            ReloadShardsSubCommand()
    ))),

    /**
     * The command to use to quickly evaluate a long Zeus script.
     */
    SCRIPT(ScriptCommand()),

    /**
     * The command to display statistics about a shard.
     */
    SHARDS(ShardsCommand()),

    /**
     * The command bans a user from the guild in order to clear the messages, then unbans them again.
     * Thus, this command acts like an enhanced kick.
     */
    SOFT_BAN(PunishmentCommand("softban", Permission.BAN_MEMBERS, PunishmentType.SOFT_BAN)),

    /**
     * This command temporarily bans users.
     */
    TEMP_BAN(PunishmentCommand("tempban", Permission.BAN_MEMBERS, PunishmentType.TEMP_BAN)),

    /**
     * This command temporarily applies the muted role to a user.
     */
    TEMP_MUTE(PunishmentCommand("tempmute", Permission.MESSAGE_WRITE, PunishmentType.TEMP_MUTE)),

    /**
     * This command revokes a ban punishment on a user.
     */
    UN_BAN(PunishmentRevocationCommand("unban", PunishmentType.BAN)),

    /**
     * This command revokes a mute punishment on a user.
     */
    UN_MUTE(PunishmentRevocationCommand("unmute", PunishmentType.MUTE));

}