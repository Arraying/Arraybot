package de.arraying.arraybot.script.method;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * Copyright 2018 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class EmbedMethods {

    /**
     * Creates a new embed.
     * @return A fresh Embed.
     */
    public EmbedBuilder newEmbed() {
        return new EmbedBuilder();
    }

    /**
     * Checks whether or not the embed can be sent.
     * @param embedBuilder An embed builder.
     * @return True if it can, false otherwise.
     */
    public boolean canSend(EmbedBuilder embedBuilder) {
        return embedBuilder != null && canSend(embedBuilder.build());
    }

    /**
     * Checks whether or not the embed can be sent.
     * @param messageEmbed A message embed object.
     * @return True if it can, false otherwise.
     */
    public boolean canSend(MessageEmbed messageEmbed) {
        return messageEmbed != null && messageEmbed.isSendable(AccountType.BOT);
    }

}
