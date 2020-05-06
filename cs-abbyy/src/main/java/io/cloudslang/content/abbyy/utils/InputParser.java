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

package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.constants.InputNames;
import io.cloudslang.content.abbyy.entities.Region;
import io.cloudslang.content.constants.BooleanValues;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.EnumSet;

public final class InputParser {
    private InputParser() {

    }


    public static String parseDescription(String descr) {
        if (StringUtils.isNotEmpty(descr)) {
            return (descr.length() <= 255) ? descr : descr.substring(0, 255);
        } else {
            return StringUtils.EMPTY;
        }
    }


    public static short parseShort(String value, String inputName) {
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, value, inputName));
        }
    }


    public static int parseInt(String value, String inputName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, value, inputName));
        }
    }


    public static boolean parseBoolean(String value, String inputName) {
        try {
            return BooleanUtils.toBoolean(value, BooleanValues.TRUE, BooleanValues.FALSE);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, value, inputName));
        }
    }


    public static Region parseRegion(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, value, InputNames.REGION));
        }

        String[] coords = value.split("[,]");
        if (coords.length != 4) {
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_NR_OF_COORDS);
        }

        int left = Integer.parseInt(coords[0]);
        int top = Integer.parseInt(coords[1]);
        int right = Integer.parseInt(coords[2]);
        int bottom = Integer.parseInt(coords[3]);

        return new Region(left, top, right, bottom);
    }


    public static <T extends Enum<T>> T parseEnum(String value, Class<T> clazz, String inputName) {
        for (T enumm : EnumSet.allOf(clazz)) {
            if (enumm.toString().equals(value)) {
                return (T) enumm;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_VALUE_FOR_INPUT, value, inputName));
    }
}
