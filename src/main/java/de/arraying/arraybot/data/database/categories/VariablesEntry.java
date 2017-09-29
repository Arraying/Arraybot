package de.arraying.arraybot.data.database.categories;

import de.arraying.arraybot.data.database.core.Entry;
import de.arraying.arraybot.data.database.core.EntryField;
import de.arraying.arraybot.data.database.templates.HashEntry;
import de.arraying.arraybot.util.UDatabase;
import de.arraying.arraybot.util.UDefaults;

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
public class VariablesEntry extends HashEntry<String> {

    /**
     * Gets a field by key.
     * @param key The key.
     * @return A field.
     */
    @Override
    public EntryField getField(String key) {
        return new EntryField(key, UDefaults.DEFAULT_NULL);
    }

    /**
     * Gets the parent.
     * @return The parent category.
     */
    @Override
    public Category getParent() {
        return null;
    }

}
