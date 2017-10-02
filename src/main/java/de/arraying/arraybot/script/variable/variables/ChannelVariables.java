package de.arraying.arraybot.script.variable.variables;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.variable.Variables;
import de.arraying.arraybot.util.Limits;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
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
@SuppressWarnings("FieldCanBeLocal")
public final class ChannelVariables extends Variables {

    private final String channel = "channel";
    private final String channelName = "channel_name";
    private final String channelPosition = "channel_position";
    private final String channelTopic = "channel_topic";
    private final String channelNSFW = "channel_nsfw";

    /**
     * Creates a new variable collection and registers events.
     */
    public ChannelVariables() {
        registerVariableEvent(channelName, variable -> {
            TextChannel channel = variable.getEnvironment().getChannel();
            String newName = variable.getVariable().value().toString()
                    .replaceAll("[^a-zA-Z0-9-_]", "-");
            if(newName.length() < Limits.CHANNEL_NAME_MIN.getLimit()
                    || newName.length() > Limits.CHANNEL_NAME_MAX.getLimit()) {
                String message = Message.ZEUS_ERROR_CHANNEL_NAME_LENGTH.content(channel, false)
                        .replace("{min}", Limits.CHANNEL_NAME_MIN.asString())
                        .replace("{max}", Limits.CHANNEL_NAME_MAX.asString());
                channel.sendMessage(message).queue();
            } else {
                try {
                    channel.getManager().setName(newName).queue();
                } catch(PermissionException | IllegalArgumentException exception) {
                    UZeus.error(channel, variable.getLineNumber(), exception.getMessage());
                }
            }
        });
        registerVariableEvent(channelPosition, variable -> {
            TextChannel channel = variable.getEnvironment().getChannel();
            Object positionRaw = variable.getVariable().value();
            if(!(positionRaw instanceof Integer)) {
                Message.ZEUS_ERROR_CHANNEL_POSITION_TYPE.send(channel,false).queue();
            } else {
                try {
                    channel.getManager().setPosition((Integer) positionRaw).queue();
                } catch(PermissionException exception) {
                    UZeus.error(channel, variable.getLineNumber(), exception.getMessage());
                }
            }
        });
        registerVariableEvent(channelTopic, variable -> {
            TextChannel channel = variable.getEnvironment().getChannel();
            String newTopic = variable.getVariable().value().toString();
            if(newTopic.length() > Limits.CHANNEL_TOPIC_MAX.getLimit()) {
                String message = Message.ZEUS_ERROR_CHANNEL_TOPIC_LENGTH.content(channel, false)
                        .replace("{max}", Limits.CHANNEL_TOPIC_MAX.asString());
                channel.sendMessage(message).queue();
            } else {
                try {
                    channel.getManager().setTopic(newTopic).queue();
                } catch(PermissionException | IllegalArgumentException exception) {
                    UZeus.error(channel, variable.getLineNumber(), exception.getMessage());
                }
            }
        });
        registerVariableEvent(channelNSFW, variable -> {
            TextChannel channel = variable.getEnvironment().getChannel();
            Object positionRaw = variable.getVariable().value();
            if(!(positionRaw instanceof Boolean)) {
                Message.ZEUS_ERROR_CHANNEL_NSFW_TYPE.send(channel,false).queue();
            } else {
                try {
                    channel.getManager().setNSFW((Boolean) positionRaw).queue();
                } catch(PermissionException exception) {
                    UZeus.error(channel, variable.getLineNumber(), exception.getMessage());
                }
            }
        });
    }

    /**
     * Registers the variables.
     * @param builder The current runtime builder.
     * @param environment The command environment to have access to objects.
     * @return The builder.
     * @throws ZeusException If an error occurs.
     */
    @Override
    public ZeusRuntimeBuilder registerVariables(ZeusRuntimeBuilder builder, CommandEnvironment environment) throws ZeusException {
        TextChannel channel = environment.getChannel();
        return builder.withVariables(createConstant(this.channel, channel.getIdLong()),
                createMutable(channelName, channel.getName()),
                createMutable(channelPosition, channel.getPosition()),
                createMutable(channelTopic, channel.getTopic()),
                createMutable(channelNSFW, channel.isNSFW()));
    }

}
