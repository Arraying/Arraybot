package de.arraying.arraybot.manager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
public final class FileManager {

    private final Logger logger = LoggerFactory.getLogger("File-Manager");
    private final File removalFile = new File("removals.json");

    /**
     * When the file manager is created.
     */
    public FileManager() {
        try {
            if(removalFile.createNewFile()) {
                JSONObject json = new JSONObject().put("pending", new JSONArray());
                write(removalFile, json.toString());
            }
        } catch(IOException exception) {
            logger.error("Error creating new file.", exception);
        }
    }

    /**
     * Gets the content of the file.
     * @param file The file.
     * @return The content.
     * @throws IOException When an error occurs.
     */
    public static synchronized String content(File file)
            throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    /**
     * Adds a guild to the pending to be removed status.
     * @param id The guild.
     * @throws IOException When an error occurs.
     */
    public synchronized void addToRemovalQueue(long id)
            throws IOException {
        if(!removalFile.exists()) {
            throw new IllegalStateException("Removal file no longer exists.");
        }
        JSONObject json = new JSONObject(content(removalFile));
        json.getJSONArray("pending").put(id);
        write(removalFile, json.toString());
    }

    /**
     * Removes a guild from the removal queue.
     * @param id The guild.
     * @throws IOException When an error occurs.
     */
    public synchronized void removeFromRemovalQueue(long id)
            throws IOException {
        if(!removalFile.exists()) {
            throw new IllegalStateException("Removal file no longer exists.");
        }
        JSONObject json = new JSONObject(content(removalFile));
        JSONArray array = json.getJSONArray("pending");
        for(int i = 0; i < array.length(); i++) {
            if(array.getLong(i) == id) {
                array.remove(i);
            }
        }
        write(removalFile, json.toString());
    }

    /**
     * Gets all guilds in the removal queue.
     * @return A collection of guild IDs.
     * @throws IOException When an error occurs.
     */
    public synchronized List<Long> getRemovalQueue()
            throws IOException {
        if(!removalFile.exists()) {
            throw new IllegalStateException("Removal file no longer exists.");
        }
        JSONObject json = new JSONObject(content(removalFile));
        JSONArray array = json.getJSONArray("pending");
        List<Long> pending = new ArrayList<>();
        for(int i = 0; i < array.length(); i++) {
            pending.add(array.getLong(i));
        }
        return pending;
    }

    /**
     * Writes to the file.
     * @param file The file.
     * @param content The content.
     */
    private void write(File file, String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.close();
        } catch(IOException exception) {
            logger.error("An error occurred writing to file.", exception);
        }
    }

}
