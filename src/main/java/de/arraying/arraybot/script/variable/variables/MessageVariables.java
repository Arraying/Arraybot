package de.arraying.arraybot.script.variable.variables;

import de.arraying.arraybot.command.other.CommandEnvironment;
import de.arraying.arraybot.script.variable.Variables;
import de.arraying.arraybot.util.UZeus;
import de.arraying.zeus.backend.ZeusException;
import de.arraying.zeus.runtime.ZeusRuntimeBuilder;
import net.dv8tion.jda.core.entities.Message;
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
public final class MessageVariables extends Variables {

    private final String message = "message";
    private final String messageContent = "message_content";
    private final String messageContentRaw = "message_content_raw";
    private final String messagePinned = "message_pinned";

    /**
     * Creates a variable collection and registers events.
     */
    public MessageVariables() {
       registerVariableEvent(messagePinned, variable -> {
           TextChannel channel = variable.getEnvironment().getChannel();
           Object valueRaw = variable.getVariable().value();
           if(!(valueRaw instanceof Boolean)) {
               de.arraying.arraybot.language.Message.ZEUS_ERROR_MESSAGE_PIN_BOOLEAN.send(channel, false);
           } else {
               boolean value = (boolean) valueRaw;
               try {
                   Message message = variable.getEnvironment().getMessage();
                   if(value
                           && !message.isPinned()) {
                       message.pin().queue(null, error -> UZeus.error(channel, variable.getLineNumber(), error.getMessage() + "."));
                   } else if(!value
                           && message.isPinned()) {
                       message.unpin().queue(null, error -> UZeus.error(channel, variable.getLineNumber(), error.getMessage() + "."));
                   }
               } catch(PermissionException exception) {
                   UZeus.error(channel, variable.getLineNumber(), de.arraying.arraybot.language.Message.ZEUS_ERROR_MESSAGE_PIN_PERMISSION.content(channel, false));
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
        Message message = environment.getMessage();
        return builder.withVariables(createConstant(this.message, message.getIdLong()),
                createConstant(messageContent, message.getContent()),
                createConstant(messageContentRaw, message.getRawContent()),
                createMutable(messagePinned, message.isPinned()));
    }

}
