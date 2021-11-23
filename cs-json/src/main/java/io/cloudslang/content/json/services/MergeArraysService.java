/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
import io.cloudslang.content.json.entities.MergeArraysInput;
import io.cloudslang.content.json.validators.MergeArraysValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MergeArraysService {

    private final MergeArraysValidator validator = new MergeArraysValidator();


    public @NotNull Map<String, String> execute(@NotNull MergeArraysInput input) {
        List<RuntimeException> validationErrs = this.validator.validate(input);
        if (!validationErrs.isEmpty()) {
            throw validationErrs.get(0);
        }

        JsonArray mergedArray = new JsonArray();
        mergedArray.addAll(input.getArray1());
        mergedArray.addAll(input.getArray2());

        String returnResult = mergedArray.toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
