/*
 * (c) Copyright 2019 Micro Focus
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


package io.cloudslang.content.alibaba.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

public class Validator {
    private final List<String> errorList;

    public Validator() {
        errorList = new ArrayList<>();
    }

    public static boolean isValidFloat(@Nullable String floatStr) {
        if (StringUtils.isBlank(floatStr)) {
            return false;
        } else {
            String stripedInteger = StringUtils.strip(floatStr);

            try {
                NumberUtils.createFloat(stripedInteger);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public String getErrors() {
        return getErrors(System.lineSeparator());
    }

    public String getErrors(@NotNull final String delimiter) {
        return join(errorList, delimiter);
    }

    public Validator validatePort(@NotNull final String value, @NotNull final String inputName) {
        if (!isValidIpPort(value)) {
            errorList.add(format("[%s]: Invalid port value [%s]!", inputName, value));
        }
        return this;
    }

    public Validator validateInt(@NotNull final String intValue, @NotNull final String inputName) {
        if (!isValidInt(intValue)) {
            errorList.add(format("[%s]: Invalid integer value.", inputName));
        }
        return this;
    }

    public Validator validateInt(@NotNull final String intValue, @NotNull final String inputName, @NotNull final int lowerBound, @NotNull final int upperBound, @NotNull final boolean includeLowerBound, @NotNull final boolean includeUpperBound) {
        if (!isValidInt(intValue)) {
            errorList.add(format("[%s]: Invalid integer value.", inputName));
        } else {
            if (!isValidInt(intValue, lowerBound, upperBound, includeLowerBound, includeUpperBound)) {
                errorList.add(format("[%s]: Invalid integer range [" + lowerBound + " , " + upperBound + "].", inputName));
            }
        }
        return this;
    }

    public Validator validateFloat(@NotNull final String floatValue, @NotNull final String inputName) {
        if (!isValidFloat(floatValue)) {
            errorList.add(format("[%s]: Invalid float value.", inputName));
        }
        return this;
    }

    public Validator validateIntList(@NotNull final String intValueList, @NotNull final String inputName, @NotNull final String delimiter) {
        for (String value : intValueList.split(delimiter)) {
            if (!isValidInt(value)) {
                errorList.add(format("[%s]: Invalid integer value - [%s].", inputName, value));
            }
        }
        return this;
    }

    public Validator validateBoolean(@NotNull final String booleanValue, @NotNull final String inputName) {
        if (!isValid(booleanValue)) {
            errorList.add(format("[%s]: Invalid boolean value!", inputName));
        }
        return this;
    }

    public Validator validateBooleanList(@NotNull final String booleanValueList, @NotNull final String inputName, @NotNull final String delimiter) {
        for (String value : booleanValueList.split(delimiter)) {
            if (!isValid(value)) {
                errorList.add(format("[%s]: Invalid boolean value - [%s].", inputName, value));
            }
        }
        return this;
    }

    @SafeVarargs
    public final Validator validateSameLength(@NotNull final List<String> listNames,
                                              @NotNull final List<String>... args) {
        if (args.length < 1) {
            return this;
        }

        final List<String> firstList = args[0];
        for (final List<String> list : args) {
            if (list.size() != firstList.size()) {
                errorList.add(format("The lists [%s] are not the same length!",
                        join(listNames, ", ")));
            }
        }

        return this;
    }

    @SafeVarargs
    public final Validator validateSameLengthSizeBound(@NotNull final Integer size,
                                                       @NotNull final List<String> listNames,
                                                       @NotNull final List<String>... args) {
        if (args.length < 1) {
            return this;
        }

        final List<String> firstList = args[0];
        for (final List<String> list : args) {
            if (list.size() != firstList.size()) {
                errorList.add(format("The lists [%s] are not the same length!",
                        join(listNames, ", ")));
            }
        }

        if (firstList.size() > size) {
            errorList.add(format("Some of the lists [%s] contain more than [%s] elements!",
                    join(listNames, ", "), Integer.toString(size)));
        }

        return this;
    }
}
