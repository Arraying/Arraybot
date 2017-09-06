package de.arraying.arraybot.shard;

import net.dv8tion.jda.core.JDA;

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
public final class ShardEntry {

    private final JDA jda;
    private final int id;
    private long lastEvent = 0;

    /**
     * Creates a shard entry.
     * @param jda The shard.
     * @param id The shard ID.
     */
    public ShardEntry(JDA jda, int id) {
        this.jda = jda;
        this.id = id;
    }

    /**
     * Getter to get the JDA.
     * @return The JDA object.
     */
    public JDA getJDA() {
        return jda;
    }

    /**
     * Gets the shard ID.
     * @return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the last event.
     * @return The time of the last event, in ms.
     */
    long getLastEvent() {
        return lastEvent;
    }

    /**
     * Sets the last event.
     * @param timeInMillis The time of the event, in ms.
     */
    public void onEvent(long timeInMillis) {
        lastEvent = timeInMillis;
    }

}
