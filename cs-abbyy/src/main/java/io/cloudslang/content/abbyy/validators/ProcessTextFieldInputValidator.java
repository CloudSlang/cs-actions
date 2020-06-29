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

package io.cloudslang.content.abbyy.validators;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.constants.InputNames;
import io.cloudslang.content.abbyy.constants.Limits;
import io.cloudslang.content.abbyy.entities.inputs.ProcessTextFieldInput;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class ProcessTextFieldInputValidator extends AbbyyInputValidator<ProcessTextFieldInput> {

    @Override
    void validateFurther(@NotNull ProcessTextFieldInput request) throws ValidationException {
        validateLanguages(request);
        validatePlaceHoldersCount(request);
        validateDescription(request);
    }


    private void validateLanguages(@NotNull ProcessTextFieldInput request) throws ValidationException {
        if (request.getLanguages() == null) {
            return;
        }

        for (String language : request.getLanguages()) {
            if (StringUtils.isBlank(language)) {
                String msg = String.format(ExceptionMsgs.INVALID_VALUE_DETECTED, language, InputNames.LANGUAGE);
                throw new ValidationException(msg);
            }
        }
    }


    private void validatePlaceHoldersCount(@NotNull ProcessTextFieldInput request) throws ValidationException {
        if (request.getPlaceholdersCount() != null && request.getPlaceholdersCount() < 0) {
            String msg = String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, request.getPlaceholdersCount(), InputNames.PLACEHOLDERS_COUNT);
            throw new ValidationException(msg);
        }
    }


    private void validateDescription(@NotNull ProcessTextFieldInput request) throws ValidationException {
        if (StringUtils.isEmpty(request.getDescription())) {
            return;
        }

        if (request.getDescription().length() > Limits.MAX_SIZE_OF_DESCR) {
            throw new ValidationException(ExceptionMsgs.MAX_SIZE_OF_DESCR_EXCEEDED);
        }
    }
}
