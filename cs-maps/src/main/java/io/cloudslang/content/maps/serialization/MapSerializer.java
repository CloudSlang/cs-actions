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

import java.util.Iterator;
import java.util.Map;

public class MapSerializer {

    public @NotNull String serialize(@NotNull Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        mapAsString.append(Delimiters.MAP_START);

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            if(key == null) {
                mapAsString.append((String) null);
            } else {
                mapAsString.append(Delimiters.QUOTE_DELIM).append(key).append(Delimiters.QUOTE_DELIM);
            }

            mapAsString.append(Delimiters.KEY_VALUE_PAIR_DELIM).append(" ");

            if(value == null) {
                mapAsString.append((String) null);
            } else {
                mapAsString.append(Delimiters.QUOTE_DELIM).append(value).append(Delimiters.QUOTE_DELIM);
            }

            if (iterator.hasNext()) {
                mapAsString.append(Delimiters.MAP_ENTRY_DELIM).append(" ");
            }
        }

        mapAsString.append(Delimiters.MAP_END);
        return mapAsString.toString();
    }
}
