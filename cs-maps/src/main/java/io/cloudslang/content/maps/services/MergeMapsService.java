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

import io.cloudslang.content.maps.entities.MergeMapsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import io.cloudslang.content.maps.serialization.MapDeserializer;
import io.cloudslang.content.maps.serialization.MapSerializer;
import io.cloudslang.content.maps.validators.MergeMapsInputValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MergeMapsService {

    private final MergeMapsInputValidator validator = new MergeMapsInputValidator();

    public @NotNull Map<String,String> execute(@NotNull MergeMapsInput input) throws Exception{
        ValidationException validationException = validator.validate(input);
        if(validationException != null){
            throw validationException;
        }

        MapSerializer serializer = new MapSerializer(input.getMap1PairDelimiter(), input.getMap1EntryDelimiter(),
                input.getMap1Start(), input.getMap1End());

        MapDeserializer map1Deserializer = new MapDeserializer(input.getMap1PairDelimiter(), input.getMap1EntryDelimiter(),
                input.getMap1Start(), input.getMap1End());

        MapDeserializer map2Deserializer = new MapDeserializer(input.getMap2PairDelimiter(), input.getMap2EntryDelimiter(),
                input.getMap2Start(), input.getMap2End());

        Map<String, String> map1 = map1Deserializer.deserialize(input.getMap1());
        Map<String, String> map2 = map2Deserializer.deserialize(input.getMap2());

        for(Map.Entry<String,String> entry : map2.entrySet()){
            map1.put(entry.getKey(),entry.getValue());
        }

        String returnResult = serializer.serialize(map1);
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
