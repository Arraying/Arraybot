package de.arraying.arraybot.script.method;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.categories.VariablesEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.util.Limits;
import de.arraying.arraybot.util.UPremium;
import net.dv8tion.jda.core.entities.Guild;


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
public final class StorageMethods {

    private final CommandEnvironment environment;

    /**
     * Creates a storage environment.
     * @param environment The command environment.
     */
    public StorageMethods(CommandEnvironment environment) {
        this.environment = environment;
    }

    /**
     * Gets a stored value via key.
     * @param key The key.
     * @return The value.
     */
    public String get(String key) {
        VariablesEntry entry = (VariablesEntry) Category.VARIABLES.getEntry();
        return entry.fetch(entry.getField(key), environment.getGuild().getIdLong(), null);
    }

    /**
     * Sets a storage value via key.
     * @param key The key.
     * @param value The new value.
     * @return The value passed in, as a string.
     * @throws ExceedLimitException If no more variables can be stored.
     */
    public String set(String key, Object value)
            throws ExceedLimitException {
        Guild guild = environment.getGuild();
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        int current = Integer.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.COUNT_VS), guild.getIdLong(), null));
        current += 1;
        if(current >= Limits.VS_CAP.getLimit()
                && !UPremium.INSTANCE.isPremium(environment)) {
            throw new ExceedLimitException(guild);
        }
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.COUNT_VS), guild.getIdLong(), null, current);
        String newValue = value.toString();
        VariablesEntry entry = (VariablesEntry) Category.VARIABLES.getEntry();
        entry.push(entry.getField(key), environment.getGuild().getIdLong(), null, newValue);
        return newValue;
    }

    /**
     * Deletes a storage value.
     * @param key The key.
     */
    public void delete(String key) {
        VariablesEntry entry = (VariablesEntry) Category.VARIABLES.getEntry();
        entry.purge(entry.getField(key), environment.getGuild().getIdLong(), null);
        GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
        int current = Integer.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.COUNT_VS), environment.getGuild().getIdLong(), null));
        guildEntry.push(guildEntry.getField(GuildEntry.Fields.COUNT_VS), environment.getGuild().getIdLong(), null, current);
    }

    public class ExceedLimitException extends Exception {

        /**
         * Creates a new limit exceeded exception.
         * @param guild The guild.
         */
        ExceedLimitException(Guild guild) {
            super(Message.SCRIPT_ERROR_PREMIUM.getContent(guild.getIdLong(), String.valueOf(Limits.VS_CAP.getLimit())));
        }

    }

}
