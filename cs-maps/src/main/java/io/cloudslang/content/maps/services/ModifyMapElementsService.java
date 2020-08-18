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

import io.cloudslang.content.maps.entities.ModifyMapElementsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.utils.StringMethods;
import io.cloudslang.content.maps.validators.ModifyMapElementsInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class ModifyMapElementsService {

    private final ModifyMapElementsInputValidator validator = new ModifyMapElementsInputValidator();

    public @NotNull Map<String, String> execute(@NotNull ModifyMapElementsInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(), false);

        Map<String, String> map = serializer.deserialize(input.getMap());

        Map<String, String> newMap;

        switch (input.getElements().toLowerCase()) {
            case "keys":
                newMap = map.keySet().stream()
                        .collect(Collectors.toMap(key -> StringMethods.execute(key, input.getMethod(), input.getValue()), map::get));
                break;
            case "values":
                newMap = map.keySet().stream()
                        .collect(Collectors.toMap(key -> key, key -> StringMethods.execute(map.get(key), input.getMethod(), input.getValue())));
                break;
            default:
                newMap = map.keySet().stream()
                        .collect(Collectors.toMap(key -> StringMethods.execute(key, input.getMethod(), input.getValue()),
                                key -> StringMethods.execute(map.get(key), input.getMethod(), input.getValue())));
                break;
        }

        String returnResult = serializer.serialize(newMap);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
