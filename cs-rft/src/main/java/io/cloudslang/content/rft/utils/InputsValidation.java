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
package io.cloudslang.content.rft.utils;


import io.cloudslang.content.rft.entities.sftp.SFTPCommonInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Inputs.FTPInputs.*;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.PARAM_CLOSE_SESSION;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    @NotNull
    public static List<String> verifyInputsFTP(
            @Nullable final String hostName,
            @Nullable final String port,
            @Nullable final String localFile,
            @Nullable final String remoteFile,
            @Nullable final String user,
            @Nullable final String password,
            @Nullable final String type,
            @Nullable final String passive,
            @Nullable final String characterSet) {

        final List<String> exceptionMessages = new ArrayList<>();

        addVerifyNotNullOrEmpty(exceptionMessages, hostName, PARAM_HOSTNAME);
        addVerifyPort(exceptionMessages, port);
        addVerifyNotNullOrEmpty(exceptionMessages, localFile, PARAM_LOCAL_FILE);
        addVerifyNotNullOrEmpty(exceptionMessages, remoteFile, PARAM_REMOTE_FILE);
        addVerifyNotNullOrEmpty(exceptionMessages, user, PARAM_USER);
        addVerifyNotNullOrEmpty(exceptionMessages, password, PARAM_PASSWORD);
        addVerifyType(exceptionMessages, type);
        addVerifyBoolean(exceptionMessages, passive, PARAM_PASSIVE);
        addVerifyCharacterSet(exceptionMessages, characterSet);

        return exceptionMessages;
    }


    public static List<String> verifyInputsSFTP(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String username,
            @Nullable final String password,
            @Nullable final String privateKey,
            @Nullable final String characterSet,
            @Nullable final String closeSession,
            @Nullable final SFTPOperation sftpOperation,
            @Nullable final String firstSpecificInput,
            @Nullable final String secondSpecificInput) {

        final List<String> exceptions = verifyCommonSFTPInputs(host,port,username,password,privateKey,characterSet,closeSession);
        if (sftpOperation == SFTPOperation.GET) {
            addVerifyNotNullOrEmpty(exceptions, firstSpecificInput, Inputs.SFTPInputs.PARAM_REMOTE_FILE);
            addVerifyNotNullOrEmpty(exceptions, secondSpecificInput, Inputs.SFTPInputs.PARAM_LOCAL_LOCATION);

        } else if (sftpOperation == SFTPOperation.PUT) {
            addVerifyNotNullOrEmpty(exceptions, firstSpecificInput, Inputs.SFTPInputs.PARAM_REMOTE_LOCATION);
            addVerifyNotNullOrEmpty(exceptions, secondSpecificInput, Inputs.SFTPInputs.PARAM_LOCAL_FILE);

        } else if (sftpOperation == SFTPOperation.GET_CHILDREN) {
            addVerifyNotNullOrEmpty(exceptions,firstSpecificInput,Inputs.SFTPInputs.PARAM_REMOTE_PATH);
            addVerifyNotNullOrEmpty(exceptions,secondSpecificInput,Inputs.SFTPInputs.PARAM_DELIMITER);
        }
        return exceptions;
    }

    private static List<String> verifyCommonSFTPInputs(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String username,
            @Nullable final String password,
            @Nullable final String privateKey,
            @Nullable final String characterSet,
            @Nullable final String closeSession) {

        final List<String> exceptions = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptions, host, Inputs.SFTPInputs.PARAM_HOST);
        addVerifyPort(exceptions, port);
        addVerifyNotNullOrEmpty(exceptions, username, Inputs.SFTPInputs.PARAM_USERNAME);
        addVerifyNotNullOrEmpty(exceptions, password, Inputs.SFTPInputs.PARAM_PASSWORD);
        addVerifyCharacterSet(exceptions, characterSet);
        addVerifyBoolean(exceptions,closeSession, PARAM_CLOSE_SESSION);

        return exceptions;

    }

    private static void addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (!isValid(input))
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
    }

    private static void addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
    }

    private static void addVerifyPort(@NotNull List<String> exceptions, @Nullable final String input) {
        int port;
        if (input != null && input.length() > 0) {
            port = tryParse(input);
            if (port < 0 || port > 65535) {
                exceptions.add(String.format(EXCEPTION_INVALID_PORT, input));
            }
        }
    }

    private static void addVerifyCharacterSet(@NotNull List<String> exceptions, @Nullable final String input) {
        if (!Charset.availableCharsets().containsKey(input)) {
            exceptions.add(EXCEPTION_CHARACTER_SET);
        }

    }

    private static void addVerifyType(@NotNull List<String> exceptions, @Nullable final String input) {
        if (!isValidType(input))
            exceptions.add(String.format(EXCEPTION_INVALID_TYPE, input, PARAM_TYPE));
    }

    private static boolean isValidType(@Nullable final String type) {
        if (isEmpty(type))
            return false;
        else {
            final String lowerCaseType = StringUtils.strip(type).toLowerCase();
            return lowerCaseType.equals(BINARY_FILE_TYPE) || lowerCaseType.equals(ASCII_FILE_TYPE);
        }
    }

    private static Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
