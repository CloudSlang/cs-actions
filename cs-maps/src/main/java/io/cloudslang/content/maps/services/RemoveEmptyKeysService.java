/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.maps.entities.RemoveEmptyKeysInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.validators.RemoveEmptyKeysValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

public class RemoveEmptyKeysService {

    private final RemoveEmptyKeysValidator validator = new RemoveEmptyKeysValidator();


    public @NotNull Map<String, String> execute(@NotNull RemoveEmptyKeysInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(),false);

        Map<String, String> map = serializer.deserialize(input.getMap());

        Map<String, String> result = map.entrySet().stream()
                .filter(e -> !e.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String returnResult = serializer.serialize(result);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
