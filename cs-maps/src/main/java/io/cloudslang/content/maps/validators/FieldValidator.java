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
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public final class FieldValidator {

    private FieldValidator() {

    }


    public static void validateMap(String map, String mapStart, String mapEnd) throws ValidationException {
        if (!map.startsWith(mapStart)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_START_WITH_MAP_START);
        }

        if (!map.endsWith(mapEnd)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_END_WITH_MAP_END);
        }
    }


    public static void validateKey(String key, String pairDelimiter, String entryDelimiter, String elementWrapper, String mapStart, String mapEnd) throws ValidationException {
        if (StringUtils.isBlank(key)) {
            throw new ValidationException(ExceptionsMsgs.NULL_KEY);
        }

        throwIfCommonCharacter(key, pairDelimiter, String.format(ExceptionsMsgs.KEY_CANNOT_CONTAIN, InputNames.PAIR_DELIMITER));
        throwIfCommonCharacter(key, entryDelimiter, String.format(ExceptionsMsgs.KEY_CANNOT_CONTAIN, InputNames.ENTRY_DELIMITER));
        throwIfCommonCharacter(key, elementWrapper, String.format(ExceptionsMsgs.KEY_CANNOT_CONTAIN, InputNames.ELEMENT_WRAPPER));
        throwIfCommonCharacter(key, mapStart, String.format(ExceptionsMsgs.KEY_CANNOT_CONTAIN, InputNames.MAP_START));
        throwIfCommonCharacter(key, mapEnd, String.format(ExceptionsMsgs.KEY_CANNOT_CONTAIN, InputNames.MAP_END));
    }


    public static void validateValue(String value, String pairDelimiter, String entryDelimiter, String elementWrapper, String mapStart, String mapEnd) throws ValidationException {
        if (value == null) {
            return;
        }

        throwIfCommonCharacter(value, pairDelimiter, String.format(ExceptionsMsgs.VALUE_CANNOT_CONTAIN, InputNames.PAIR_DELIMITER));
        throwIfCommonCharacter(value, entryDelimiter, String.format(ExceptionsMsgs.VALUE_CANNOT_CONTAIN, InputNames.ENTRY_DELIMITER));
        throwIfCommonCharacter(value, elementWrapper, String.format(ExceptionsMsgs.VALUE_CANNOT_CONTAIN, InputNames.ELEMENT_WRAPPER));
        throwIfCommonCharacter(value, mapStart, String.format(ExceptionsMsgs.VALUE_CANNOT_CONTAIN, InputNames.MAP_START));
        throwIfCommonCharacter(value, mapEnd, String.format(ExceptionsMsgs.VALUE_CANNOT_CONTAIN, InputNames.MAP_END));
    }


    public static void validatePairDelimiter(String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_PAIR_DELIMITER);
        }

        if (pairDelimiter.contains(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }


    public static void validateEntryDelimiter(String entryDelimiter, String pairDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_ENTRY_DELIMITER);
        }

        if (entryDelimiter.equals(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT);
        }
    }

    public static void validateMethod(String method) throws ValidationException {
        List<String> methodsList = Arrays.asList("to_lowercase", "to_uppercase", "add_prefix", "add_suffix", "strip_spaces");


        if (!methodsList.contains(method.toLowerCase())) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.METHOD));
        }
    }

    public static void validateElements(String elements) throws ValidationException {
        List<String> elementsList = Arrays.asList("keys", "values", "all");

        if (!elementsList.contains(elements.toLowerCase())) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.ELEMENTS));
        }
    }

    public static void validateElementWrapper(String elementWrapper, String pairDelimiter, String entryDelimiter) throws ValidationException {
        throwIfCommonCharacter(elementWrapper, pairDelimiter,
                String.format(ExceptionsMsgs.ELEMENT_WRAPPER_CANNOT_HAVE_COMMON_CHAR_WITH, InputNames.PAIR_DELIMITER));
        throwIfCommonCharacter(elementWrapper,
                entryDelimiter, String.format(ExceptionsMsgs.ELEMENT_WRAPPER_CANNOT_HAVE_COMMON_CHAR_WITH, InputNames.ENTRY_DELIMITER));
    }


    private static void throwIfCommonCharacter(String str1, String str2, String errorMsg) throws ValidationException {
        for (char c : str1.toCharArray()) {
            if (str2.contains(String.valueOf(c))) {
                throw new ValidationException(errorMsg);
            }
        }
    }
}
