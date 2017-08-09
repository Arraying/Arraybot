package de.arraying.arraybot.commands.abstraction

import de.arraying.arraybot.commands.other.CommandEnvironment
import de.arraying.arraybot.core.language.Messages
import de.arraying.arraybot.core.punishment.PunishmentType
import de.arraying.arraybot.utils.UInput
import de.arraying.arraybot.utils.ULimit
import de.arraying.arraybot.utils.UPermission
import de.arraying.arraybot.utils.UTime
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
abstract class PunishmentCommand(val commandName: String,
                                 val type: PunishmentType,
                                 val commandPermission: Permission):
        DefaultCommand(commandName,
                CommandCategory.MODERATION,
                Permission.MESSAGE_WRITE,
                customPermissionChecking = true) {

    /**
     * Declares custom permission checking.
     * Also checks if the type is valid.
     */
    init {
        checkType()
    }

    /**
     * Executes the punishment command.
     */
    override fun onDefaultCommand(environment: CommandEnvironment, args: Array<String>) {
        val channel = environment.channel
        val staff = environment.member
        if((type == PunishmentType.MUTE
                || type == PunishmentType.TEMPMUTE)
                && !UPermission.hasPermission(staff, environment.cache!!.mod!!.mutePermission.toString())) {
            Messages.PUNISH_COMMAND_PERMISSION.send(channel).queue()
            return
        } else if(!staff.hasPermission(commandPermission)) {
            Messages.PUNISH_COMMAND_PERMISSION.send(channel).queue()
            return
        }
        if(args.size < 2) {
            Messages.PUNISH_COMMAND_PROVIDE_USER.send(channel).queue()
            return
        }
        val userRaw = args[1]
        val validUser: Boolean
        if(type == PunishmentType.TEMPBAN
                || type == PunishmentType.BAN) {
            validUser = UInput.isValid(UInput.InputType.USER, userRaw, true)
        } else {
            validUser = UInput.isValid(UInput.InputType.USER, userRaw)
        }
        if(!validUser) {
            Messages.PUNISH_COMMAND_INVALID_USER.send(channel).queue()
            return
        }
        val user = UInput.retrieve(userRaw)
        val expiration: Long
        val firstReasonIndex: Int
        if(type.isTemp()) {
            if(args.size < 3) {
                Messages.PUNISH_COMMAND_PROVIDE_TIME.send(channel).queue()
                return
            }
            val timeRaw = args[2]
            expiration = UTime.parseDuration(timeRaw)
            if(expiration == -1L) {
                Messages.PUNISH_COMMAND_INVALID_TIME.send(channel).queue()
                return
            }
            firstReasonIndex = 3
        } else {
            expiration = -1
            firstReasonIndex = 2
        }
        val reason: String
        if(args.size > firstReasonIndex) {
            val stringBuilder = StringBuilder()
            for(i in firstReasonIndex..args.size-1) {
                stringBuilder
                        .append(args[i])
                        .append(" ")
            }
            reason = stringBuilder.toString().trim()
        } else {
            reason = Messages.PUNISH_REASON.content(channel)
        }
        if(reason.length > ULimit.REASON.maxLength) {
            Messages.PUNISH_REASON_LENGTH.send(channel).queue()
            return
        }
        if(arraybot.managerPunish.punish(environment.guild, user, type, staff, expiration, reason)) {
            Messages.PUNISH_COMMAND_SUCCESSFUL.send(channel).queue()
        } else {
            Messages.PUNISH_COMMAND_UNSUCCESSFUL.send(channel).queue()
        }
    }

    /**
     * Checks if the type is valid
     */
    fun checkType() {
        if(type == PunishmentType.UNMUTE
                || type == PunishmentType.UNBAN
                || type == PunishmentType.UNKNOWN) {
            throw IllegalArgumentException("The punishment type provided is invalid.")
        }
    }

}