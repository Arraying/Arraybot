package de.arraying.arraybot.utils

import net.dv8tion.jda.core.entities.Member

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
object UtilsPlaceholder {

    /**
     * Replaces all placeholders.
     */
    fun process(member: Member, input: String): String {
        return input.replace("{user}", member.asMention)
                .replace("{username}", member.user.name)
                .replace("{userdiscriminator}", member.user.discriminator)
                .replace("{usercombo}", "${member.user.name}#${member.user.discriminator}")
                .replace("{guildname}", member.guild.name)
                .replace("{guildid}", member.guild.id)
                .replace("{guildcount}", member.guild.members.size.toString())
                .replace("{time}", UtilsTime.getDisplayableTime(member.guild))
    }

}