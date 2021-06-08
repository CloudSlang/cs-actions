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

import io.cloudslang.content.ldap.constants.Constants;
import io.cloudslang.content.ldap.constants.ExceptionMsgs;
import io.cloudslang.content.ldap.constants.InputNames;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputBuilderUtils {

    public static String buildHost(String hostname, boolean mandatory) throws Exception {
        if (isEmpty(hostname) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.HOST));
        }
        return hostname.trim();
    }

    public static String buildOU(String OU, boolean mandatory) throws Exception {
        if (isEmpty(OU) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.OU));
        }
        return OU.trim();
    }

    public static String buildUserDN(String userDN, boolean mandatory) throws Exception {
        if (isEmpty(userDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.USER_DN));
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
        if (!isValid(trustAllRoots)){
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_BOOLEAN, InputNames.TRUST_ALL_ROOTS));
        }
        return StringUtils.isEmpty(trustAllRoots) || Boolean.parseBoolean(trustAllRoots);
    }


    public static boolean buildUseSSL(String useSSL) throws Exception {
        if (!isValid(useSSL)){
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_BOOLEAN, InputNames.USE_SSL));
        }
        try {
            return Boolean.parseBoolean(useSSL);
        } catch (Exception e) {
            return false;
        }
    }


    public static String buildKeystore(String keystore) {
        return defaultIfEmpty(keystore, Constants.DEFAULT_JAVA_KEYSTORE);
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
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.ROOT_DN));
        }
        return rootDN;
    }

    public static String buildComputerDN(String computerDN, boolean mandatory) throws Exception {
        if (isEmpty(computerDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.COMPUTER_DN));
        }
        return computerDN;
    }

    public static String buildNewOUDN(String newOUDN, boolean mandatory) throws Exception {
        if (isEmpty(newOUDN) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.NEW_OUDN));
        }
        return newOUDN;
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

    public static boolean buildUseSSL(String input, boolean mandatory) throws Exception {

        if (isEmpty(input) && mandatory) {
            throw new Exception(String.format(ExceptionMsgs.REQUIRED_INPUT_NOT_SPECIFIED, InputNames.USE_SSL));
        }
        if (!isValid(input)){
            throw new Exception(String.format(ExceptionMsgs.EXCEPTION_INVALID_BOOLEAN, InputNames.USE_SSL));
        }
        try {
            return Boolean.parseBoolean(input.toString());
        } catch (Exception e) {
            return false;
        }
    }
}