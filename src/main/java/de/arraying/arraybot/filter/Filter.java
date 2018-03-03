package de.arraying.arraybot.filter;

import de.arraying.arraybot.data.database.categories.GuildEntry;
import de.arraying.arraybot.data.database.core.Category;
import de.arraying.arraybot.data.database.templates.SetEntry;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.threadding.AbstractTask;
import de.arraying.arraybot.util.UDefaults;
import de.arraying.arraybot.util.UFormatting;
import de.arraying.arraybot.util.ULambda;
import de.arraying.arraybot.util.UPlaceholder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;

import java.util.regex.PatternSyntaxException;

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
public final class Filter {

    private static Filter instance;
    private final static Object mutex = new Object();

    /**
     * Private constructor to prevent initialization.
     */
    private Filter(){}

    /**
     * The static instance getter. Thread safe.
     * @return The instance.
     */
    public static Filter getInstance() {
        if(instance == null) {
            synchronized(mutex) {
                if(instance == null) {
                    instance = new Filter();
                }
            }
        }
        return instance;
    }

    /**
     * Handles message filtering.
     * @param event The event.
     */
    public void handle(GuildMessageReceivedEvent event) {
        new AbstractTask("Filter-Handler-" + event.getMessageId()) {

            /**
             * When the task is executed.
             */
            @Override
            public void onExecution() {
                Guild guild = event.getGuild();
                long guildId = guild.getIdLong();
                if(event.getAuthor().getIdLong() == guild.getSelfMember().getUser().getIdLong()) {
                    return;
                }
                GuildEntry guildEntry = (GuildEntry) Category.GUILD.getEntry();
                boolean filter = Boolean.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_ENABLED), guildId, null));
                if(!filter) {
                    return;
                }
                SetEntry filterEntries = (SetEntry) Category.FILTER.getEntry();
                boolean regex = Boolean.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_REGEX), guildId, null));
                String filteredMessage = null;
                phraseLoop:
                for(String filtered : filterEntries.values(guildId)) {
                    if(needsFiltering(filtered, event.getMessage().getContentRaw(), regex)) {
                        filteredMessage = filtered;
                        break;
                    }
                    for(MessageEmbed embed : event.getMessage().getEmbeds()) {
                        if(needsFilteringEmbed(filtered, embed, regex)) {
                            filteredMessage = filtered;
                            break phraseLoop;
                        }
                    }
                }
                if(filteredMessage == null) {
                    return;
                }
                if(FilterBypass.getAll(guild).stream().anyMatch(bypass -> isBypass(bypass, event))) {
                    return;
                }
                if(ULambda.INSTANCE.anyBypass(FilterBypass.getAll(guild), event.getMessage())) {
                    return;
                }
                try {
                    event.getMessage().delete().queue();
                } catch(PermissionException exception) {
                    return;
                }
                boolean silent = Boolean.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_SILENT), guildId, null));
                if(silent) {
                    return;
                }
                boolean dm = Boolean.valueOf(guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_PRIVATE), guildId, null));
                String response = guildEntry.fetch(guildEntry.getField(GuildEntry.Fields.FILTER_MESSAGE), guildId, null);
                TextChannel channel = event.getChannel();
                if(response.equals(UDefaults.DEFAULT_NULL)) {
                    response = Message.FILTER_MESSAGE.getContent(channel);
                }
                response = UPlaceholder.replaceCore(event.getMember(), response);
                response = UPlaceholder.replaceChannel(channel, response);
                response = UPlaceholder.replaceMessage(event.getMessage(), response);
                if(dm) {
                    if(!regex) {
                        response = response.replace("{message}", "`" + UFormatting.removeMentions(filteredMessage) + "`");
                    } else {
                        response = response.replace("{message}", Message.FILTER_REGEX.getContent(channel));
                    }
                    final String finalResponse = response;
                    event.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(finalResponse).queue());
                } else {
                    event.getChannel().sendMessage(response).queue();
                }
            }

        }.create();
    }

    /**
     * Whether or not a phrase needs to be filtered.
     * @param filteredPhrase The phrase that is filtered.
     * @param message The message that a user sent.
     * @param regex True = the phrase is a regular expression, false = it is not.
     * @return True if it needs filtering, false otherwise.
     */
    private boolean needsFiltering(String filteredPhrase, String message, boolean regex) {
        if(!regex) {
            String phraseTrimmed = filteredPhrase.trim().toLowerCase();
            if(message.contains(filteredPhrase.toLowerCase())
                    || (message.startsWith(phraseTrimmed) && message.contains(" "))
                    || (message.endsWith(phraseTrimmed) && message.contains(" "))
                    || message.equalsIgnoreCase(phraseTrimmed)) {
                return true;
            }
        } else {
            try {
                return message.matches(filteredPhrase);
            } catch(PatternSyntaxException exception) {
                return false;
            }
        }
        return false;
    }

    /**
     * Whether or not an embed needs to be filtered.
     * @param filteredPhrase The phrase that is filtered.
     * @param embed The embed the user provided.
     * @param regex True = the phrase is a regular expression, false = it is not.
     * @return True if it needs filtering, false otherwise.
     */
    private boolean needsFilteringEmbed(String filteredPhrase, MessageEmbed embed, boolean regex) {
        return embed.getDescription() != null
                && needsFiltering(filteredPhrase, embed.getDescription(), regex)
                || embed.getTitle() != null
                && needsFiltering(filteredPhrase, embed.getTitle(), regex)
                || embed.getAuthor() != null
                && embed.getAuthor().getName() != null
                && needsFiltering(filteredPhrase, embed.getAuthor().getName(), regex)
                || embed.getFooter() != null
                && embed.getFooter().getText() != null
                && needsFiltering(filteredPhrase, embed.getFooter().getText(), regex);
    }

    private boolean isBypass(FilterBypass bypass, GuildMessageReceivedEvent event) {
        return (bypass.getType() == FilterBypassType.USER
                && event.getAuthor().getIdLong() == bypass.getValue())
                || (bypass.getType() == FilterBypassType.CHANNEL
                && event.getChannel().getIdLong() == bypass.getValue())
                || (bypass.getType() == FilterBypassType.ROLE
                && event.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == bypass.getValue()));
    }

}
