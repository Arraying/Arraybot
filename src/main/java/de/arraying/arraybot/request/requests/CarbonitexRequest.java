package de.arraying.arraybot.request.requests;

import de.arraying.arraybot.request.AbstractBotListRequest;
import net.dv8tion.jda.core.JDA;
import okhttp3.RequestBody;
import org.json.JSONObject;

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
public final class CarbonitexRequest extends AbstractBotListRequest {

    /**
     * Creates a new Carbonitex request.
     * @param shard The shard.
     */
    public CarbonitexRequest(JDA shard) {
        super("https://www.carbonitex.net/discord/data/botdata.php", shard);
    }

    /**
     * Gets the request body.
     * @return The body.
     */
    @Override
    public RequestBody getRequestBody() {
        JSONObject json = getJSONObject();
        json.put("key", configuration.getKeyCarbonitex());
        return RequestBody.create(type, json.toString());
    }

    /**
     * Gets the authorization key.
     * @return Null because not required.
     */
    @Override
    public String getKey() {
        return null;
    }

    /**
     * Whether or not the request can be made.
     * @return True if it can, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        return !configuration.getKeyCarbonitex().isEmpty();
    }

}
