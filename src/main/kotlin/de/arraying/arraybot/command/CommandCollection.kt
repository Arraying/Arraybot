package de.arraying.arraybot.command

import de.arraying.arraybot.command.commands.`fun`.cat.CatCommand
import de.arraying.arraybot.command.commands.`fun`.dog.DogCommand
import de.arraying.arraybot.command.commands.`fun`.eightball.EightballCommand
import de.arraying.arraybot.command.commands.`fun`.urban.UrbanCommand
import de.arraying.arraybot.command.commands.customization.announcements.AnnouncementsCommand
import de.arraying.arraybot.command.commands.customization.announcements.subcommands.*
import de.arraying.arraybot.command.commands.customization.announcer.AnnouncerCommand
import de.arraying.arraybot.command.commands.customization.announcer.AnnouncerGenericSubCommand
import de.arraying.arraybot.command.commands.customization.autorole.AutoRoleCommand
import de.arraying.arraybot.command.commands.customization.autorole.subcommands.AutoRoleSetCommand
import de.arraying.arraybot.command.commands.customization.autorole.subcommands.AutoRoleShowSubCommand
import de.arraying.arraybot.command.commands.customization.autorole.subcommands.AutoRoleToggleSubCommand
import de.arraying.arraybot.command.commands.customization.custom.CustomCommand
import de.arraying.arraybot.command.commands.customization.custom.subcommands.*
import de.arraying.arraybot.command.commands.customization.filter.FilterCommand
import de.arraying.arraybot.command.commands.customization.filter.subcommands.*
import de.arraying.arraybot.command.commands.customization.language.LanguageCommand
import de.arraying.arraybot.command.commands.customization.modlogs.ModLogsCommand
import de.arraying.arraybot.command.commands.customization.mutesettings.MuteSettingsCommand
import de.arraying.arraybot.command.commands.customization.mutesettings.subcommands.MuteSettingsPermissionSubCommand
import de.arraying.arraybot.command.commands.customization.mutesettings.subcommands.MuteSettingsRoleSubCommand
import de.arraying.arraybot.command.commands.customization.prefix.PrefixCommand
import de.arraying.arraybot.command.commands.developer.eval.EvalCommand
import de.arraying.arraybot.command.commands.developer.reload.ReloadCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadAppSubCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadLanguagesSubCommand
import de.arraying.arraybot.command.commands.developer.reload.subcommands.ReloadShardsSubCommand
import de.arraying.arraybot.command.commands.developer.script.ScriptCommand
import de.arraying.arraybot.command.commands.developer.shards.ShardsCommand
import de.arraying.arraybot.command.commands.moderation.clear.ClearCommand
import de.arraying.arraybot.command.commands.moderation.history.HistoryCommand
import de.arraying.arraybot.command.commands.moderation.lookup.LookupCommand
import de.arraying.arraybot.command.commands.moderation.punishment.PunishmentCommand
import de.arraying.arraybot.command.commands.moderation.punishment.PunishmentRevocationCommand
import de.arraying.arraybot.command.commands.utils.commands.CommandsCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsDisableSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsEnableSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsInfoSubCommand
import de.arraying.arraybot.command.commands.utils.commands.subcommands.CommandsListSubCommand
import de.arraying.arraybot.command.commands.utils.convert.ConvertCommand
import de.arraying.arraybot.command.commands.utils.help.HelpCommand
import de.arraying.arraybot.command.commands.utils.id.IDCommand
import de.arraying.arraybot.command.commands.utils.invite.InviteCommand
import de.arraying.arraybot.command.commands.utils.override.OverrideCommand
import de.arraying.arraybot.command.commands.utils.ping.PingCommand
import de.arraying.arraybot.command.commands.utils.premium.PremiumCommand
import de.arraying.arraybot.command.commands.utils.stats.StatsCommand
import de.arraying.arraybot.command.templates.DefaultCommand
import de.arraying.arraybot.punishment.PunishmentType
import net.dv8tion.jda.api.Permission

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
     * The command that handles announcements at regular intervals.
     */
    ANNOUNCEMENTS(AnnouncementsCommand(arrayOf(
            AnnouncementsToggleSubCommand(),
            AnnouncementsChannelSubCommand(),
            AnnouncementsIntervalSubCommand(),
            AnnouncementsAddSubCommand(),
            AnnouncementsDeleteSubCommand(),
            AnnouncementsInfoSubCommand(),
            AnnouncementsListSubCommand()
    ))),

    /**
     * The command handles join and leave messages and their corresponding settings.
     * Announcements are handled separately.
     */
    ANNOUNCER(AnnouncerCommand(arrayOf(
            AnnouncerGenericSubCommand("join",
                    aliases = arrayOf("j"),
                    join = true),
            AnnouncerGenericSubCommand("leave",
                    aliases = arrayOf("l"),
                    join = false)
    ))),

    /**
     * The command that handler the automatic role assigning when users join.
     */
    AUTO_ROLE(AutoRoleCommand(arrayOf(
            AutoRoleSetCommand(),
            AutoRoleShowSubCommand(),
            AutoRoleToggleSubCommand()
    ))),

    /**
     * The command that permanently bans users.
     */
    BAN(PunishmentCommand("ban", Permission.BAN_MEMBERS, PunishmentType.BAN)),

    /**
     * Meeeooooowwwww.
     */
    CAT(CatCommand()),

    /**
     * The command that clears messages.
     */
    CLEAR(ClearCommand()),

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
     * The command that can do conversion.
     */
    CONVERT(ConvertCommand()),

    /**
     * The custom command management command.
     */
    CUSTOM(CustomCommand(arrayOf(
            CustomCreateSubCommand(),
            CustomDeleteSubCommand(),
            CustomInfoSubCommand(),
            CustomListSubCommand(),
            CustomSetDescriptionSubCommand(),
            CustomSetPermSubCommand(),
            CustomSetSynaxSubCommand(),
            CustomSetTypeSubCommand(),
            CustomSetValueSubCommand()
    ))),

    /**
     * Woof.
     */
    DOG(DogCommand()),

    /**
     * The 8Ball command.
     */
    EIGHT_BALL(EightballCommand()),

    /**
     * The command to evaluate code.
     */
    EVAL(EvalCommand()),


    /**
     * The command that handles filtered messages.
     */
    FILTER(FilterCommand(arrayOf(
            FilterAddSubCommand(),
            FilterBypassAddSubCommand(),
            FilterBypassInfoSubCommand(),
            FilterBypassListSubCommand(),
            FilterBypassRemoveSubCommand(),
            FilterListSubCommand(),
            FilterMessageSubCommand(),
            FilterRegexSubCommand(),
            FilterRemoveSubCommand(),
            FilterSilentSubCommand(),
            FilterToggleSubCommand()
    ))),

    /**
     * The help command giving a basic overview of the bot, but not listing commands.
     */
    HELP(HelpCommand()),

    /**
     * The command that shows all punishments for a user.
     */
    HISTORY(HistoryCommand()),

    /**
     * The command that will show the IDs of different entities.
     */
    ID(IDCommand()),

    /**
     * The command that sends invite links.
     */
    INVITE(InviteCommand()),

    /**
     * The command that kicks a user from the guild.
     */
    KICK(PunishmentCommand("kick", Permission.KICK_MEMBERS, PunishmentType.KICK)),

    /**
     * Sets the bot's language for the guild.
     */
    LANGUAGE(LanguageCommand()),

    /**
     * The command that shows info on specific punishments.
     */
    LOOKUP(LookupCommand()),

    /**
     * The command that gets/sets the punishment logging channel.
     */
    MOD_LOGS(ModLogsCommand()),

    /**
     * The command that applies the muted role to a user.
     */
    MUTE(PunishmentCommand("mute", Permission.MESSAGE_WRITE, PunishmentType.MUTE)),

    /**
     * The command that managers mute settings.
     */
    MUTE_SETTINGS(MuteSettingsCommand(arrayOf(
            MuteSettingsPermissionSubCommand(),
            MuteSettingsRoleSubCommand()
    ))),

    /**
     * The override command that handles permission overrides.
     */
    OVERRIDE(OverrideCommand()),

    /**
     * The command that manages the prefix.
     */
    PREFIX(PrefixCommand()),

    /**
     * The command shows information on Arraybot Premium.
     */
    PREMIUM(PremiumCommand()),

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
     * The command that displays statistics.
     */
    STATS(StatsCommand()),

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
    UN_MUTE(PunishmentRevocationCommand("unmute", PunishmentType.MUTE)),

    /**
     * The UrbanDictionary command.
     */
    URBAN(UrbanCommand());

}