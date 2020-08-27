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
package io.cloudslang.content.maps.utils;

import io.cloudslang.content.maps.constants.DefaultInputValues;
import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public final class BuilderUtils {
    private BuilderUtils () {

    }

    public static boolean parseStripWhitespaces(String stripWhitespaces) throws ValidationException {
        try {
            stripWhitespaces = StringUtils.defaultIfEmpty(stripWhitespaces, DefaultInputValues.STRIP_WHITESPACES);
            return BooleanUtils.toBoolean(stripWhitespaces.toLowerCase(), String.valueOf(true), String.valueOf(false));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.STRIP_WHITESPACES));
        }
    }

    public static boolean handleEmptyValue(String handleEmptyValue) throws ValidationException {
        try {
            handleEmptyValue = StringUtils.defaultIfEmpty(handleEmptyValue, DefaultInputValues.HANDLE_EMPTY_VALUE);
            return BooleanUtils.toBoolean(handleEmptyValue.toLowerCase(), String.valueOf(true), String.valueOf(false));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.HANDLE_EMPTY_VALUE));
        }
    }

    public static boolean ignoreCaseValue (String ignoreCase) throws ValidationException {
        try {
            ignoreCase = StringUtils.defaultIfEmpty(ignoreCase, DefaultInputValues.IGNORE_CASE);
            return BooleanUtils.toBoolean(ignoreCase.toLowerCase(), String.valueOf(true), String.valueOf(false));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.IGNORE_CASE));
        }
    }
}
