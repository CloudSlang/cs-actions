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
package io.cloudslang.content.maps.services;

import io.cloudslang.content.maps.entities.SortMapsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.utils.SortMaps;
import io.cloudslang.content.maps.validators.SortMapsInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class SortMapsService {

    private final SortMapsInputValidator validator = new SortMapsInputValidator();

    public @NotNull Map<String, String> execute(@NotNull SortMapsInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(), false);

        Map<String, String> map = serializer.deserialize(input.getMap());
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        String k = null;

        switch (input.getSortBy().toLowerCase()) {
            case "key":
                List ks = Arrays.asList(map.keySet().toArray());
                Object keys = ks.stream().sorted(new SortMaps()).collect(Collectors.joining(","));
                ks = new ArrayList<>(Arrays.asList(((String) keys).split(",")));

                if (input.getSortOrder().toLowerCase().equals("desc")) {
                    Collections.reverse(ks);
                }

                for (int i = 0; i < ks.size(); i++) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (entry.getKey().equals(ks.get(i))) {
                            k = entry.getValue();
                            break;
                        }
                    }
                    newMap.put(String.valueOf(ks.get(i)), k);
                }
                break;

            case "value":
                List v = Arrays.asList(map.values().toArray());
                Object val = v.stream().sorted(new SortMaps()).collect(Collectors.joining(","));
                v = new ArrayList<>(Arrays.asList(((String) val).split(",")));

                if (input.getSortOrder().toLowerCase().equals("desc")) {
                    Collections.reverse(v);
                }

                for (int i = 0; i < (v).size(); i++) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (entry.getValue().equals((v).get(i))) {
                            k = entry.getKey();
                            break;
                        }
                    }
                    newMap.put(k, String.valueOf(((v).get(i))));
                    map.remove(k);
                }

                break;
        }

        String returnResult = serializer.serialize(newMap);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
