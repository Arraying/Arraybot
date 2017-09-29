package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.script.method.Methods;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
@SuppressWarnings({"unused", "WeakerAccess"})
public final class EmbedMethods extends Methods {

    private final Map<String, EmbedBuilder> embeds = new ConcurrentHashMap<>();
    private int embedIndex = -1;

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     */
    public EmbedMethods(CommandEnvironment environment) {
        super(environment);
    }

    /**
     * Creates a new embed.
     * @return The ID of the embed.
     */
    @ZeusMethod
    public String embed_new() {
        String id = UZeus.createId("embed", environment.getMessage().getIdLong(), newEmbedIndex());
        embeds.put(id, new EmbedBuilder());
        return id;
    }

    /**
     * Sets the embed description.
     * @param embed The embed ID.
     * @param description The description.
     */
    @ZeusMethod
    public void embed_description(String embed, String description) {
        EmbedBuilder embedBuilder = embeds.get(embed);
        if(embedBuilder != null) {
            embedBuilder.setDescription(description);
        }
    }

    /**
     * Sets the colour of the embed (RGB).
     * @param embed The embed ID.
     * @param r The red value.
     * @param g The green value.
     * @param b The blue value.
     */
    @ZeusMethod
    public void embed_color(String embed, Integer r, Integer g, Integer b) {
        try {
            EmbedBuilder embedBuilder = embeds.get(embed);
            if(embedBuilder != null) {
                embedBuilder.setColor(new Color(r, g, b));
            }
        } catch(IllegalArgumentException ignored) {}
    }

    /**
     * Sets the title of the embed.
     * @param embed The embed ID.
     * @param title The title.
     */
    @ZeusMethod
    public void embed_title(String embed, String title) {
        embed_title(embed, title, null);
    }

    /**
     * Sets the title of the embed.
     * @param embed The embed ID.
     * @param title The title.
     * @param iconUrl The icon URL.
     */
    @ZeusMethod
    public void embed_title(String embed, String title, String iconUrl) {
        try {
            EmbedBuilder embedBuilder = embeds.get(embed);
            if(embedBuilder != null) {
                embedBuilder.setTitle(title, iconUrl);
            }
        } catch(IllegalArgumentException ignored) {}
    }

    /**
     * Sets the footer.
     * @param embed The embed ID.
     * @param footer The footer.
     */
    @ZeusMethod
    public void embed_footer(String embed, String footer) {
        embed_footer(embed, footer, null);
    }

    /**
     * Sets the footer.
     * @param embed The embed ID.
     * @param footer The footer.
     * @param iconUrl The icon URL.
     */
    @ZeusMethod
    public void embed_footer(String embed, String footer, String iconUrl) {
        try {
            EmbedBuilder embedBuilder = embeds.get(embed);
            if(embedBuilder != null) {
                embedBuilder.setFooter(footer, iconUrl);
            }
        } catch(IllegalArgumentException ignored) {}
    }

    /**
     * Adds a field to the embed.
     * @param embed The embed ID.
     * @param title The title.
     * @param value The value.
     * @param inline Whether or not to display inline.
     */
    @ZeusMethod
    public void embed_field(String embed, String title, String value, Boolean inline) {
        try {
            EmbedBuilder embedBuilder = embeds.get(embed);
            if(embedBuilder != null) {
                embedBuilder.addField(title, value, inline);
            }
        } catch(IllegalArgumentException ignored) {}
    }

    /**
     * Adds a blank field to the embed.
     * @param embed The embed ID.
     * @param inline Whether or not to display inline.
     */
    @ZeusMethod
    public void embed_field_blank(String embed, Boolean inline) {
        try {
            EmbedBuilder embedBuilder = embeds.get(embed);
            if(embedBuilder != null) {
                embedBuilder.addBlankField(inline);
            }
        } catch(IllegalArgumentException ignored) {}
    }

    /**
     * Gets all embeds.
     * @return A map of embeds.
     */
    public Map<String, EmbedBuilder> getEmbeds() {
        return embeds;
    }

    /**
     * Gets a new unique embed ID.
     * @return An integer.
     */
    private int newEmbedIndex() {
        return ++embedIndex;
    }

}
