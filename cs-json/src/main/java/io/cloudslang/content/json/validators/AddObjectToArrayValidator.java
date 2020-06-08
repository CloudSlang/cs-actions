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
package io.cloudslang.content.json.validators;

import io.cloudslang.content.json.entities.AddObjectToArrayInput;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddObjectToArrayValidator {
    public @NotNull List<RuntimeException> validate(@NotNull AddObjectToArrayInput input) {
        List<RuntimeException> errs = new ArrayList<>();

        addErrsForArray(errs, input);
        addErrsForElement(errs, input);
        addErrsForIndex(errs, input);

        return errs;
    }


    private void addErrsForArray(List<RuntimeException> errs, AddObjectToArrayInput input) {
        if (input.getArray() == null) {
            String msg = String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY);
            errs.add(new IllegalArgumentException(msg));
        }
    }


    private void addErrsForElement(List<RuntimeException> errs, AddObjectToArrayInput input) {
        if (input.getElement() == null) {
            String msg = String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ELEMENT);
            errs.add(new IllegalArgumentException(msg));
        }
    }


    private void addErrsForIndex(List<RuntimeException> errs, AddObjectToArrayInput input) {
        if (input.getArray() == null || input.getIndex() == null) {
            return;
        }

        if (input.getIndex() < -input.getArray().size() || input.getIndex() > input.getArray().size()) {
            errs.add(new IndexOutOfBoundsException());
        }
    }
}
