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

import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.exceptions.DeserializationException;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MapDeserializer {

    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;


    public MapDeserializer(@NotNull String pairDelimiter, @NotNull String entryDelimiter,
                           @NotNull String mapStart, @NotNull String mapEnd) {
        this.pairDelimiter = pairDelimiter;
        this.entryDelimiter = entryDelimiter;
        this.mapStart = mapStart;
        this.mapEnd = mapEnd;
    }


    public @NotNull Map<String, String> deserialize(@NotNull String mapAsString) throws DeserializationException {
        Map<String, String> map = new LinkedHashMap<>();

        int beginIndex = this.mapStart.length();
        int endIndex = mapAsString.length() - this.mapEnd.length();
        mapAsString = mapAsString.substring(beginIndex, endIndex);

        String[] mapEntries = mapAsString.split(Pattern.quote(this.entryDelimiter));

        for (String mapEntry : mapEntries) {
            String[] keyValuePair = mapEntry.split(Pattern.quote(this.pairDelimiter));
            if (keyValuePair.length < 2) {
                throw new DeserializationException(ExceptionsMsgs.MISSING_PAIR_DELIMITER);
            }
            if(keyValuePair.length > 2) {
                throw new DeserializationException(ExceptionsMsgs.PAIR_DELIMITER_APPEARS_MORE_THAN_ONCE);
            }
            String key = keyValuePair[0].trim();
            String value = keyValuePair[1].trim();
            map.put(key, value);
        }

        return map;
    }
}
