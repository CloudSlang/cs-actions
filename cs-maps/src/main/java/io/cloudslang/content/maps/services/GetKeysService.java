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

import io.cloudslang.content.maps.entities.GetKeysInput;
import io.cloudslang.content.maps.exceptions.ServiceException;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.validators.GetKeysInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.maps.constants.ExceptionsMsgs.NULL_MAP;

public class GetKeysService {

    private final GetKeysInputValidator validator = new GetKeysInputValidator();

    public Map<String, String> execute(@NotNull GetKeysInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(), false);

        Map<String, String> map = serializer.deserialize(input.getMap());

        if (map.size() == 0){
            throw new ServiceException(NULL_MAP);
        }

        String returnResult = getKeys(map);

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }

    private String getKeys(Map<String, String> map) {

        StringBuilder stringBuilder = new StringBuilder();
        Object[] keys = map.keySet().toArray();

        for (Object key : keys) {
            stringBuilder.append(key.toString()).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }
}