/*
 * (c) Copyright 2021 Micro Focus
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


import io.cloudslang.content.utils.NumberUtilities;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Inputs.CommonInputs.CONNECTION_TIMEOUT;
import static io.cloudslang.content.rft.utils.Inputs.CommonInputs.EXECUTION_TIMEOUT;
import static io.cloudslang.content.rft.utils.Inputs.FTPInputs.PASSWORD;
import static io.cloudslang.content.rft.utils.Inputs.FTPInputs.*;
import static io.cloudslang.content.rft.utils.Inputs.RemoteSecureCopyInputs.DESTINATION_HOST;
import static io.cloudslang.content.rft.utils.Inputs.RemoteSecureCopyInputs.SOURCE_HOST;
import static io.cloudslang.content.rft.utils.Inputs.RemoteSecureCopyInputs.SOURCE_PATH;
import static io.cloudslang.content.rft.utils.Inputs.SCPInputs.LOCAL_FILE;
import static io.cloudslang.content.rft.utils.Inputs.SCPInputs.REMOTE_FILE;
import static io.cloudslang.content.rft.utils.Inputs.SCPInputs.*;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs;
import static io.cloudslang.content.rft.utils.Inputs.SFTPInputs.CLOSE_SESSION;
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

        addVerifyNotNullOrEmpty(exceptionMessages, hostName, HOST_NAME);
        addVerifyPort(exceptionMessages, port);
        addVerifyNotNullOrEmpty(exceptionMessages, localFile, LOCAL_FILE);
        addVerifyNotNullOrEmpty(exceptionMessages, remoteFile, REMOTE_FILE);
        addVerifyNotNullOrEmpty(exceptionMessages, user, USER);
        addVerifyNotNullOrEmpty(exceptionMessages, password, PASSWORD);
        addVerifyType(exceptionMessages, type);
        addVerifyBoolean(exceptionMessages, passive, PASSIVE);
        addVerifyCharacterSet(exceptionMessages, characterSet);

        return exceptionMessages;
    }

    public static List<String> verifyGetChildrenInputsSFTP(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String username,
            @Nullable final String password,
            @Nullable final String proxyPort,
            @Nullable final String characterSet,
            @Nullable final String closeSession,
            @Nullable final String firstSpecificInput,
            @Nullable final String secondSpecificInput,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout) {

        final List<String> exceptions = verifyCommonSFTPInputs(host, port, username, password, proxyPort,
                characterSet, closeSession, connectionTimeout, executionTimeout);

        addVerifyNotNullOrEmpty(exceptions, firstSpecificInput, SFTPInputs.REMOTE_PATH);
        addVerifyNotNullOrEmpty(exceptions, secondSpecificInput, SFTPInputs.DELIMITER);

        return exceptions;
    }

    public static List<String> verifySFTPFileInputs(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String username,
            @Nullable final String password,
            @Nullable final String proxyPort,
            @Nullable final String characterSet,
            @Nullable final String closeSession,
            @Nullable final SFTPOperation sftpOperation,
            @Nullable final String firstSpecificInput,
            @Nullable final String secondSpecificInput,
            @Nullable final String thirdSpecificInput,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout) {

        final List<String> exceptions = verifyCommonSFTPInputs(host, port, username, password, proxyPort,
                characterSet, closeSession, connectionTimeout, executionTimeout);

        if (sftpOperation == SFTPOperation.GET) {
            addVerifyNotNullOrEmpty(exceptions, firstSpecificInput, SFTPInputs.REMOTE_FILE);
            addVerifyNotNullOrEmpty(exceptions, secondSpecificInput, SFTPInputs.REMOTE_LOCATION);
            addVerifyNotNullOrEmpty(exceptions, thirdSpecificInput, SFTPInputs.LOCAL_LOCATION);

        } else if (sftpOperation == SFTPOperation.PUT) {
            addVerifyNotNullOrEmpty(exceptions, firstSpecificInput, SFTPInputs.REMOTE_PATH_PUT);
            addVerifyNotNullOrEmpty(exceptions, secondSpecificInput, SFTPInputs.LOCAL_PATH_PUT);
            addVerifyNotNullOrEmpty(exceptions, thirdSpecificInput, SFTPInputs.LOCAL_FILE);
        }

        return exceptions;
    }


    private static List<String> verifyCommonSFTPInputs(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String username,
            @Nullable final String password,
            @Nullable final String proxyPort,
            @Nullable final String characterSet,
            @Nullable final String closeSession,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout) {

        final List<String> exceptions = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptions, host, SFTPInputs.HOST);
        addVerifyPort(exceptions, port);
        addVerifyNotNullOrEmpty(exceptions, username, SFTPInputs.USERNAME);
        addVerifyNotNullOrEmpty(exceptions, password, SFTPInputs.PASSWORD);
        addVerifyPort(exceptions, proxyPort);
        addVerifyCharacterSet(exceptions, characterSet);
        addVerifyBoolean(exceptions, closeSession, CLOSE_SESSION);
        addVerifyNumber(exceptions, connectionTimeout, CONNECTION_TIMEOUT);
        addVerifyNumber(exceptions, executionTimeout, EXECUTION_TIMEOUT);

        return exceptions;
    }

    public static List<String> verifyRemoteCopyInputs(
            @Nullable final String sourceHost,
            @Nullable final String sourcePort,
            @Nullable final String sourcePath,
            @Nullable final String sourceProtocol,
            @Nullable final String sourceCharacterSet,
            @Nullable final String destinationHost,
            @Nullable final String destinationPort,
            @Nullable final String destinationPath,
            @Nullable final String destinationProtocol,
            @Nullable final String destinationCharacterSet,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout) {

        final List<String> exceptions = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptions, sourceHost, SOURCE_HOST);
        addVerifyNotNullOrEmpty(exceptions, destinationHost, DESTINATION_HOST);
        addVerifyPort(exceptions, sourcePort);
        addVerifyPort(exceptions, destinationPort);
        addVerifyNotNullOrEmpty(exceptions, sourcePath, SOURCE_PATH);
        addVerifyNotNullOrEmpty(exceptions, destinationPath, SOURCE_PATH);
        addVerifyProtocol(exceptions, sourceProtocol);
        addVerifyProtocol(exceptions, destinationProtocol);
        addVerifyCharacterSet(exceptions, sourceCharacterSet);
        addVerifyCharacterSet(exceptions, destinationCharacterSet);
        addVerifyNumber(exceptions, connectionTimeout, CONNECTION_TIMEOUT);
        addVerifyNumber(exceptions, executionTimeout, EXECUTION_TIMEOUT);

        return exceptions;
    }

    public static List<String> verifySCPRemoteCopyFileInputs(
            @Nullable final String sourceHost,
            @Nullable final String sourcePort,
            @Nullable final String sourcePath,
            @Nullable final String destinationHost,
            @Nullable final String destinationPort,
            @Nullable final String destinationPath,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout,
            @Nullable final String knownHostsPolicy) {

        final List<String> exceptions = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptions, sourceHost, SOURCE_HOST);
        addVerifyNotNullOrEmpty(exceptions, destinationHost, DESTINATION_HOST);
        addVerifyPort(exceptions, sourcePort);
        addVerifyPort(exceptions, destinationPort);
        addVerifyNotNullOrEmpty(exceptions, sourcePath, SOURCE_PATH);
        addVerifyNotNullOrEmpty(exceptions, destinationPath, SOURCE_PATH);
        addVerifyNumber(exceptions, connectionTimeout, CONNECTION_TIMEOUT);
        addVerifyNumber(exceptions, executionTimeout, EXECUTION_TIMEOUT);
        addVerifyKnownHostsPolicies(exceptions, knownHostsPolicy);
        return exceptions;
    }

    public static List<String> verifySCPCopyFileInputs(
            @Nullable final String host,
            @Nullable final String port,
            @Nullable final String localFile,
            @Nullable final String copyAction,
            @Nullable final String remoteFile,
            @Nullable final String connectionTimeout,
            @Nullable final String executionTimeout,
            @Nullable final String knownHostsPolicy) {

        final List<String> exceptions = new ArrayList<>();
        addVerifyNotNullOrEmpty(exceptions, host, HOST);
        addVerifyNotNullOrEmpty(exceptions, localFile, LOCAL_FILE);
        addVerifyNotNullOrEmpty(exceptions, remoteFile, REMOTE_FILE);
        addVerifyCopyAction(exceptions, copyAction);
        addVerifyPort(exceptions, port);
        addVerifyNumber(exceptions, connectionTimeout, CONNECTION_TIMEOUT);
        addVerifyNumber(exceptions, executionTimeout, EXECUTION_TIMEOUT);
        addVerifyKnownHostsPolicies(exceptions, knownHostsPolicy);
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
            exceptions.add(String.format(EXCEPTION_INVALID_TYPE, input, TYPE));
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

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        } else if (Integer.parseInt(input) < 0) {
            exceptions.add(String.format(EXCEPTION_INVALID_NEGATIVE_NUMBER, input, inputName));
        }
        return exceptions;
    }

    private static void addVerifyProtocol(@NotNull List<String> exceptions, @Nullable final String input) {
        String[] protocols = {"local", "scp", "sftp", "smb3"};
        if (!Arrays.asList(protocols).contains(input.toLowerCase())) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROTOCOL, input));
        }
    }

    private static List<String> addVerifyCopyAction(@NotNull List<String> exceptions, @Nullable final String input) {
        String[] copyActions = {"to", "from"};
        if (!Arrays.asList(copyActions).contains(input.toLowerCase())) {
            exceptions.add(String.format(EXCEPTION_INVALID_COPY_ACTION, input));
        }
        return exceptions;
    }

    private static void addVerifyKnownHostsPolicies(@NotNull List<String> exceptions, @Nullable final String input) {
        String[] protocols = {"allow", "strict", "add"};
        if (!Arrays.asList(protocols).contains(input.toLowerCase())) {
            exceptions.add(String.format(EXCEPTION_INVALID_KNOWN_HOSTS_POLICIES, input));
        }
    }

    public static List<String> verifyInputsSFTPCommand(String remotePath, String newRemotePath, String mode, String host, String port,
                                                       String username, String password, String proxyPort,
                                                       String characterSet, String closeSession, String connectionTimeout,
                                                       String executionTimeout) {
        final List<String> exceptions = verifyCommonSFTPInputs(host,port, username, password, proxyPort,
                characterSet,closeSession, connectionTimeout, executionTimeout);

        addVerifyNotNullOrEmpty(exceptions, remotePath, Inputs.SFTPInputs.REMOTE_PATH);
//        addVerifyNotNullOrEmpty(exceptions, newRemotePath, Inputs.SFTPInputs.NEW_REMOTE_PATH);
//        addVerifyNotNullOrEmpty(exceptions, mode, Inputs.SFTPInputs.MODE);

        return exceptions;
    }

    public static List<String> verifyInputsSFTPDeleteFile(String remoteFile, String host, String port,
                                                       String username, String password, String proxyPort,
                                                       String characterSet, String closeSession, String connectionTimeout,
                                                       String executionTimeout){
        final List<String> exceptions = verifyCommonSFTPInputs(host,port, username, password, proxyPort,
                characterSet,closeSession, connectionTimeout, executionTimeout);
        return exceptions;
    }

    public static List<String> verifyInputsSFTPRename(String remoteFile,String newRemoteFile, String host, String port,
                                                          String username, String password, String proxyPort,
                                                          String characterSet, String closeSession, String connectionTimeout,
                                                          String executionTimeout){
        final List<String> exceptions = verifyCommonSFTPInputs(host,port, username, password, proxyPort,
                characterSet,closeSession, connectionTimeout, executionTimeout);

        addVerifyNotNullOrEmpty(exceptions, remoteFile, SFTPInputs.REMOTE_FILE);
        addVerifyNotNullOrEmpty(exceptions, newRemoteFile, SFTPInputs.NEW_REMOTE_FILE);

    public static List<String> verifyInputsSFTPDeleteFile(String remotePath, String host, String port,
                                                          String username, String password, String proxyPort,
                                                          String characterSet, String closeSession, String connectionTimeout,
                                                          String executionTimeout) {
        final List<String> exceptions = verifyCommonSFTPInputs(host, port, username, password, proxyPort,
                characterSet, closeSession, connectionTimeout, executionTimeout);

        addVerifyNotNullOrEmpty(exceptions, remotePath, SFTPInputs.REMOTE_FILE);
        return exceptions;
    }
}
