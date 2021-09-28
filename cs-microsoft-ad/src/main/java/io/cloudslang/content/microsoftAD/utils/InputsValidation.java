/*
 * (c) Copyright 2021 Micro Focus, L.P.
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

package io.cloudslang.content.microsoftAD.utils;

import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.EXCEPTION_INVALID_HOSTNAME_VERIFIER;
import static io.cloudslang.content.microsoftAD.utils.Descriptions.Common.EXCEPTION_INVALID_TLS_VERSION;
import static io.cloudslang.content.microsoftAD.utils.Inputs.AuthorizationInputs.PASSWORD;
import static io.cloudslang.content.microsoftAD.utils.Inputs.AuthorizationInputs.USERNAME;
import static io.cloudslang.content.microsoftAD.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.microsoftAD.utils.Inputs.CreateUser.*;
import static io.cloudslang.content.microsoftAD.utils.Inputs.DeleteUser.USER_ID;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {

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
    public static List<String> verifyDeleteInputs(@Nullable final String userPrincipalName,
                                                  @Nullable final String userId,
                                                  @Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots,
                                                  @Nullable final String x509HostnameVerifier,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyUserInputs(exceptionMessages, userPrincipalName, userId);

        exceptionMessages.addAll(verifyCommonUserInputs(proxyPort, trust_all_roots, x509HostnameVerifier,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal));
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetInputs(@Nullable final String userPrincipalName,
                                               @Nullable final String userId,
                                               @Nullable final String proxyPort,
                                               @Nullable final String trust_all_roots,
                                               @Nullable final String x509HostnameVerifier,
                                               @Nullable final String connectTimeout,
                                               @Nullable final String socketTimeout,
                                               @Nullable final String keepAlive,
                                               @Nullable final String connectionsMaxPerRoute,
                                               @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyUserInputs(exceptionMessages, userPrincipalName, userId);

        exceptionMessages.addAll(verifyCommonUserInputs(proxyPort, trust_all_roots, x509HostnameVerifier,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal));
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCommonUserInputs(@Nullable final String proxyPort,
                                                      @Nullable final String trust_all_roots,
                                                      @Nullable final String x509HostnameVerifier,
                                                      @Nullable final String connectTimeout,
                                                      @Nullable final String socketTimeout,
                                                      @Nullable final String keepAlive,
                                                      @Nullable final String connectionsMaxPerRoute,
                                                      @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyx509HostnameVerifier(exceptionMessages, x509HostnameVerifier, X509_HOSTNAME_VERIFIER);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCreateUserInputs(@Nullable final String accountEnabled,
                                                      @Nullable final String displayName,
                                                      @Nullable final String mailNickname,
                                                      @Nullable final String userPrincipalName,
                                                      @Nullable final String forceChangePassword,
                                                      @Nullable final String password,
                                                      @Nullable final String proxyPort,
                                                      @Nullable final String trust_all_roots,
                                                      @Nullable final String x509HostnameVerifier,
                                                      @Nullable final String connectTimeout,
                                                      @Nullable final String socketTimeout,
                                                      @Nullable final String keepAlive,
                                                      @Nullable final String connectionsMaxPerRoute,
                                                      @Nullable final String connectionsMaxTotal) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptionMessages, accountEnabled, ACCOUNT_ENABLED);
        addVerifyBoolean(exceptionMessages, accountEnabled, ACCOUNT_ENABLED);
        addVerifyNotNullOrEmpty(exceptionMessages, displayName, DISPLAY_NAME);
        addVerifyNotNullOrEmpty(exceptionMessages, mailNickname, MAIL_NICKNAME);
        addVerifyNotNullOrEmpty(exceptionMessages, userPrincipalName, USER_PRINCIPAL_NAME);
        addVerifyNotNullOrEmpty(exceptionMessages, forceChangePassword, FORCE_CHANGE_PASSWORD);
        addVerifyBoolean(exceptionMessages, forceChangePassword, FORCE_CHANGE_PASSWORD);
        addVerifyNotNullOrEmpty(exceptionMessages, password, PASSWORD);

        exceptionMessages.addAll(verifyCommonUserInputs(proxyPort, trust_all_roots, x509HostnameVerifier,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal));

        return exceptionMessages;
    }


    @NotNull
    private static List<String> addVerifyUserInputs(@NotNull List<String> exceptions, @Nullable final String userPrincipalName,
                                                    @Nullable final String userId) {
        if (isEmpty(userPrincipalName) && isEmpty(userId)) {
            exceptions.add(String.format(EXCEPTION_INVALID_LOGIN_TYPE_REST, USER_PRINCIPAL_NAME, USER_ID));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyLoginType(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (!(StringUtilities.equalsIgnoreCase(input, API) || StringUtilities.equalsIgnoreCase(input, NATIVE))) {
            exceptions.add(String.format(EXCEPTION_INVALID_LOGIN_TYPE, inputName));
        }
        return exceptions;
    }

    @NotNull
    public static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
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
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, PROXY_PORT));
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
        List<String> x509HostnameVerifiers = new ArrayList<>();
        x509HostnameVerifiers.add(STRICT);
        x509HostnameVerifiers.add(ALLOW_ALL);
        x509HostnameVerifiers.add(BROWSER_COMPATIBLE);
        if (!x509HostnameVerifiers.contains(input.toLowerCase().trim()))
            exceptions.add(String.format(EXCEPTION_INVALID_HOSTNAME_VERIFIER, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyTlsVersion(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> tlsVersions = new ArrayList<>();
        tlsVersions.add(TLSV1);
        tlsVersions.add(TLSV11);
        tlsVersions.add(TLSV12);
        tlsVersions.add(TLSV13);
        if (!tlsVersions.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_TLS_VERSION, input, inputName));
        return exceptions;
    }

}

