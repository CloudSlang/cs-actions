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
import com.google.gson.JsonParser;
import io.cloudslang.content.json.entities.AddToArrayInput;
import io.cloudslang.content.json.utils.JsonUtils;
import io.cloudslang.content.json.validators.AddToArrayValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AddToArrayService {

    private final AddToArrayValidator validator = new AddToArrayValidator();


    public @NotNull Map<String, String> execute(@NotNull AddToArrayInput input) {
        List<RuntimeException> validationErrs = this.validator.validate(input);
        if (!validationErrs.isEmpty()) {
            throw validationErrs.get(0);
        }

        int index = JsonUtils.getPositiveIndex(input.getArray(), input.getIndex());
        JsonElement element = new JsonParser().parse("\"" + input.getElement().replace("\"", "\\\"") + "\"");
        JsonArray array = JsonUtils.addToArray(input.getArray(), element, index);

        String returnResult = array.toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
