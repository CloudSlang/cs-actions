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


import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import io.cloudslang.content.json.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.json.utils.Constants.GetArraySublistAction.*;

public class GetArraySublistValidator {
    public static @NotNull List<RuntimeException> validate(String array, String fromIndex, String toIndex) {
        List<RuntimeException> errs = new ArrayList<>();

        if (StringUtils.isEmpty(array)) {
            errs.add(new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY)));
        }

        try {
            if (StringUtils.isEmpty(fromIndex)) {
                errs.add(new IllegalArgumentException(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.FROM_INDEX)));
            }
            if (Integer.parseInt(fromIndex) < 0) {
                errs.add(new IllegalArgumentException(NEGATIVE_FROM_INPUT_VALUE));
            }
        } catch (Exception e) {
            errs.add(new RuntimeException(e.getMessage()));
        }

        try {
            if (!StringUtils.isEmpty(toIndex)) {
                if (Integer.parseInt(toIndex) < 0) {
                    errs.add(new IllegalArgumentException(NEGATIVE_TO_INPUT_VALUE));
                }
                if (Integer.parseInt(toIndex) <= Integer.parseInt(fromIndex)) {
                    errs.add(new IllegalArgumentException(TO_INDEX_HIGHER_THAN_FROM_INDEX));
                }
            }
        } catch (Exception e) {
            errs.add(new RuntimeException(e.getMessage()));
        }

        return errs;
    }
}
