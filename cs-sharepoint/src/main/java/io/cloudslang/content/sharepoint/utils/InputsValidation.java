/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.utils;

import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.sharepoint.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    private static List<String> x509HostnameVerifiers = new ArrayList<String>() {{
        add(STRICT);
        add(ALLOW_ALL);
    }};

    private static List<String> entitiesTypeVerifiers = new ArrayList<String>() {{
        add(FOLDERS);
        add(FILES);
        add(ALL);
    }};

    @NotNull
    public static List<String> verifyAuthorizationInputs(@Nullable final String loginType, @Nullable final String clientId, @Nullable final String clientSecret, @Nullable final String username, @Nullable final String password, @NotNull final String proxyPort) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyLoginType(exceptionMessages, loginType, LOGIN_TYPE);
        addVerifyNotNullOrEmpty(exceptionMessages, clientId, CLIENT_ID);
        if (StringUtilities.equalsIgnoreCase(loginType, NATIVE)) {
            addVerifyNotNullOrEmpty(exceptionMessages, username, USERNAME);
            addVerifyNotNullOrEmpty(exceptionMessages, password, PASSWORD);
        }
        if (StringUtilities.equalsIgnoreCase(loginType, API)) {
            addVerifyNotNullOrEmpty(exceptionMessages, clientSecret, CLIENT_SECRET);
        }
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        return exceptionMessages;
    }
    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String proxyPort,
                                                  @Nullable final String trustAllRoots,
                                                  @Nullable final String x509HostnameVerifier,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trustAllRoots, TRUST_ALL_ROOTS);
        addVerifyx509HostnameVerifier(exceptionMessages, x509HostnameVerifier, X509_HOSTNAME_VERIFIER);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyLoginType(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (!(StringUtilities.equalsIgnoreCase(input, API) || StringUtilities.equalsIgnoreCase(input, NATIVE))) {
            exceptions.add(String.format(EXCEPTION_INVALID_LOGIN_TYPE, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, input));
        }
        return exceptions;
    }

    @NotNull
    public static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyx509HostnameVerifier(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (x509HostnameVerifiers.stream().noneMatch(input::equalsIgnoreCase))
            exceptions.add(String.format(EXCEPTION_INVALID_HOSTNAME_VERIFIER, input, inputName));
        return exceptions;
    }

    @NotNull
    public static List<String> addVerifyEntitiesType(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (entitiesTypeVerifiers.stream().noneMatch(input::equalsIgnoreCase))
            exceptions.add(String.format(EXCEPTION_INVALID_ENTITIES_TYPE, input, inputName));
        return exceptions;
    }
}
