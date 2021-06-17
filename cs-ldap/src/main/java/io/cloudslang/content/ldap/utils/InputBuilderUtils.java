/*
 * (c) Copyright 2020 Micro Focus
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
package io.cloudslang.content.ldap.utils;

import io.cloudslang.content.ldap.constants.CipherSuites;
import io.cloudslang.content.ldap.constants.ExceptionMsgs;
import io.cloudslang.content.ldap.constants.InputNames;
import io.cloudslang.content.ldap.constants.TlsVersions;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputBuilderUtils {

    public static String buildHost(String hostname, boolean mandatory) throws Exception {
        if (isEmpty(hostname) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.HOST));
        }
        return hostname.trim();
    }

    public static String buildDistinguishedName(String distinguishedName, boolean mandatory) throws Exception {
        if (isEmpty(distinguishedName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.DISTINGUISHED_NAME));
        }
        return distinguishedName.trim();
    }

    public static String buildUserDN(String userDN, boolean mandatory) throws Exception {
        if (isEmpty(userDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.USER_DISTINGUISHED_NAME));
        }
        return userDN.trim();
    }

    public static String buildComputerCommonName(String computerCommonName, boolean mandatory) throws Exception {
        if (isEmpty(computerCommonName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.COMPUTER_COMMON_NAME));
        }
        return computerCommonName.trim();

    }

    public static String buildSAMAccountName(String sAMAccountName, String computerCommonName) {
        if (isEmpty(sAMAccountName)) {
            sAMAccountName = computerCommonName;
        }
        return sAMAccountName.trim();
    }


    public static String buildUsername(String username) throws Exception {
        if (isEmpty(username)) {
            throw new Exception(String.format(ExceptionMsgs.INPUT_NOT_SPECIFIED, InputNames.USERNAME));
        }
        return username;
    }

    public static String buildPassword(String password) {
        return (password == null) ? StringUtils.EMPTY : password;
    }

    public static boolean buildTrustAllRoots(String trustAllRoots) throws Exception {
        if (!isValid(trustAllRoots)) {
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_BOOLEAN, InputNames.TRUST_ALL_ROOTS));
        }
        return StringUtils.isEmpty(trustAllRoots) || Boolean.parseBoolean(trustAllRoots);
    }


    public static String buildProtocol(String protocol) throws Exception {
        if (!(Arrays.asList("http", "https").contains(protocol.toLowerCase()))) {
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_PROTOCOL, InputNames.PROTOCOL));
        }
        return protocol;
    }

    public static String buildProtocol(String protocol, Boolean mandatory) throws Exception {
        if (isEmpty(protocol) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.PROTOCOL));
        }
        if (!(Arrays.asList("http", "https").contains(protocol.toLowerCase()))) {
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_PROTOCOL, InputNames.PROTOCOL));
        }
        return protocol;
    }


    public static boolean buildEscapeChars(String escapeChars) {
        try {
            return Boolean.parseBoolean(escapeChars);
        } catch (Exception e) {
            return false;
        }
    }

    public static String buildRootDN(String rootDN, boolean mandatory) throws Exception {
        if (isEmpty(rootDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.ROOT_DISTINGUISHED_NAME));
        }
        return rootDN;
    }

    public static String buildComputerDN(String computerDN, boolean mandatory) throws Exception {
        if (isEmpty(computerDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.COMPUTER_DISTINGUISHED_NAME));
        }
        return computerDN;
    }

    public static String buildNewOUDN(String ouCommonName, boolean mandatory) throws Exception {
        if (isEmpty(ouCommonName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.OU_COMMON_NAME));
        }
        return ouCommonName;
    }

    public static String buildUserCommonName(String userCommonName) throws Exception {
        if (isEmpty(userCommonName)) {
            throw new Exception(String.format(ExceptionMsgs.INPUT_NOT_SPECIFIED, InputNames.USER_COMMON_NAME));
        }
        return userCommonName.trim();
    }

    public static String buildUserPassword(String userPassword) throws Exception {
        if (isEmpty(userPassword)) {
            throw new Exception(String.format(ExceptionMsgs.INPUT_NOT_SPECIFIED, InputNames.USER_PASSWORD));
        }
        return userPassword.trim();
    }

    public static String buildGroupCommonName(String groupCommonName, boolean mandatory) throws Exception {
        if (isEmpty(groupCommonName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.GROUP_COMMON_NAME));
        }
        return groupCommonName.trim();
    }

    public static String buildGroupDN(String groupDistinguishedName, boolean mandatory) throws Exception {
        if (isEmpty(groupDistinguishedName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.GROUP_DISTINGUISHED_NAME));
        }
        return groupDistinguishedName.trim();
    }

    public static String buildSAMAccountNameRequired(String sAMAccountName, boolean mandatory) throws Exception {
        if (isEmpty(sAMAccountName) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.SAM_ACCOUNT_NAME));
        }
        return sAMAccountName.trim();
    }

    public static String buildUserCommonName(String input, boolean mandatory) throws Exception {

        if (isEmpty(input) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.COMPUTER_COMMON_NAME));
        }
        return input.trim();
    }

    public static String buildUsername(String input, boolean mandatory) throws Exception {

        if (isEmpty(input) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.USERNAME));
        }
        return input.trim();
    }

    public static String buildPassword(String input, boolean mandatory) throws Exception {

        if (isEmpty(input) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.PASSWORD));
        }
        return input.trim();
    }

    public static String buildTlsVersions(String tlsVersionInput) throws Exception {

        String[] tlsVersionsArray = tlsVersionInput.replaceAll("\\s+", StringUtils.EMPTY).split(",");
        List<String> tlsVersions = new ArrayList<>();
        for (String tlsVersion : tlsVersionsArray) {
            if (!TlsVersions.validate(tlsVersion)) {
                throw new IllegalArgumentException("Illegal value of input " + InputNames.TLS_VERSION);
            }
            tlsVersions.add(tlsVersion);
        }
        return tlsVersionInput;
    }


    public static List<String> buildAllowedCiphers(String allowedCiphersInput) throws Exception {
        if (StringUtils.isEmpty(allowedCiphersInput)) {
            return Collections.emptyList();
        }

        String[] cipherSuites = allowedCiphersInput.replaceAll("\\s+", StringUtils.EMPTY).split(",");
        List<String> allowedCiphers = Arrays.asList(cipherSuites);

        for (String cipher : allowedCiphers) {
            if (!CipherSuites.validate(cipher)) {
                throw new IllegalArgumentException("Illegal value of input " + InputNames.ALLOWED_CIPHERS);
            }
        }

        return allowedCiphers;
    }

    public static String addVerifyPort(String inputName) throws Exception {
        if (!isValidIpPort(inputName)) {
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_PORT, InputNames.PROXY_PORT));
        }
        return inputName;
    }

}