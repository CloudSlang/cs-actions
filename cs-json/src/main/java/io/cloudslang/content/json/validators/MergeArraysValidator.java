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

import io.cloudslang.content.json.entities.MergeArraysInput;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MergeArraysValidator {
    public @NotNull List<RuntimeException> validate(@NotNull MergeArraysInput input) {
        List<RuntimeException> errs = new ArrayList<>();

        if (input.getArray1() == null) {
            String msg = String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY1);
            errs.add(new IllegalArgumentException(msg));
        }

        if (input.getArray2() == null) {
            String msg = String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY2);
            errs.add(new IllegalArgumentException(msg));
        }

        return errs;
    }
}
