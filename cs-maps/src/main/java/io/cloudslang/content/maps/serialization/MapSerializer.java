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
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

public class MapSerializer {

    private final String pairDelimiter;
    private final String entryDelimiter;
    private final String mapStart;
    private final String mapEnd;


    public MapSerializer(@NotNull String pairDelimiter, @NotNull String entryDelimiter,
                         @NotNull String mapStart, @NotNull String mapEnd) {
        this.pairDelimiter = pairDelimiter.replace(Chars.CRLF, Chars.LF);
        this.entryDelimiter = entryDelimiter.replace(Chars.CRLF, Chars.LF);
        this.mapStart = mapStart.replace(Chars.CRLF, Chars.LF);
        this.mapEnd = mapEnd.replace(Chars.CRLF, Chars.LF);
    }


    public @NotNull String serialize(@NotNull Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        mapAsString.append(this.mapStart);

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            if((this.entryDelimiter.concat(key).lastIndexOf(this.entryDelimiter) > 0) ||
                    (StringUtils.isEmpty(key) && this.entryDelimiter.concat(this.pairDelimiter).lastIndexOf(this.entryDelimiter) > 0)) {
                mapAsString.append(Chars.NON_BREAKING_SPACE);
            }

            mapAsString.append(key).append(this.pairDelimiter).append(value);

            // avoid situation like map=<> key=whatever value=val| and entryDelim=|| => <whatever=val|||>
            // or map=<> key=whatever value=EMPTY_STRING pairDelim=| and entryDelim=|| => <whatever|||>
            // that would break the next deserialization
            if((value.concat(this.entryDelimiter).indexOf(this.entryDelimiter) < value.length()) ||
                    (StringUtils.isEmpty(value) && this.pairDelimiter.concat(this.entryDelimiter).indexOf(this.entryDelimiter)
                            < this.pairDelimiter.length())) {
                mapAsString.append(Chars.NON_BREAKING_SPACE);
            }

            if (iterator.hasNext()) {
                mapAsString.append(this.entryDelimiter);
            }
        }

        mapAsString.append(this.mapEnd);
        return mapAsString.toString();
    }
}
