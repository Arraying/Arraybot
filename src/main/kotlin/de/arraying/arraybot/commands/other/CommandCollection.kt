package de.arraying.arraybot.commands.other

import de.arraying.arraybot.commands.commands.customization.announcer.CommandAnnouncer
import de.arraying.arraybot.commands.commands.customization.announcer.subcommands.SubCommandAnnouncerAnnouncements
import de.arraying.arraybot.commands.commands.customization.announcer.subcommands.SubCommandAnnouncerJoin
import de.arraying.arraybot.commands.commands.customization.announcer.subcommands.SubCommandAnnouncerLeave
import de.arraying.arraybot.commands.commands.customization.announcer.subcommands.SubCommandAnnouncerStatus
import de.arraying.arraybot.commands.commands.customization.custom.CommandCustom
import de.arraying.arraybot.commands.commands.customization.custom.subcommands.*
import de.arraying.arraybot.commands.commands.customization.default.CommandDefault
import de.arraying.arraybot.commands.commands.customization.default.subcommands.SubCommandDefaultDisable
import de.arraying.arraybot.commands.commands.customization.default.subcommands.SubCommandDefaultEnable
import de.arraying.arraybot.commands.commands.customization.default.subcommands.SubCommandDefaultList
import de.arraying.arraybot.commands.commands.moderation.filter.CommandFilter
import de.arraying.arraybot.commands.commands.moderation.filter.subcommands.*
import de.arraying.arraybot.commands.commands.customization.prefix.CommandPrefix
import de.arraying.arraybot.commands.commands.developer.eval.CommandEval
import de.arraying.arraybot.commands.commands.developer.eval.subcommands.SubCommandEvalJavascript
import de.arraying.arraybot.commands.commands.developer.eval.subcommands.SubCommandEvalKotlin
import de.arraying.arraybot.commands.commands.developer.restart.CommandRestart
import de.arraying.arraybot.commands.commands.moderation.ban.CommandBan
import de.arraying.arraybot.commands.commands.moderation.ban.CommandBanSoft
import de.arraying.arraybot.commands.commands.moderation.ban.CommandBanTemp
import de.arraying.arraybot.commands.commands.moderation.kick.CommandKick
import de.arraying.arraybot.commands.commands.moderation.mute.CommandMute
import de.arraying.arraybot.commands.commands.moderation.mute.CommandMuteTemp
import de.arraying.arraybot.commands.commands.utils.commands.CommandCommands
import de.arraying.arraybot.commands.commands.utils.commands.subcommands.SubCommandCommandsCategory
import de.arraying.arraybot.commands.commands.utils.commands.subcommands.SubCommandCommandsInfo
import de.arraying.arraybot.commands.commands.utils.commands.subcommands.SubCommandCommandsList
import de.arraying.arraybot.commands.commands.utils.commands.subcommands.SubCommandCommandsListall
import de.arraying.arraybot.commands.commands.utils.help.CommandHelp
import de.arraying.arraybot.commands.commands.utils.uptime.CommandUptime
import de.arraying.arraybot.commands.types.DefaultCommand

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

    RESTART(CommandRestart()),
    COMMANDS(CommandCommands(arrayOf(
            SubCommandCommandsCategory(),
            SubCommandCommandsInfo(),
            SubCommandCommandsList(),
            SubCommandCommandsListall()
    ))),
    CUSTOM(CommandCustom(arrayOf(
            SubCommandCustomCreate(),
            SubCommandCustomDelete(),
            SubCommandCustomExport(),
            SubCommandCustomImport(),
            SubCommandCustomList(),
            SubCommandCustomSetDescription(),
            SubCommandCustomSetPerm(),
            SubCommandCustomSetSyntax(),
            SubCommandCustomSetType(),
            SubCommandCustomSetValue()
    ))),
    DEFAULT(CommandDefault(arrayOf(
            SubCommandDefaultDisable(),
            SubCommandDefaultEnable(),
            SubCommandDefaultList()
    ))),
    HELP(CommandHelp()),
    PREFIX(CommandPrefix()),
    KICK(CommandKick()),
    MUTE(CommandMute()),
    TEMP_MUTE(CommandMuteTemp()),
    BAN(CommandBan()),
    TEMP_BAN(CommandBanTemp()),
    SOFT_BAN(CommandBanSoft()),
    FILTER(CommandFilter(arrayOf(
            SubCommandFilterAdd(),
            SubCommandFilterList(),
            SubCommandFilterMessage(),
            SubCommandFilterPrivate(),
            SubCommandFilterRegex(),
            SubCommandFilterRemove(),
            SubCommandFilterSilent(),
            SubCommandFilterToggle()
    ))),
    UPTIME(CommandUptime()),
    ANNOUNCER(CommandAnnouncer(arrayOf(
            SubCommandAnnouncerAnnouncements(),
            SubCommandAnnouncerJoin(),
            SubCommandAnnouncerLeave(),
            SubCommandAnnouncerStatus()
    ))),
    EVAL(CommandEval(arrayOf(
            SubCommandEvalKotlin(),
            SubCommandEvalJavascript()
    )))

}