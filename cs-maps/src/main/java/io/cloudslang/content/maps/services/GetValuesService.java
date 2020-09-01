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

import io.cloudslang.content.maps.entities.GetValuesInput;
import io.cloudslang.content.maps.exceptions.ServiceException;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.validators.GetValuesInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.maps.constants.Chars.COMMA;
import static io.cloudslang.content.maps.constants.ExceptionsMsgs.MISSING_KEY;
import static io.cloudslang.content.maps.constants.ExceptionsMsgs.NULL_MAP;

public class GetValuesService {

    private final GetValuesInputValidator validator = new GetValuesInputValidator();

    public Map<String, String> execute(@NotNull GetValuesInput input) throws Exception {
        ValidationException validationEx = validator.validate(input);
        if (validationEx != null) {
            throw validationEx;
        }

        MapSerializer serializer = new MapSerializer(
                input.getPairDelimiter(), input.getEntryDelimiter(),
                input.getMapStart(), input.getMapEnd(),
                input.getElementWrapper(), input.isStripWhitespaces(), false);

        Map<String, String> map = serializer.deserialize(input.getMap());
        String returnResult = getValues(map, input);

        if (map.size() == 0){
            throw new ServiceException(NULL_MAP);
        }

        return OutputUtilities.getSuccessResultsMap(returnResult);
    }

    private String getValues(Map<String, String> map, GetValuesInput input) throws ServiceException {
        StringBuilder stringBuilder = new StringBuilder();
        String keysList = input.getKey();
        if (input.isStripWhitespaces())
            keysList = input.getKey().replaceAll("\\s+", "");

        if (StringUtils.isEmpty(keysList)) {
            for (String value : map.values()) {
                stringBuilder.append(value).append(',');
            }
            if (stringBuilder.length() > 0)
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } else {
            String[] keys;
            if (!StringUtils.isEmpty(input.getKeyDelimiter())) {
                keys = keysList.split(input.getKeyDelimiter());
            } else {
                keys = new String[]{keysList};
            }
            for (int i = 0; i < keys.length; i++)
                if (map.get(keys[i]) != null)
                    stringBuilder.append(map.get(keys[i])).append(COMMA);
            if (stringBuilder.length() > 0)
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            if (stringBuilder.toString().length() > 0)
                return stringBuilder.toString();
            else
                throw new ServiceException(MISSING_KEY);
        }
    }
}
