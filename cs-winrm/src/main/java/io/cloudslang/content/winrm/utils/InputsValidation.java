/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.winrm.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static io.cloudslang.content.winrm.utils.Constants.*;
import static io.cloudslang.content.winrm.utils.Inputs.WinRMInputs.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class InputsValidation {

    @NotNull
    public static List<String> verifyWinRMInputs(@Nullable final String proxyPort,
                                                 @Nullable final String trust_all_roots,
                                                 @Nullable final String operationTimeout,
                                                 @Nullable final String requestNewKerberosToken,
                                                 @NotNull final String authType,
                                                 @NotNull final String x509HostnameVerifier,
                                                 @NotNull final String trustKeystore,
                                                 @NotNull final String keystore,
                                                 @NotNull final String port,
                                                 @NotNull final String tlsVersion,
                                                 @NotNull final String protocol) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyProxy(exceptionMessages, proxyPort, PROXY_PORT);
        addVerifyBoolean(exceptionMessages, trust_all_roots, TRUST_ALL_ROOTS);
        addVerifyBoolean(exceptionMessages, requestNewKerberosToken, REQUEST_NEW_KERBEROS_TICKET);
        addVerifyNumber(exceptionMessages, operationTimeout, OPERATION_TIMEOUT);
        addVerifyAuthType(exceptionMessages, authType, AUTH_TYPE);
        addVerifyx509HostnameVerifier(exceptionMessages, x509HostnameVerifier, X509_HOSTNAME_VERIFIER);
        addVerifyFile(exceptionMessages, trustKeystore, TRUST_KEYSTORE);
        addVerifyFile(exceptionMessages, keystore, KEYSTORE);
        addVerifyPort(exceptionMessages, port, PORT);
        addVerifyTlsVersion(exceptionMessages, tlsVersion, TLS_VERSION);
        addVerifyProtocol(exceptionMessages, protocol, PROTOCOL);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (!isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PATH, filePath, inputName));
        }
        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }

    @NotNull
    private static List<String> addVerifyProxy(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PROXY, input, PROXY_PORT));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
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
    private static List<String> addVerifyAuthType(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> authTypes = new ArrayList<>();
        authTypes.add("basic");
        authTypes.add("ntlm");
        authTypes.add("kerberos");
        if (!authTypes.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_AUTH_TYPE, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyx509HostnameVerifier(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> x509HostnameVerifiers = new ArrayList<>();
        x509HostnameVerifiers.add("strict");
        x509HostnameVerifiers.add("allow_all");
        x509HostnameVerifiers.add("browser_compatible");
        if (!x509HostnameVerifiers.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_HOSTNAME_VERIFIER, input, inputName));
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyPort(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (!isValidIpPort(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_PORT, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyTlsVersion(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> tlsVersions = new ArrayList<>();
        tlsVersions.add("sslv3");
        tlsVersions.add("tlsv1");
        tlsVersions.add("tlsv1.1");
        tlsVersions.add("tlsv1.2");
        tlsVersions.add("tlsv1.3");

        if (!tlsVersions.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_TLS_VERSION, input, inputName));
        return exceptions;
    }
    @NotNull
    private static List<String> addVerifyProtocol(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        List<String> protocols = new ArrayList<>();
        protocols.add("http");
        protocols.add("https");

        if (!protocols.contains(input.toLowerCase()))
            exceptions.add(String.format(EXCEPTION_INVALID_PROTOCOL, input, inputName));
        return exceptions;
    }


}
