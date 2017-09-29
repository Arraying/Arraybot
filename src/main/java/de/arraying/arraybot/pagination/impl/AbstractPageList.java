package de.arraying.arraybot.pagination.impl;

import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.pagination.Pages;
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
public final class AbstractPageList extends AbstractPageCore implements Pages {

    private final CustomEmbedBuilder embed;
    private final int total;
    private final String title;

    /**
     * Creates new pages as a list type.
     * @param embed The embed to use.
     * @param total The total number of pages.
     * @param entries All entries.
     * @param title The title of the page.
     */
    public AbstractPageList(CustomEmbedBuilder embed, int total, Object[] entries, String title) {
        compute(total, entries);
        this.embed = embed;
        this.total = total;
        this.title = title;
    }

    /**
     * Gets the embed builder for the corresponding page.
     * @param pageNumber The page number. 1 based indexing.
     * @param channel The text channel. Used for language manipulation.
     * @return An embed builder.
     */
    @Override
    public CustomEmbedBuilder getPage(int pageNumber, TextChannel channel) {
        if(!pages.containsKey(pageNumber)) {
            pageNumber = firstPage;
        }
        List<Object> entries = pages.get(pageNumber);
        StringBuilder builder = new StringBuilder();
        for(Object entry : entries) {
            builder.append("• ")
                    .append(UFormatting.stripFormatting(entry.toString()))
                    .append("\n");
        }
        String list = builder.toString();
        return embed.addField(title,
                    list,
                    false)
                .setFooter(Message.PAGE_FOOTER.content(channel, false)
                            .replace("{pageinfo}", pageNumber + "/" + total),
                        null);
    }

    /**
     * Gets the page total.
     * @return The page total.
     */
    @Override
    public int getTotalPages() {
        return total;
    }

}
