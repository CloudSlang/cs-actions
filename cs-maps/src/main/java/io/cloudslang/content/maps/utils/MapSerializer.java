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
package io.cloudslang.content.maps.utils;

import io.cloudslang.content.maps.constants.Chars;
import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.exceptions.DeserializationException;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.validators.FieldValidator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MapSerializer {

    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;
    private final String elementWrapper;
    private final boolean stripWhitespaces;
    private final boolean handleEmptyValue;


    public MapSerializer(@NotNull String pairDelimiter, @NotNull String entryDelimiter,
                         @NotNull String mapStart, @NotNull String mapEnd,
                         @NotNull String elementWrapper, boolean stripWhitespaces, boolean handleEmptyValue) {
        this.pairDelimiter = pairDelimiter.replace(Chars.CRLF, Chars.LF);
        this.entryDelimiter = entryDelimiter.replace(Chars.CRLF, Chars.LF);
        this.mapStart = mapStart.replace(Chars.CRLF, Chars.LF);
        this.mapEnd = mapEnd.replace(Chars.CRLF, Chars.LF);
        this.elementWrapper = elementWrapper.replace(Chars.CRLF, Chars.LF);
        this.stripWhitespaces = stripWhitespaces;
        this.handleEmptyValue = handleEmptyValue;
    }


    public @NotNull String serialize(@NotNull Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        mapAsString.append(this.mapStart);

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            if (StringUtils.isEmpty(value) && this.handleEmptyValue) {
                value = Chars.NULL;
            }

            mapAsString.append(this.elementWrapper).append(key).append(this.elementWrapper)
                    .append(this.pairDelimiter)
                    .append(this.elementWrapper).append(value).append(this.elementWrapper);

            if (iterator.hasNext()) {
                mapAsString.append(this.entryDelimiter);
            }
        }

        mapAsString.append(this.mapEnd);
        return mapAsString.toString();
    }


    public @NotNull Map<String, String> deserialize(@NotNull String mapAsString) throws DeserializationException, ValidationException {
        Map<String, String> map = new LinkedHashMap<>();

        mapAsString = mapAsString.replace(Chars.CRLF, Chars.LF);
        mapAsString = trimStartAndEnd(mapAsString, this.mapStart, this.mapEnd);

        if (mapAsString.length() == 0) {
            return map;
        }

        String[] mapEntries = mapAsString.split(Pattern.quote(this.entryDelimiter));

        for (String mapEntry : mapEntries) {
            String[] keyValuePair = splitMapEntry(mapEntry);

            String key = processKeyOrValue(keyValuePair[0]);
            String value = processKeyOrValue(keyValuePair[1]);

            FieldValidator.validateKey(key, pairDelimiter, entryDelimiter, elementWrapper, mapStart, mapEnd);
            FieldValidator.validateValue(value, pairDelimiter, entryDelimiter, elementWrapper, mapStart, mapEnd);

            if (map.containsKey(key)) {
                throw new DeserializationException(ExceptionsMsgs.DUPLICATE_KEY);
            }
            map.put(key, value);
        }

        return map;
    }


    private String trimStartAndEnd(String str, String start, String end) {
        int beginIndex = start.length();
        int endIndex = str.length() - end.length();
        return str.substring(beginIndex, endIndex);
    }


    private String processKeyOrValue(String keyOrValue) throws DeserializationException {
        if (this.stripWhitespaces) {
            keyOrValue = keyOrValue.trim();
        }

        if (StringUtils.isNotEmpty(this.elementWrapper)) {
            if (!keyOrValue.startsWith(this.elementWrapper)) {
                throw new DeserializationException(ExceptionsMsgs.KEY_OR_VALUE_WAS_EXPECTED_TO_START_WITH_WRAPPER);
            }

            if (!keyOrValue.endsWith(this.elementWrapper)) {
                throw new DeserializationException(ExceptionsMsgs.KEY_OR_VALUE_WAS_EXPECTED_TO_END_WITH_WRAPPER);
            }

            keyOrValue = trimStartAndEnd(keyOrValue, this.elementWrapper, this.elementWrapper);
        }

        if (this.stripWhitespaces) {
            keyOrValue = keyOrValue.replaceAll("\\s+","");;
        }
        return keyOrValue.replace(Chars.RECORD_SEPARATOR, StringUtils.EMPTY);
    }

    private String[] splitMapEntry(String mapEntry) throws DeserializationException {
        String[] keyValuePair = mapEntry.split(Pattern.quote(this.pairDelimiter));
        if (keyValuePair.length == 2) {
            return keyValuePair;
        } else if (keyValuePair.length < 2) {
            if (mapEntry.endsWith(this.pairDelimiter)) {
                String[] tmp = new String[2];
                tmp[0] = keyValuePair[0];
                tmp[1] = StringUtils.EMPTY;
                return tmp;
            }
            throw new DeserializationException(ExceptionsMsgs.MISSING_PAIR_DELIMITER);
        } else {
            throw new DeserializationException(ExceptionsMsgs.PAIR_DELIMITER_APPEARS_MORE_THAN_ONCE);
        }
    }
}
