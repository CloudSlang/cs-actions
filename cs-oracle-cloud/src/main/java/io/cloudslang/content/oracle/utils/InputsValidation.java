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

package io.cloudslang.content.oracle.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.oracle.utils.Constants.Common.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        return exceptionMessages;
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
}
