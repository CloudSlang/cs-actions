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

package io.cloudslang.content.json.validators;


import io.cloudslang.content.json.entities.GetArraySublistInput;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import io.cloudslang.content.json.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.json.utils.Constants.GetArraySublistAction.*;

public class GetArraySublistValidator {
    public @NotNull List<RuntimeException> validate(GetArraySublistInput input) {
        List<RuntimeException> errs = new ArrayList<>();

        addErrsForArray(errs, input);
        addErrsForFromIndex(errs, input);
        addErrsForToIndex(errs, input);

        return errs;
    }


    private void addErrsForArray(List<RuntimeException> errs, GetArraySublistInput input) {
        if (input.getArray() == null) {
            errs.add(new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY)));
        }
    }

    private void addErrsForFromIndex(List<RuntimeException> errs, GetArraySublistInput input) {
        if(input.getFromIndex() >= input.getArray().size() || input.getFromIndex() < 0) {
            errs.add(new IllegalArgumentException(INVALID_FROM_INDEX_VALUE));
        }
    }

    private void addErrsForToIndex(List<RuntimeException> errs, GetArraySublistInput input) {
        if(input.getToIndex() > input.getArray().size() || input.getToIndex() < 0) {
            errs.add(new IllegalArgumentException(INVALID_TO_INDEX_VALUE));
        }
        if(input.getFromIndex() >= input.getToIndex()) {
            errs.add(new IllegalArgumentException(TO_INDEX_HIGHER_THAN_FROM_INDEX));
        }
    }
}
