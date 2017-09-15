package de.arraying.arraybot.request.requests;

import de.arraying.arraybot.request.AbstractBotListRequest;
import net.dv8tion.jda.core.JDA;
import okhttp3.RequestBody;

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
public final class DiscordPwRequest extends AbstractBotListRequest {

    /**
     * Creates a new bots.discord.pw request.
     * @param shard The shard.
     */
    public DiscordPwRequest(JDA shard) {
        super(String.format("https://bots.discord.pw/api/bots/%s/stats", shard.getSelfUser().getId()), shard);
    }

    /**
     * Gets the request body.
     * @return The body.
     */
    @Override
    public RequestBody getRequestBody() {
        return RequestBody.create(type, getJSONObject().toString());
    }

    /**
     * Gets the authorization key.
     * @return The token.
     */
    @Override
    public String getKey() {
        return configuration.getKeyDiscordPw();
    }

    /**
     * Whether or not the request can be made.
     * @return True if it can, false otherwise.
     */
    @Override
    public boolean isAvailable() {
        return !configuration.getKeyDiscordPw().isEmpty();
    }

}
