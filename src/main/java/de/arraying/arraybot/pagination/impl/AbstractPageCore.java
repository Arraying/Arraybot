package de.arraying.arraybot.pagination.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

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
abstract class AbstractPageCore {

    final TreeMap<Integer, List<Object>> pages = new TreeMap<>();
    final int firstPage = 1;

    /**
     * Computes the entries and adds them to the map.
     * @param itemsPerPage The number of items per page.
     * @param entries An array of entries.
     */
    final void compute(int itemsPerPage, Object[] entries) {
        if(itemsPerPage < 1) {
            throw new IllegalArgumentException("The number of items per page must be equal to one or more.");
        }
        if(entries.length == 0) {
            throw new IllegalArgumentException("The entries must not be empty.");
        }
        int currentPage = firstPage;
        int currentIteration = 0;
        newPage(currentPage);
        for(int i = 0; i < entries.length; i++) {
            Object entry = entries[i];
            pages.get(currentPage).add(entry);
            currentIteration++;
            if(currentIteration >= itemsPerPage
                    && i <= entries.length -1) {
                currentPage++;
                currentIteration = 0;
            }
        }
    }

    /**
     * Creates a new page.
     * @param index The index.
     */
    private void newPage(int index) {
        pages.put(index, new ArrayList<>());
    }

}
