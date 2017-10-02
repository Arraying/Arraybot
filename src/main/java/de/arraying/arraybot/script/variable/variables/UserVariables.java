package de.arraying.arraybot.script.variable.variables;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.language.Message;
import de.arraying.arraybot.script.variable.Variables;
import de.arraying.arraybot.util.Limits;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.managers.GuildController;

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
public final class UserVariables extends Variables {

    private final String user = "user";
    private final String userAvatar = "user_avatar";
    private final String userDiscriminator = "user_discriminator";
    private final String userName = "user_name";
    private final String userBot = "user_is_bot";
    private final String userNickname = "user_nickname";

    /**
     * Creates a variable collection and registers events.
     */
    public UserVariables() {
        registerVariableEvent(userNickname, variable -> {
            TextChannel channel = variable.getEnvironment().getChannel();
            String nickname = variable.getVariable().value().toString();
            if(nickname.length() < Limits.NICKNAME_MIN.getLimit()
                    || nickname.length() > Limits.NICKNAME_MAX.getLimit()) {
                String message = Message.ZEUS_ERROR_NICKNAME_LENGTH.content(channel, false)
                        .replace("{min}", Limits.NICKNAME_MIN.asString())
                        .replace("{max}", Limits.NICKNAME_MAX.asString());
                channel.sendMessage(message).queue();
            } else {
                try {
                    GuildController controller = variable.getEnvironment().getGuild().getController();
                    controller.setNickname(variable.getEnvironment().getMember(), variable.getVariable().value().toString()).reason("Script").queue();
                } catch(PermissionException exception) {
                    UZeus.error(channel, variable.getLineNumber(), Message.ZEUS_ERROR_NICKNAME_PERMISSION.content(channel, false));
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
    public ZeusRuntimeBuilder registerVariables(ZeusRuntimeBuilder builder, CommandEnvironment environment)
            throws ZeusException {
        User user = environment.getAuthor();
        return builder.withVariables(createConstant(this.user, user.getIdLong()),
                createConstant(userAvatar, user.getAvatarUrl()),
                createConstant(userDiscriminator, user.getDiscriminator()),
                createConstant(userName, user.getName()),
                createConstant(userBot, user.isBot()),
                createMutable(userNickname, environment.getMember().getNickname() == null ? user.getName() : environment.getMember().getNickname()));
    }

}
