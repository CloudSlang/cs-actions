/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.rft.utils.ftp;


import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.rft.utils.ftp.Inputs.*;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class FTPInputsValidation {

    @NotNull
    public static List<String> verifyInputsFTP(
            @NotNull final String hostName,
            @NotNull final String port,
            @NotNull final String localFile,
            @NotNull final String remoteFile,
            @NotNull final String user,
            @NotNull final String password,
            @NotNull final String type,
            @NotNull final String passive,
            @NotNull final String characterSet) {

        final List<String> exceptionMessages = new ArrayList<>();

        addVerifyNotNullOrEmpty(exceptionMessages, hostName, PARAM_HOSTNAME);
        addVerifyPort(exceptionMessages, port);
        addVerifyNotNullOrEmpty(exceptionMessages, localFile, PARAM_LOCAL_FILE);
        addVerifyNotNullOrEmpty(exceptionMessages, remoteFile, PARAM_REMOTE_FILE);
         addVerifyNotNullOrEmpty(exceptionMessages,user,PARAM_USER);
        // addVerifyNotNullOrEmpty(exceptionMessages,password,PARAM_PASSWORD);
        addVerifyType(exceptionMessages, type);
        addVerifyBoolean(exceptionMessages, passive);
        addVerifyCharacterSet(exceptionMessages, characterSet);

        return exceptionMessages;
    }

    private static void addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input) {
        if (!isValid(input))
            exceptions.add(String.format(Constants.EXCEPTION_INVALID_BOOLEAN, input, Inputs.PARAM_PASSIVE));
    }

    private static void addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(Constants.EXCEPTION_NULL_EMPTY, inputName));
        }
    }

    private static void addVerifyPort(@NotNull List<String> exceptions, @Nullable final String input) {
        int port;
        if (input != null && input.length() > 0) {
            port = tryParse(input);
            if (port < 0 || port > 65535) {
                exceptions.add(String.format(Constants.EXCEPTION_INVALID_PORT, input));
            }
        }
    }

    private static void addVerifyCharacterSet(@NotNull List<String> exceptions, @Nullable final String input) {
        if (!Charset.availableCharsets().containsKey(input)){
            exceptions.add(Constants.EXCEPTION_CHARACTER_SET);
        }

    }

    private static void addVerifyType(@NotNull List<String> exceptions, @Nullable final String input) {
        if (!isValidType(input))
            exceptions.add(String.format(Constants.EXCEPTION_INVALID_TYPE, input, Inputs.PARAM_TYPE));
    }

    private static boolean isValidType(@NotNull final String type) {
        final String lowerCaseType = StringUtils.strip(type).toLowerCase();
        return lowerCaseType.equals(Constants.BINARY_FILE_TYPE) || lowerCaseType.equals(Constants.ASCII_FILE_TYPE);
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
