package de.arraying.arraybot.command.custom.type.actions

import de.arraying.arraybot.command.custom.type.CustomCommandAction
import de.arraying.arraybot.command.other.CommandEnvironment
import de.arraying.arraybot.language.Message
import net.dv8tion.jda.core.exceptions.PermissionException

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
class AddRoleAction: CustomCommandAction, RoleAction() {

    /**
     * Adds the specified role to the specified user.
     */
    override fun onAction(environment: CommandEnvironment, value: String) {
        val guild = environment.guild
        val channel = environment.channel
        val action = preprocess(environment, value) ?: return
        val user = if(action.b == null) environment.member.user.idLong else action.b!!
        try {
            guild.controller.addSingleRoleToMember(guild.getMemberById(user), guild.getRoleById(action.a)).queue()
            Message.CUSTOM_TYPE_ROLE_ADDED.send(channel).queue()
        } catch(exception: PermissionException) {
            Message.CUSTOM_TYPE_ROLE_PERMISSION.send(channel).queue()
        }
    }

}