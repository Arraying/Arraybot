package de.arraying.arraybot.util;

import de.arraying.kotys.JSON;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
public final class URequest {

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Gets the JSON response of a POST request.
     * @param url The URL.
     * @return The JSON object.
     * @throws IOException If an error occurs.
     */
    @SuppressWarnings("ConstantConditions")
    public static JSON get(String url)
            throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return new JSON(response.body().string());
    }

}
