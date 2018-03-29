/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.dca.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.dca.utils.Constants.HTTP;
import static io.cloudslang.content.dca.utils.Constants.HTTPS;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.join;

public class Validator {
    private final List<String> errorList;

    public Validator() {
        errorList = new ArrayList<>();
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


    public Validator validateProtocol(@NotNull final String value, @NotNull final String inputName,
                                      @NotNull final List<String> validValues) {
        if (!validValues.contains(value)) {
            errorList.add(format("[%s]: Invalid protocol [%s]. Valid values are [%s]",
                    inputName, value, join(validValues, ", ")));
        }
        return this;
    }

    public Validator validateProtocol(@NotNull final String value, @NotNull final String inputName) {
        return validateProtocol(value, inputName, asList(HTTP, HTTPS));
    }

    public Validator validateInt(@NotNull final String intValue, @NotNull final String inputName) {
        if (!isValidInt(intValue)) {
            errorList.add(format("[%s]: Invalid integer value.", inputName));
        }
        return this;
    }

    public Validator validateBoolean(@NotNull final String booleanValue, @NotNull final String inputName) {
        if (!isValid(booleanValue)) {
            errorList.add(format("[%s]: Invalid boolean value!", inputName));
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
}
