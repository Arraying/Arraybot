package de.arraying.arraybot.script.method.methods;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.script.method.Methods;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.annotations.ZeusMethod;
import net.dv8tion.jda.client.exceptions.VerificationLevelException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.PermissionException;


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
@SuppressWarnings("unused")
public final class MessageMethods extends Methods {

    private final EmbedMethods methods;

    /**
     * Creates a new method collection object.
     * @param environment The command environment.
     * @param methods The embed methods.
     */
    public MessageMethods(CommandEnvironment environment, EmbedMethods methods) {
        super(environment);
        this.methods = methods;
    }

    /**
     * Deletes the command message.
     */
    @ZeusMethod
    public void delete() {
        try {
            environment.getMessage().delete().queue();
        } catch(PermissionException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

    /**
     * Messages the current channel.
     * @param message The message.
     */
    @ZeusMethod
    public void message_channel(Object message) {
        try {
            environment.getChannel().sendMessage(message.toString()).queue();
        } catch(PermissionException | VerificationLevelException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

    /**
     * Messages another channel.
     * @param id The channel ID.
     * @param message The message.
     */
    @ZeusMethod
    public void message_channel_other(Long id, Object message) {
        TextChannel channel = environment.getGuild().getTextChannelById(id);
        if(channel == null) {
            return;
        }
        try {
            channel.sendMessage(message.toString()).queue();
        } catch(PermissionException | VerificationLevelException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

    /**
     * Messages a private channel.
     * @param id The user's ID.
     * @param message The message.
     */
    @ZeusMethod
    public void message_private(Long id, Object message) {
        Member member = environment.getGuild().getMemberById(id);
        if(member == null) {
            return;
        }
        try {
            member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(message.toString()).queue());
        } catch(PermissionException | VerificationLevelException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

    /**
     * Messages the current channel.
     * @param embed The embed ID.
     */
    @ZeusMethod
    public void message_channel_embed(String embed) {
        try {
            EmbedBuilder embedBuilder = methods.getEmbeds().get(embed);
            if(embedBuilder != null) {
                environment.getChannel().sendMessage(embedBuilder.build()).queue();
            }
        } catch(PermissionException | VerificationLevelException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

    /**
     * Messages a private channel.
     * @param id The user's ID.
     * @param embed The embed.
     */
    @ZeusMethod
    public void message_private_embed(Long id, String embed) {
        Member member = environment.getGuild().getMemberById(id);
        if(member == null) {
            return;
        }
        try {
            EmbedBuilder embedBuilder = methods.getEmbeds().get(embed);
            if(embed != null) {
                member.getUser().openPrivateChannel().queue(channel -> channel.sendMessage(embedBuilder.build()).queue());
            }
        } catch(PermissionException | VerificationLevelException | IllegalArgumentException exception) {
            UZeus.errorInChannel(environment.getChannel(), exception);
        }
    }

}
