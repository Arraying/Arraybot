package de.arraying.arraybot.data;

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
public final class Cache {
    
    private static Cache instance;
    private static final Object mutex = new Object();
    private String authorIconUrl;

    /**
     * Empty constructor to prevent initialization.
     */
    private Cache() {
    }

    /**
     * The static instance getter. Thread safe.
     * @return The instance.
     */
    public static Cache getInstance() {
        if(instance == null) {
            synchronized(mutex) {
                if(instance == null) {
                    instance = new Cache();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the author's icon URL.
     * @return A url.
     */
    public String getAuthorIconUrl() {
        return authorIconUrl;
    }

    /**
     * Sets the author's icon URL.
     * @param authorIconUrl A url.
     */
    public void setAuthorIconUrl(String authorIconUrl) {
        this.authorIconUrl = authorIconUrl;
    }

}
