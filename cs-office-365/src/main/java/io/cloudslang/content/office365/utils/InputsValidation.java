/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.utils;

import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.office365.utils.Constants.*;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.*;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.PASSWORD;
import static io.cloudslang.content.office365.utils.Inputs.AuthorizationInputs.USERNAME;
import static io.cloudslang.content.office365.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.*;
import static io.cloudslang.content.office365.utils.Inputs.CreateMessage.IS_READ_RECEIPT_REQUESTED;
import static io.cloudslang.content.office365.utils.Inputs.EmailInputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;

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
    public static List<String> verifyGetMessageInputs(@Nullable final String messageId,
                                                      @Nullable final String userPrincipalName,
                                                      @Nullable final String userId,
                                                      @Nullable final String proxyPort,
                                                      @Nullable final String trust_all_roots,
                                                      @Nullable final String connectTimeout,
                                                      @Nullable final String socketTimeout,
                                                      @Nullable final String keepAlive,
                                                      @Nullable final String connectionsMaxPerRoute,
                                                      @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = verifyCommonInputs(userPrincipalName, userId, proxyPort, trust_all_roots,
                connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);

        addVerifyNotNullOrEmpty(exceptionMessages, messageId, MESSAGE_ID);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String userPrincipalName,
                                                  @Nullable final String userId,
                                                  @Nullable final String proxyPort,
                                                  @Nullable final String trust_all_roots,
                                                  @Nullable final String connectTimeout,
                                                  @Nullable final String socketTimeout,
                                                  @Nullable final String keepAlive,
                                                  @Nullable final String connectionsMaxPerRoute,
                                                  @Nullable final String connectionsMaxTotal) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyUserInputs(exceptionMessages, userPrincipalName, userId);
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
    public static List<String> verifyCreateMessageInputs(@Nullable final String sender, @Nullable final String proxyPort, @Nullable final String trust_all_roots,
                                                         @Nullable final String userPrincipalName, @Nullable final String userId, @Nullable final String connectTimeout,
                                                         @Nullable final String toRecipients, @Nullable final String isRead, @Nullable final String isDeliveryReceiptRequested,
                                                         @Nullable final String isReadReceiptRequested,
                                                         @Nullable final String socketTimeout, @Nullable final String keepAlive, @Nullable final String connectionsMaxPerRoute,
                                                         @Nullable final String connectionsMaxTotal) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyUserInputs(exceptionMessages, userPrincipalName, userId);
        addVerifyNotNullOrEmpty(exceptionMessages, sender, SENDER);
        addVerifyNotNullOrEmpty(exceptionMessages, toRecipients, TO_RECIPIENTS);
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyNumber(exceptionMessages, connectTimeout, CONNECT_TIMEOUT);
        addVerifyNumber(exceptionMessages, socketTimeout, SOCKET_TIMEOUT);
        addVerifyBoolean(exceptionMessages, keepAlive, KEEP_ALIVE);
        addVerifyBoolean(exceptionMessages, isRead, IS_READ);
        addVerifyBoolean(exceptionMessages, isDeliveryReceiptRequested, IS_DELIVERY_RECEIPT_REQUESTED);
        addVerifyBoolean(exceptionMessages, isReadReceiptRequested, IS_READ_RECEIPT_REQUESTED);
        addVerifyNumber(exceptionMessages, connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE);
        addVerifyNumber(exceptionMessages, connectionsMaxTotal, CONNECTIONS_MAX_TOTAL);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyUserInputs(@NotNull List<String> exceptions, @Nullable final String userPrincipalName,
                                                    @Nullable final String userId) {
        if (StringUtils.isEmpty(userPrincipalName) && StringUtils.isEmpty(userId)) {
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
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, PROXY_PORT));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (StringUtilities.isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }
}
