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

import io.cloudslang.content.constants.InputNames;

public final class Inputs extends InputNames {

    public static class CommonInputs {

        public static final String USER_ID = "userId";
        public static final String USER_PRINCIPAL_NAME = "userPrincipalName";
        public static final String ACCOUNT_ENABLED = "accountEnabled";
        public static final String DISPLAY_NAME = "displayName";
        public static final String ON_PREMISES_IMMUTABLE_ID = "onPremisesImmutableId";
        public static final String MAIL_NICKNAME = "mailNickname";
        public static final String FORCE_CHANGE_PASSWORD = "forceChangePasswordNextSignIn";
        public static final String PASSWORD = "password";
        public static final String PASSWORD_PROFILE = "passwordProfile";
        public static final String BODY = "body";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String AUTH_TOKEN = "authToken";

        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String SOCKET_TIMEOUT = "socketTimeout";
        public static final String KEEP_ALIVE = "keepAlive";
        public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
        public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
    }

    public static class AuthorizationInputs {
        public static final String LOGIN_TYPE = "loginType";
        public static final String CLIENT_ID = "clientId";
        public static final String CLIENT_SECRET = "clientSecret";
        public static final String USERNAME = "username";
        public static final String LOGIN_AUTHORITY = "loginAuthority";
        public static final String SCOPE = "scope";
    }

    public static class UpdateUserInputs {
        public static final String UPDATED_USER_PRINCIPAL_NAME = "updatedUserPrincipalName";
    }

    public static class IsUserInGroup {
        public static final String SECURITY_ENABLED_GROUPS = "securityEnabledOnly";
    }


    public static class AssignUserLicenseInputs {
        public static final String ASSIGNED_LICENSES = "assignedLicenses";
    }

    public static class RemoveUserLicenseInputs {
        public static final String REMOVED_LICENSES = "removedLicenses";
    }

    public static class ChangeUserPasswordInputs {
        public static final String CURRENT_PASSWORD = "currentPassword";
        public static final String NEW_PASSWORD = "newPassword";
    }

}

