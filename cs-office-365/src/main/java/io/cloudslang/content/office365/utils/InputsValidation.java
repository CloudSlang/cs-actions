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

import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.office365.utils.AuthorizationInputNames.*;
import static io.cloudslang.content.office365.utils.Constants.*;
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

//    //This method is commented as it's not used at this point. You can use it for numeric validation.
//    @NotNull
//    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
//        if (StringUtilities.isEmpty(input)) {
//            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
//        } else if (!NumberUtilities.isValidInt(input)) {
//            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, PROXY_PORT));
//        }
//        return exceptions;
//    }
}
