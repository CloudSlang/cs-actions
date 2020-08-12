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

import io.cloudslang.content.maps.constants.Chars;
import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.exceptions.DeserializationException;
import org.apache.commons.lang3.StringUtils;
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
        this.pairDelimiter = pairDelimiter.replace(Chars.CRLF, Chars.LF);
        this.entryDelimiter = entryDelimiter.replace(Chars.CRLF, Chars.LF);
        this.mapStart = mapStart.replace(Chars.CRLF, Chars.LF);
        this.mapEnd = mapEnd.replace(Chars.CRLF, Chars.LF);
    }


    public @NotNull Map<String, String> deserialize(@NotNull String mapAsString) throws DeserializationException {
        Map<String, String> map = new LinkedHashMap<>();

        mapAsString = mapAsString.replace(Chars.CRLF, Chars.LF);

        int beginIndex = this.mapStart.length();
        int endIndex = mapAsString.length() - this.mapEnd.length();
        mapAsString = mapAsString.substring(beginIndex, endIndex);

        if (mapAsString.length() == 0) {
            return map;
        }

        String[] mapEntries = mapAsString.split(Pattern.quote(this.entryDelimiter));

        for (String mapEntry : mapEntries) {
            String[] keyValuePair = mapEntry.split(Pattern.quote(this.pairDelimiter));

            if (keyValuePair.length < 2) {
                if (mapEntry.endsWith(this.pairDelimiter)) {
                    String[] tmp = new String[2];
                    tmp[0] = keyValuePair[0];
                    tmp[1] = StringUtils.EMPTY;
                    keyValuePair = tmp;
                } else {
                    throw new DeserializationException(ExceptionsMsgs.MISSING_PAIR_DELIMITER);
                }
            }
            if (keyValuePair.length > 2) {
                throw new DeserializationException(ExceptionsMsgs.PAIR_DELIMITER_APPEARS_MORE_THAN_ONCE);
            }

            String key = keyValuePair[0].replace(Chars.NON_BREAKING_SPACE, " ").trim();
            String value = keyValuePair[1].replace(Chars.NON_BREAKING_SPACE, " ").trim();

            if (map.containsKey(key)) {
                throw new DeserializationException(ExceptionsMsgs.DUPLICATE_KEY);
            }
            map.put(key, value);
        }

        return map;
    }
}
