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

import io.cloudslang.content.ldap.constants.ExceptionMsgs;
import io.cloudslang.content.ldap.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputBuilderUtils {

    public static String buildHost(String hostname, boolean mandatory) throws Exception {
        if (isEmpty(hostname) && mandatory) {
            throw new Exception(ExceptionMsgs.HOST_NOT_SPECIFIED);
        }
        return hostname.trim();
    }

    public static String buildOU(String OU, boolean mandatory) throws Exception {
        if (isEmpty(OU) && mandatory) {
            throw new Exception(ExceptionMsgs.OU_NOT_SPECIFIED);
        }
        return OU.trim();
    }

    public static String buildComputerCommonName(String computerCommonName, boolean mandatory) throws Exception {
        if (isEmpty(computerCommonName) && mandatory) {
            throw new Exception(ExceptionMsgs.CN_NOT_SPECIFIED);
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
            throw new Exception(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
        }
        return username;
    }

    public static String buildPassword(String password) {
        return (password == null) ? StringUtils.EMPTY : password;
    }

    public static boolean buildTrustAllRoots(String trustAllRoots) {
        return StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);
    }


    public static boolean buildUseSSL(String useSSL) {
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
            throw new Exception(ExceptionMsgs.ROOTDN_NOT_SPECIFIED);
        }
        return rootDN;
    }

    public static String buildComputerDN(String computerDN, boolean mandatory) throws Exception {
        if (isEmpty(computerDN) && mandatory) {
            throw new Exception(ExceptionMsgs.COMPDN_NOT_SPECIFIED);
        }
        return computerDN;
    }

    public static String buildNewOUDN(String newOUDN, boolean mandatory) throws Exception {
        if (isEmpty(newOUDN) && mandatory) {
            throw new Exception(ExceptionMsgs.NEWOUDN_NOT_SPECIFIED);
        }
        return newOUDN;
    }

}