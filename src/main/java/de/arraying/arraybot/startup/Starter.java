package de.arraying.arraybot.startup;

import de.arraying.arraybot.startup.startups.StartupBot;
import de.arraying.arraybot.startup.startups.StartupCommands;
import de.arraying.arraybot.startup.startups.StartupLanguages;
import de.arraying.arraybot.startup.startups.StartupRedis;

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
public final class Starter {

    private static final StartupTask[] tasks = new StartupTask[] {new StartupBot(), new StartupCommands(), new StartupLanguages(), new StartupRedis()};

    /**
     * Starts all startup tasks.
     */
    public static void start() {
        for(StartupTask task : tasks) {
            task.create();
        }
    }

}
