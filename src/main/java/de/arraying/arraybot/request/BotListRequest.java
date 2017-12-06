package de.arraying.arraybot.request;

import de.arraying.arraybot.util.UDatatypes;
import de.arraying.kotys.JSON;
import de.arraying.kotys.JSONField;
import net.dv8tion.jda.core.JDA;
import okhttp3.*;
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
    @JSONField(key = "url") private String url;
    @JSONField(key = "auth") private String auth;
    @JSONField(key = "parameters") private Parameter[] parameters;

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
            response.close();
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
        JSON json = new JSON();
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
            json.put(parameter.getKey(), object);
        }
        return RequestBody.create(type, json.marshal());
    }

}
