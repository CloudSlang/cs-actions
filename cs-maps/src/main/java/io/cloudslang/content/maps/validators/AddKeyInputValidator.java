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
package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class AddKeyInputValidator {

    public ValidationException validate(@NotNull AddKeyInput input) {
        try {
            validateKey(input);
            validateValue(input);
            validatePairDelimiter(input);
            validateEntryDelimiter(input);
            validateMapStart(input);
            validateMapEnd(input);
            validateMap(input);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateMap(AddKeyInput input) throws ValidationException {
        if (StringUtils.isEmpty(input.getMap()) &&
                (StringUtils.isNotEmpty(input.getMapStart()) || StringUtils.isNotEmpty(input.getMapStart()))) {
            throw new ValidationException(ExceptionsMsgs.NO_MAP_PROVIDED);
        }

        if (!input.getMap().startsWith(input.getMapStart())) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_START_WITH_MAP_START);
        }

        if (!input.getMap().endsWith(input.getMapEnd())) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_END_WITH_MAP_END);
        }

        if (input.getMap().length() < input.getMapStart().length() + input.getMapEnd().length()) {
            throw new ValidationException(ExceptionsMsgs.INVALID_MAP);
        }
    }


    private void validateKey(AddKeyInput input) throws ValidationException {
        if (input.getKey() == null) {
            return;
        }

        if (input.getKey().contains(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_PAIR_DELIMITER);
        }

        if (input.getKey().contains(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }


    private void validateValue(AddKeyInput input) throws ValidationException {
        if (input.getValue() == null) {
            return;
        }

        if (input.getValue().contains(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_PAIR_DELIMITER);
        }

        if (input.getValue().contains(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }


    private void validatePairDelimiter(AddKeyInput input) throws ValidationException {
        if (StringUtils.isEmpty(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_PAIR_DELIMITER);
        }

        if(input.getPairDelimiter().contains(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }


    private void validateEntryDelimiter(AddKeyInput input) throws ValidationException {
        if (StringUtils.isEmpty(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_ENTRY_DELIMITER);
        }

        if(input.getEntryDelimiter().equals(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT);
        }
    }


    private void validateMapStart(AddKeyInput input) throws ValidationException {
        if (input.getMapStart() == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_START);
        }
    }


    private void validateMapEnd(AddKeyInput input) throws ValidationException {
        if (input.getMapEnd() == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_END);
        }
    }
}
