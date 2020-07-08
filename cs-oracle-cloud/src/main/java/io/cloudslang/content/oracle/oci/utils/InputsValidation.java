/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.oci.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String privateKeyData,
                                                  @Nullable final String privateKeyFile,
                                                  @Nullable final String proxyPort) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        verifyPrivateKey(exceptionMessages, privateKeyData, privateKeyFile);

        return exceptionMessages;
    }

    @NotNull
    private static List<String> verifyPrivateKey(@NotNull List<String> exceptions, @Nullable final String privateKeyData, @NotNull final String privateKeyFile) {
        if (isEmpty(privateKeyData) && isEmpty(privateKeyFile)) {
            exceptions.add(EXCEPTION_NULL_EMPTY_PRIVATE_KEY);
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input,
                                               @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input,
                                                 @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input,
                                                @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> isNumber(@NotNull List<String> exceptions, @Nullable final String input,
                                         @NotNull final String inputName) {
        if (!isEmpty(input) && !NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    public static List<String> verifyJSONObject(@NotNull List<String> exceptions, @Nullable final String input,
                                                @NotNull final String inputName) {
        try {
            if (!isEmpty(input))
                new ObjectMapper().readTree(input);
        } catch (IOException e) {
            exceptions.add(String.format(EXCEPTION_INVALID_JSON, input, inputName));
        }
        return exceptions;
    }
}
