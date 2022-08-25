/*
 * (c) Copyright 2022 Micro Focus, L.P.
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

package io.cloudslang.content.oauth.utils;

public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the OAuth 2.0 service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the  OAuth 2.0 service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
    }

    public static class GetAuthorizationToken {
        public static final String LOGIN_TYPE_DESC = "Login method according to application type\n" +
                "Valid values: 'API', 'Native'\n" +
                "Default: 'API'";
        public static final String CLIENT_ID_DESC = "Service Client ID";
        public static final String CLIENT_SECRET_DESC = "Service Client Secret";
        public static final String USERNAME_DESC = "Username used for authentication";
        public static final String PASSWORD_DESC = "Password used for authentication";
        public static final String LOGIN_AUTHORITY_DESC = "The authority URL. Usually, the format for this input is:\n" +
                "'https://login.windows.net/TENANT_NAME/oauth2/token' where TENANT_NAME is your application\n" +
                "tenant.";
        public static final String RESOURCES_DESC = "The resource URL\n" +
                "Default: 'https://graph.microsoft.com'";
        public static final String SCOPE_DESC = "The scope URL. The scope consists of a series of resource permissions separated " +
                "by a comma (,), i.e.: 'https://graph.microsoft.com/User.Read, https://graph.microsoft.com/Sites.Read'. " +
                "The 'https://graph.microsoft.com/.default' scope means that the user consent to all of the configured permissions " +
                "present on the specific Azure AD Application. For the 'API' loginType, '/.default' scope should be used. \n" +
                "Default: 'https://graph.microsoft.com/.default'";

        public static final String RETURN_RESULT_DESC = "This will contain the token used for OAuth 2.0 authentication.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String AUTH_TOKEN_DESC = "The authentication token.";
        public static final String AUTH_TOKEN_TYPE_DESC = "The authentication token type.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the token.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";
    }
}