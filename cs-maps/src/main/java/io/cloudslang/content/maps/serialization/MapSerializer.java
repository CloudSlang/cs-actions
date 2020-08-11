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
        this.pairDelimiter = pairDelimiter;
        this.entryDelimiter = entryDelimiter;
        this.mapStart = mapStart;
        this.mapEnd = mapEnd;
    }


    public @NotNull String serialize(@NotNull Map<String, String> map) {
        StringBuilder mapAsString = new StringBuilder();
        mapAsString.append(this.mapStart);

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);

            mapAsString.append(key).append(this.pairDelimiter).append(value);

            if (iterator.hasNext()) {
                mapAsString.append(this.entryDelimiter);
            }
        }

        mapAsString.append(this.mapEnd);
        return mapAsString.toString();
    }
}
