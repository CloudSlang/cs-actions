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

import io.cloudslang.content.maps.entities.MapComparatorInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.utils.MapSerializer;
import io.cloudslang.content.maps.validators.MapComparatorInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.maps.constants.Chars.CONTAINS;
import static io.cloudslang.content.maps.constants.Chars.EQUALS;

public class MapComparatorService {

    private final MapComparatorInputValidator validate = new MapComparatorInputValidator();

    public @NotNull Map<String, String> execute(@NotNull MapComparatorInput input) throws Exception {
        ValidationException validationException = validate.validateMatchType(input);
        if (validationException != null) {
            throw validationException;
        }

        MapSerializer map1Serializer = new MapSerializer(
                input.getMap1PairDelimiter(), input.getMap1EntryDelimiter(),
                input.getMap1Start(), input.getMap1End(),
                input.getMap1ElementWrapper(), input.isStripWhitespaces(), false);

        MapSerializer map2Serializer = new MapSerializer(
                input.getMap2PairDelimiter(), input.getMap2EntryDelimiter(),
                input.getMap2Start(), input.getMap2End(),
                input.getMap2ElementWrapper(), input.isStripWhitespaces(), false);

        Map<String, String> map1 = map1Serializer.deserialize(input.getMap1());
        Map<String, String> map2 = map2Serializer.deserialize(input.getMap2());

        String returnResult = "false";
        if (input.getMatchType().equals(EQUALS)) {
            if (map1.equals(map2))
                returnResult = "true";
            else
                returnResult = "false";
        }
        else if(input.getMatchType().equals(CONTAINS))
        {
            if(map1.entrySet().containsAll(map2.entrySet()))
                returnResult = "true";
            else
                returnResult = "false";
        }
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }

}
