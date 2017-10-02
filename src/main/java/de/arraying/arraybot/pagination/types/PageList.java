package de.arraying.arraybot.pagination.types;

import de.arraying.arraybot.pagination.PageImpl;
import de.arraying.arraybot.util.CustomEmbedBuilder;
import de.arraying.arraybot.util.UFormatting;
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
public class PageList extends PageImpl {

    /**
     * Creates pages of the type list.
     * @param embed The embed.
     * @param total The total number of pages.
     * @param entries An array of entries that need to be paginated.
     * @param title The title.
     */
    public PageList(CustomEmbedBuilder embed, int total, Object[] entries, String title) {
        super(embed, total, entries, title);
    }

    /**
     * Gets the list as a string.
     * @param input The entries.
     * @param channel The channel.
     * @return The string.
     */
    @Override
    public String asString(List<Object> input, TextChannel channel) {
        StringBuilder builder = new StringBuilder();
        for(Object entry : input) {
            builder.append("**•** ")
                    .append(UFormatting.stripFormatting(entry.toString()))
                    .append("\n");
        }
        return builder.toString();
    }

}
