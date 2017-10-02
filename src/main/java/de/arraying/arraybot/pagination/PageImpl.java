package de.arraying.arraybot.pagination;

import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.util.CustomEmbedBuilder;
import de.arraying.arraybot.util.UDatatypes;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
public abstract class PageImpl implements Pages {

    /**
     * The global first page.
     */
    public static final int FIRST_PAGE = 1;

    private final TreeMap<Integer, List<Object>> pages = new TreeMap<>();
    private final CustomEmbedBuilder embed;
    private final Object[] entries;
    private final String title;

    /**
     * Creates an abstract page core.
     * @param embed The embed builder.
     * @param total The total entries per page.
     * @param entries An array of entries that need to be paginated.
     * @param title The title of the field.
     */
    public PageImpl(CustomEmbedBuilder embed, int total, Object[] entries, String title) {
        this.embed = embed;
        this.entries = entries;
        this.title = title;
        compute(total);
    }

    /**
     * Gets the specified entries as a string.
     * @param input The entries.
     * @param channel The text channel.
     * @return A string.
     */
    protected abstract String asString(List<Object> input, TextChannel channel);

    /**
     * Gets a page.
     * @param pageNumber The page number. 1 based indexing.
     * @param channel The text channel. Used for language manipulation.
     * @return The embed builder to send.
     */
    @Override
    public CustomEmbedBuilder getPage(int pageNumber, TextChannel channel) {
        List<Object> entries = pages.get(pageNumber);
        String list = asString(entries, channel);
        return embed.addField(title,
                list,
                false)
                .setFooter(Message.PAGE_FOOTER.content(channel, false)
                                .replace("{pageinfo}", pageNumber + "/" + pages.size()),
                        null);
    }

    /**
     * Gets the page number.
     * @param input The input.
     * @return The number, or the first page.
     */
    public int getPageNumber(String input) {
        if(UDatatypes.isInt(input)
                && Integer.valueOf(input) <= pages.size()) {
            return Integer.valueOf(input);
        }
        return FIRST_PAGE;
    }

    /**
     * Computes the entries and adds them to the map.
     * @param itemsPerPage The number of items per page.
     */
    private void compute(int itemsPerPage) {
        if(itemsPerPage < 1) {
            throw new IllegalArgumentException("The number of items per page must be equal to one or more.");
        }
        if(entries.length == 0) {
            throw new IllegalArgumentException("The entries must not be empty.");
        }
        int currentPage = FIRST_PAGE;
        int currentIteration = 0;
        newPage(currentPage);
        for(int i = 0; i < entries.length; i++) {
            Object entry = entries[i];
            pages.get(currentPage).add(entry);
            currentIteration++;
            if(currentIteration >= itemsPerPage
                    && i < entries.length-1) {
                currentPage++;
                currentIteration = 0;
                newPage(currentPage);
            }
        }
        System.out.println(entries.length + " || " + pages.size());
    }

    /**
     * Creates a new page.
     * @param index The index.
     */
    private void newPage(int index) {
        pages.put(index, new ArrayList<>());
    }

}
