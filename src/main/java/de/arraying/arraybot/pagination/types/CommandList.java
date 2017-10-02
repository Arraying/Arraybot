package de.arraying.arraybot.pagination.types;

import de.arraying.arraybot.command.custom.syntax.CustomCommandSyntax;
import de.arraying.arraybot.command.templates.CustomCommand;
import de.arraying.arraybot.command.templates.DefaultCommand;
import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Entry;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.pagination.PageImpl;
import de.arraying.arraybot.util.CustomEmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

/**
 * Copyright 2017 Arraying
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
public class CommandList extends PageImpl {

    /**
     * Creates pages of the type command.
     * @param embed The embed.
     * @param total The total number of pages.
     * @param entries An array of entries that need to be paginated.
     * @param title The title.
     */
    public CommandList(CustomEmbedBuilder embed, int total, Object[] entries, String title) {
        super(embed, total, entries, title);
    }

    /**
     * Gets the list as a string.
     * @param input The entries.
     * @param channel The text channel.
     * @return The string.
     */
    @Override
    public String asString(List<Object> input, TextChannel channel) {
        long guild = channel.getGuild().getIdLong();
        GuildEntry entry = (GuildEntry) Entry.Category.GUILD.getEntry();
        String prefix = entry.fetch(entry.getField(GuildEntry.Fields.PREFIX), guild, null);
        StringBuilder builder = new StringBuilder();
        for(Object command : input) {
            String syntax;
            String description;
            if(command instanceof DefaultCommand) {
                syntax = prefix + ((DefaultCommand) command).getSyntax(guild);
                description = ((DefaultCommand) command).getDescription(guild);
            } else if(command instanceof CustomCommand) {
                syntax = prefix + ((CustomCommand) command).getName();
                if(((CustomCommand) command).getSyntax() == CustomCommandSyntax.STARTS_WITH) {
                    syntax += Message.CUSTOM_ARGUMENT.content(channel, false);
                }
                description = ((CustomCommand) command).getDescription();
            } else {
                continue;
            }
            builder.append("`")
                    .append(syntax)
                    .append("`")
                    .append("\n")
                    .append(description)
                    .append("\n\n");
        }
        return builder.toString();
    }

}
