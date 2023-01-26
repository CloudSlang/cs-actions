/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.json.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.cloudslang.content.json.utils.Constants.InputNames.DOUBLE_QUOTES;

public class GetObjectKeysImpl {

    public static String getObjectKeys(String jsonString) {

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        List<String> keyList = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : entries) {
            keyList.add(DOUBLE_QUOTES + entry.getKey() + DOUBLE_QUOTES);
        }

        return keyList.toString();
    }
}
