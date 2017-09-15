package de.arraying.arraybot.request;

import de.arraying.arraybot.Arraybot;
import de.arraying.arraybot.data.Configuration;
import net.dv8tion.jda.core.JDA;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.IOException;

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
public abstract class AbstractBotListRequest {

    private final OkHttpClient client = new OkHttpClient();
    private final String url;
    private final JDA shard;
    protected final MediaType type = MediaType.parse("application/json; charset=utf8");
    protected final Configuration configuration = Arraybot.getInstance().getConfiguration();

    /**
     * Creates a new bot list request.
     * @param url The URL of the site.
     * @param shard The shard.
     */
    public AbstractBotListRequest(String url, JDA shard) {
        this.url = url;
        this.shard = shard;
    }

    /**
     * Gets the request body.
     * @return The request body.
     */
    public abstract RequestBody getRequestBody();

    /**
     * Gets the authorization key.
     * @return The key. Can be null if not applicable.
     */
    public abstract String getKey();

    /**
     * Whether or not the request can be made.
     * @return True if it can, false otherwise.
     */
    public abstract boolean isAvailable();

    /**
     * Sends the POST request.
     * @throws IOException If an error occurs.
     */
    public void sendRequest()
            throws IOException {
        if(isAvailable()
                || configuration.isBotBeta()) {
            return;
        }
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(getRequestBody());
        String auth = getKey();
        if(auth != null) {
            request.addHeader("Authorization", auth);
        }
        client.newCall(request.build()).execute();
    }

    /**
     * Gets the JSON object.
     * @return The JSON object.
     */
    protected JSONObject getJSONObject() {
        return configuration.getBotShards() != 1 ? getBasicShardedJSON() : getBasicJSON();
    }

    /**
     * Gets the basic JSON object.
     * @return A JSON object.
     */
    private JSONObject getBasicJSON() {
        return new JSONObject()
                .put("server_count", shard.getGuilds().size());
    }

    /**
     * Gets the basic sharded JSON object.
     * @return A JSON object.
     */
    private JSONObject getBasicShardedJSON() {
        if(shard.getShardInfo() == null) {
            throw new IllegalStateException("Attempted to get the shard info but it is null.");
        }
        return getBasicJSON()
                .put("shard_id", shard.getShardInfo().getShardId())
                .put("shard_count", shard.getShardInfo().getShardTotal());
    }

}
