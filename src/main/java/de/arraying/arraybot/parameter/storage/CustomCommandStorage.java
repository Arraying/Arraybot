package de.arraying.arraybot.parameter.storage;

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
public final class CustomCommandStorage {

    private boolean silent = false, delete = false, random = false;

    /**
     * Whether or not the custom command is silent.
     * @return True if it is, false otherwise.
     */
    public boolean isSilent() {
        return silent;
    }

    /**
     * Whether or no the custom command command should be deleted.
     * @return True if it should, false otherwise.
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * Whether or not custom command values should be random.
     * @return True if they should, false otherwise.
     */
    public boolean isRandom() {
        return random;
    }

    /**
     * Sets the silent boolean.
     * @param silent The new value.
     */
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    /**
     * Sets the delete value.
     * @param delete The boolean.
     */
    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    /**
     * Sets the random value.
     * @param random The boolean.
     */
    public void setRandom(boolean random) {
        this.random = random;
    }

}
