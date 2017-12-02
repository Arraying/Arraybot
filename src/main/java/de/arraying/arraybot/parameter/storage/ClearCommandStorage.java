package de.arraying.arraybot.parameter.storage;

import java.util.HashSet;
import java.util.Set;

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
public final class ClearCommandStorage {

    private final Set<Long> users = new HashSet<>();
    private boolean bots = false;

    /**
     * Whether or not all bots should be purged.
     * @return True if they should, false otherwise.
     */
    public boolean isBots() {
        return bots;
    }

    /**
     * Gets the collection of all specific users to delete messages from.
     * If this set is empty, then it should be treated as if all users are specified.
     * @return The users.
     */
    public Set<Long> getUsers() {
        return users;
    }

    /**
     * Sets whether or not all bots should be purged.
     * @param bots True if they should, false otherwise.
     */
    public void setBots(boolean bots) {
        this.bots = bots;
    }

}
