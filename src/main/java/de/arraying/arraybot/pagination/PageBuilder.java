package de.arraying.arraybot.pagination;

import de.arraying.arraybot.pagination.impl.AbstractPageList;
import de.arraying.arraybot.util.CustomEmbedBuilder;
import de.arraying.arraybot.util.UDefaults;

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
public final class PageBuilder {

    private Type type = UDefaults.DEFAULT_PAGE_TYPE;
    private CustomEmbedBuilder embed = UDefaults.DEFAULT_PAGE_EMBED;
    private int total = UDefaults.DEFAULT_PAGE_TOTAL;
    private Object[] entries = UDefaults.DEFAULT_PAGE_ENTRIES;
    private String title = UDefaults.DEFAULT_PAGE_TITLE;

    /**
     * Sets the type.
     * @param type The type to use.
     * @return The builder.
     */
    public PageBuilder withType(Type type) {
        if(type != null) {
            this.type = type;
        }
        return this;
    }

    /**
     * Sets the embed.
     * @param embed The embed.
     * @return The builder.
     */
    public PageBuilder withEmbed(CustomEmbedBuilder embed) {
        if(embed != null) {
            this.embed = embed;
        }
        return this;
    }

    /**
     * Sets the total entries per page.
     * @param total The total items per page.
     * @return The builder.
     */
    public PageBuilder withTotal(int total) {
        if(total > 0) {
            this.total = total;
        }
        return this;
    }

    /**
     * Sets the entries for the builder.
     * @param entries An array of entries. These can be objects like commands.
     * @return The builder.
     */
    public PageBuilder withEntries(Object[] entries) {
        if(entries != null
                && entries.length > 0) {
            this.entries = entries;
        }
        return this;
    }

    /**
     * Sets the page title.
     * @param title The title.
     * @return The builder.
     */
    public PageBuilder withTitle(String title) {
        if(title != null
                && !title.isEmpty()) {
            this.title = title;
        }
        return this;
    }

    /**
     * Builds the builder.
     * @return A valid page.
     */
    public Pages build() {
        switch(type) {
            case LIST:
                return new AbstractPageList(embed, total, entries, title);
            case COMMANDS:
                return null;
            default:
                throw new IllegalStateException("Attempted to build a nonexistent type.");
        }
    }

    public enum Type {

        /**
         * The standard page type. Will list entries in a standard way.
         */
        LIST,

        /**
         * The command page type. Will treat entries as commands.
         */
        COMMANDS

    }

}
