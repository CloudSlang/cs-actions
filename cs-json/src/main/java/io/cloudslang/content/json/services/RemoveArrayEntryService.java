/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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

import io.cloudslang.content.json.entities.RemoveArrayEntryInput;
import io.cloudslang.content.json.validators.RemoveArrayEntryValidator;
import io.cloudslang.content.utils.OutputUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class RemoveArrayEntryService {

    private final RemoveArrayEntryValidator validator = new RemoveArrayEntryValidator();


    public @NotNull Map<String, String> execute(@NotNull RemoveArrayEntryInput input) {
        List<RuntimeException> validationErrs = this.validator.validate(input);
        if (!validationErrs.isEmpty()) {
            throw validationErrs.get(0);
        }

        int index = input.getIndex() >= 0 ? input.getIndex() : input.getArray().size() + input.getIndex();
        input.getArray().remove(index);

        String returnResult = input.getArray().toString();
        return OutputUtilities.getSuccessResultsMap(returnResult);
    }
}
