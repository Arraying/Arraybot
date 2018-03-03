package de.arraying.arraybot.script.abstraction;

import de.arraying.arraybot.command.CommandEnvironment;
import de.arraying.arraybot.script.LimitError;
import de.arraying.arraybot.util.UScript;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 * Copyright 2018 Arraying
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
public abstract class AbstractMessenger {

    private final CommandEnvironment environment;
    private final boolean direct;
    private int messagesSent = 0;

    /**
     * Creates a new abstract messenger.
     * @param environment The command environment.
     */
    public AbstractMessenger(CommandEnvironment environment, boolean direct) {
        this.environment = environment;
        this.direct = direct;
    }

    /**
     * Sends a message to the target.
     * This should be done after preprocessing.
     * @param message The message.
     */
    protected abstract void abstractMessage(String message) throws Exception;

    /**
     * Sends an embed to the target.
     * This should be done after preprocessing.
     * @param embed The embed.
     */
    protected abstract void abstractEmbed(MessageEmbed embed) throws Exception;

    /**
     * Sends a message to the receiver.
     * @param message The message.
     */
    public void message(String message) {
        if(message == null
                || !preprocess()) {
            return;
        }
        try {
            abstractMessage(message);
        } catch(Exception exception) {
            UScript.error(environment, exception);
        }
    }

    /**
     * Sends an embed to the receiver.
     * @param embed The receiver.
     */
    public void message(MessageEmbed embed) {
        if(embed == null
                || !preprocess()) {
            return;
        }
        try {
            abstractEmbed(embed);
        } catch(Exception exception) {
            UScript.error(environment, exception);
        }
    }

    /**
     * Preprocesses the message.
     * @return True if the message can be sent, false otherwise.
     */
    private synchronized boolean preprocess() {
        int messagesLimit = 5;
        if(messagesSent > messagesLimit) {
            if(messagesSent == messagesLimit + 1) {
                String say = direct ? "direct" : "channel";
                UScript.error(environment, new LimitError("Can only send " + messagesLimit + " " + say + " messages per custom command."));
            }
            messagesSent++;
            return false;
        }
        return true;
    }

}
