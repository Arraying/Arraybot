package de.arraying.arraybot.util;

import de.arraying.arraybot.command.custom.syntax.CustomCommandSyntax;
import de.arraying.arraybot.command.custom.type.CustomCommandType;
import de.arraying.arraybot.filter.FilterBypassType;
import de.arraying.arraybot.pagination.PageBuilder;
import de.arraying.arraybot.punishment.PunishmentType;
import net.dv8tion.jda.api.Permission;

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
public final class UDefaults {

    /**
     * The default for snowflakes (such as guild IDs, channel IDs, etc.).
     */
    public static final int DEFAULT_SNOWFLAKE = 0;

    /**
     * The default for IDs that are unique per-guild.
     */
    public static final int DEFAULT_ID = -1;

    /**
     * The default number to start counting from.
     */
    public static final int DEFAULT_COUNT = 0;

    /**
     * The default blacklist set ID.
     */
    public static final int DEFAULT_BLACKLIST = 5;

    /**
     * The default announcement interval.
     */
    public static final int DEFAULT_ANNOUNCEMENT_INTERVAL = 5;

    /**
     * The default ID for unknown snowflakes.
     */
    public static final long DEFAULT_UNKNOWN_SNOWFLAKE = -1337;

    /**
     * The default string. Should be language independent.
     * Used as default non-null values for things like custom commands, announcements, etc.
     */
    public static final String DEFAULT_STRING = ":)";

    /**
     * The default null represented as a string.
     */
    public static final String DEFAULT_NULL = "null";

    /**
     * The default custom command syntax.
     */
    public static final CustomCommandSyntax DEFAULT_CUSTOM_COMMAND_SYNTAX = CustomCommandSyntax.EQUALS;

    /**
     * The default custom command permission.
     */
    public static final Permission DEFAULT_CUSTOM_COMMAND_PERMISSION = Permission.MESSAGE_WRITE;

    /**
     * The default custom command type.
     */
    public static final CustomCommandType DEFAULT_CUSTOM_COMMAND_TYPE = CustomCommandType.MESSAGE;

    /**
     * The default bypass type.
     */
    public static final FilterBypassType DEFAULT_BYPASS_TYPE = FilterBypassType.CHANNEL;

    /**
     * The default punishment type.
     */
    public static final PunishmentType DEFAULT_PUNISHMENT_TYPE = PunishmentType.UNKNOWN;

    /**
     * The default permission required to mute people.
     */
    public static final Permission DEFAULT_MUTE_PERMISSION = Permission.KICK_MEMBERS;

    /**
     * The default page type.
     */
    public static final PageBuilder.Type DEFAULT_PAGE_TYPE = PageBuilder.Type.LIST;

    /**
     * The default embed builder to use in a page.
     */
    public static final CustomEmbedBuilder DEFAULT_PAGE_EMBED = new CustomEmbedBuilder();

    /**
     * The default number of entries to use per page.
     */
    public static final int DEFAULT_PAGE_TOTAL = 1;

    /**
     * The default entries to use.
     */
    public static final Object[] DEFAULT_PAGE_ENTRIES = { DEFAULT_STRING };

    /**
     * The default page title.
     */
    public static final String DEFAULT_PAGE_TITLE = DEFAULT_STRING;

}
