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

package io.cloudslang.content.json.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.cloudslang.content.json.entities.AddObjectToArrayInput;
import io.cloudslang.content.json.validators.AddObjectToArrayValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AddObjectToArrayService {

    private final AddObjectToArrayValidator validator = new AddObjectToArrayValidator();


    public @NotNull Map<String, String> execute(@NotNull AddObjectToArrayInput input) {
        List<RuntimeException> validationErrs = this.validator.validate(input);
        if (!validationErrs.isEmpty()) {
            throw validationErrs.get(0);
        }

        JsonArray array = input.getArray();
        JsonElement element = input.getElement();
        int index = getPositiveIndex(input.getArray(), input.getIndex());

        JsonArray resultArray = addToArray(array, element, index);

        String returnResult = resultArray.toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }


    public int getPositiveIndex(JsonArray array, Integer index) {
        if (index == null) {
            return array.size();
        }
        if (index < 0) {
            return array.size() + index;
        }
        return index;
    }


    private JsonArray addToArray(JsonArray array, JsonElement element, int index) {
        JsonArray resultArray = new JsonArray();

        for (int i = 0; i < index; i++) {
            resultArray.add(array.get(i));
        }
        resultArray.add(element);
        for (int i = index; i < array.size(); i++) {
            resultArray.add(array.get(i));
        }

        return resultArray;
    }

}
