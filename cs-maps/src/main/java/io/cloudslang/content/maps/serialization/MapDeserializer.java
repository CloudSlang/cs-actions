/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.maps.serialization;

import io.cloudslang.content.maps.constants.Delimiters;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapDeserializer {

    public @NotNull Map<String, String> deserialize(@NotNull String mapAsString) {
        Map<String, String> map = new LinkedHashMap<>();

        mapAsString = mapAsString.trim();
        mapAsString = mapAsString.substring(1, mapAsString.length() - 1);
        if(mapAsString.trim().length() == 0) {
            return map;
        }
        String[] mapEntries = mapAsString.split(Delimiters.MAP_ENTRY_DELIM);

        for(String mapEntry : mapEntries) {
            mapEntry = mapEntry.trim();
            String[] keyValuePair = mapEntry.split(Delimiters.KEY_VALUE_PAIR_DELIM);

            String key = keyValuePair[0].trim();
            if(key.startsWith(Delimiters.QUOTE_DELIM)) {
                key = key.substring(1, key.length() - 1);
            }

            String value = keyValuePair[1].trim();
            if(value.startsWith(Delimiters.QUOTE_DELIM)) {
                value = value.substring(1, value.length() - 1);
            }

            map.put(key, value);
        }

        return map;
    }
}
