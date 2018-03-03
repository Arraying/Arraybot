package de.arraying.arraybot.listener.listeners.preload;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.manager.BotManager;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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
public final class ReadyListener extends ListenerAdapter {

    private final BotManager manager = Arraybot.getInstance().getBotManager();

    /**
     * This event gets fired when JDA is ready and all
     * cached objects are usable.
     * @param event The event.
     */
    @Override
    public void onReady(ReadyEvent event) {
        manager.ready(event.getJDA());
    }

}
