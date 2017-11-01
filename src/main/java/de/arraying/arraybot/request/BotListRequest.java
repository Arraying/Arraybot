package de.arraying.arraybot.request;

import de.arraying.arraybot.util.UDatatypes;
import net.dv8tion.jda.core.JDA;
import okhttp3.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class BotListRequest {

    private final Logger logger = LoggerFactory.getLogger("Requester");
    private final MediaType type = MediaType.parse("application/json");
    private final String url;
    private final String auth;
    private final Parameter[] parameters;

    /**
     * Creates a new bot list request.
     * @param url The URL of the endpoint to POST to.
     * @param auth The value to use as the authentication header in the request.
     * @param parameters An array of request parameters to be used in the body.
     */
    public BotListRequest(String url, String auth, Parameter[] parameters) {
        this.url = url;
        this.auth = auth;
        this.parameters = parameters;
    }

    /**
     * Sends the request.
     * @param shard The JDA instance involved.
     */
    public void send(JDA shard) {
        OkHttpClient client = new OkHttpClient();
        String url = this.url.replace("{id}", shard.getSelfUser().getId());
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(getBody(shard));
        if(!auth.isEmpty()) {
            request.addHeader("Authorization", auth);
        }
        try {
            Response response = client.newCall(request.build()).execute();
            logger.info("POST request to \"{}\" returned status code {}.", url, response.code());
        } catch(IOException exception) {
            logger.error(String.format("An error occurred sending a POST request to \"%s\".", url), exception);
        }
    }

    /**
     * Gets the body for the request.
     * The "{total}" and "{shard}" placeholders can only be used when the bot is sharded.
     * @return The body.
     */
    private RequestBody getBody(JDA shard) {
        JDA.ShardInfo shardInfo = shard.getShardInfo();
        JSONObject jsonObject = new JSONObject();
        for(Parameter parameter : parameters) {
            String value = parameter.getValue()
                    .replace("{guilds}", String.valueOf(shard.getGuilds().size()));
            if(shardInfo != null) {
                value = value
                        .replace("{total}", String.valueOf(shardInfo.getShardTotal()))
                        .replace("{shard}", String.valueOf(shardInfo.getShardId()));
            }
            Object object;
            if(UDatatypes.isInt(value)) {
                object = Integer.valueOf(value);
            } else {
                object = value;
            }
            jsonObject.put(parameter.getKey(), object);
        }
        return RequestBody.create(type, jsonObject.toString());
    }

    public static class Parameter {

        private final String key;
        private final String value;

        /**
         * Creates a new POST body parameter.
         * @param key The key for the parameter.
         * @param value The value of the parameter.
         */
        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets the parameter key.
         * @return The key.
         */
        public String getKey() {
            return key;
        }

        /**
         * Gets the parameter value.
         * @return The value.
         */
        public String getValue() {
            return value;
        }

    }

}
