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

public class Descriptions {
    public static class Common {

        public static final String PROXY_HOST_DESC = "Proxy server used to access the Azure Active Directory service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Azure Active Directory service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";

        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS).";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";

        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total.";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis.";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it.";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";

        public static final String EXCEPTION_INVALID_HOSTNAME_VERIFIER = "%s for %s input is not a valid x509HostnameVerifier value. The valid values are: 'strict','browser_compatible','allow_all'.";
        public static final String EXCEPTION_INVALID_TLS_VERSION = "%s for %s input is not a valid tlsVersion value. The valid values are: 'TLSv1','TLSv1.1','TLSv1.2','TLSv1.3'.";


        //Inputs
        public static final String AUTH_TOKEN_DESC = "Token used to authenticate to Azure Active Directory.";
        public static final String USER_PRINCIPAL_NAME_DESC = "The user principal name. This input is mutually exclusive " +
                "with the userId input. \n" +
                "Example: someuser@contoso.com";
        public static final String USER_ID_DESC = "The ID of the user to perform the action on. This input is mutually " +
                "exclusive with the userPrincipalName input.";

        //Outputs
        public static final String RETURN_CODE_DESC = "0 if success, -1 if failure.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Azure API request, successful if between 200 and 300.";
        public static final String EXCEPTION_DESC = "The error message in case of failure.";
    }

    public static class GetAuthorizationToken {

        //Description
        public static final String GET_AUTHORIZATION_TOKEN_NAME = "Get Authorization Token";
        public static final String GET_THE_AUTHORIZATION_TOKEN_DESC = "Get the authorization token for Azure Active Directory.";

        public static final String LOGIN_TYPE_DESC = "Login method according to application type.\n" +
                "Valid values: 'API', 'Native'\n" +
                "Default: 'API'";
        public static final String CLIENT_ID_DESC = "Service Client ID.";
        public static final String CLIENT_SECRET_DESC = "Service Client Secret.";
        public static final String USERNAME_DESC = "Azure Active Directory username.";
        public static final String PASSWORD_DESC = "Azure Active Directory password.";
        public static final String LOGIN_AUTHORITY_DESC = "The authority URL. Usually, the format for this input is:\n" +
                "'https://login.windows.net/TENANT_NAME/oauth2/v2.0/token' where TENANT_NAME is your application\n" +
                "tenant.";
        public static final String SCOPE_DESC = "The scope URL. The scope consists of a series of resource permissions separated " +
                "by a comma (,), i.e.: 'https://graph.microsoft.com/User.Read, https://graph.microsoft.com/Sites.Read'. " +
                "The 'https://graph.microsoft.com/.default' scope means that the user consent to all of the configured permissions " +
                "present on the specific Azure AD Application. For the 'API' loginType, '/.default' scope should be used. \n" +
                "Default: 'https://graph.microsoft.com/.default'";

        public static final String RETURN_RESULT_DESC = "The authorization token for Azure Active Directory.";
        public static final String AUTH_TOKEN_DESC = "Generated authentication token.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the " +
                "token.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";
    }

    public static class CreateUser {

        //Description
        public static final String DESC = "Create a new user. The request body contains the user to create. At a " +
                "minimum, you must specify the required properties for the user. You can optionally specify any " +
                "other writable properties.";
        public static final String NAME = "Create User";

        //Inputs
        public static final String ACCOUNT_ENABLED_DESC = "Must be true if the user wants to enable the account. " +
                "\nDefault value: true.";
        public static final String DISPLAY_NAME_DESC = "The name to display in the address book for the user.";
        public static final String ON_PREMISES_IMMUTABLE_ID_DESC = "Only needs to be specified when creating a new " +
                "user account if you are using a federated domain for the user's userPrincipalName (UPN) property.";
        public static final String MAIL_NICKNAME_DESC = "The mail alias for the user.";
        public static final String USER_PRINCIPAL_NAME_DESC = "The user principal name." +
                "\nExample: someuser@contoso.com";
        public static final String FORCE_CHANGE_PASSWORD_DESC = "In case the value for the input is true, the user " +
                "must change the password on the next login. \nDefault value: false. \nNOTE: For Azure B2C " +
                "tenants, set to false and instead use custom policies and user flows to force password reset at " +
                "first sign in.";
        public static final String PASSWORD_DESC = "The password for the user. This property is required when a user is " +
                "created. The password must satisfy minimum requirements as specified by the user’s passwordPolicies " +
                "property. By default, a strong password is required.";
        public static final String BODY_DESC = "Full json body if the user wants to set additional properties. " +
                "All the other inputs are ignored if the body is given.";

        //Outputs
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response.";
        public static final String USER_ID_DESC = "The ID of the newly created user.";

        //Results
        public static final String SUCCESS_DESC = "User created successfully.";
        public static final String FAILURE_DESC = "Failed to create user.";
    }


    public static class DeleteUser {

        //Descriptions
        public static final String DELETE_USER_DESC = "Delete a user from Active Directory. When deleted, user resources are moved to a " +
                "temporary container and can be restored within 30 days. After that time, they are permanently deleted.";
        public static final String DELETE_USER_NAME = "Delete User";

        //Results
        public static final String DELETE_USER_FAILURE_DESC = "There was an error while trying to delete user.";
        public static final String DELETE_USER_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content " +
                "response code. It does not return anything in the response body.";
        public static final String SUCCESS_RETURN_RESULT_DESC = "The user was successfully deleted.";


    }

    public static class IsUserEnabled {

        //Descriptions
        public static final String IS_USER_ENABLED_DESC = "Checks if a user is enabled.";
        public static final String IS_USER_ENABLED_NAME = "Is User Enabled";

        //Outputs
        public static final String ACCOUNT_ENABLED_DESC = "True if the account is enabled, false otherwise.";

        //Results
        public static final String IS_USER_ENABLED_SUCCESS_DESC = "Request went successfully.";
        public static final String IS_USER_ENABLED_FAILURE_DESC = "There was an error while trying to do the request.";
        public static final String IS_USER_ENABLED_RETURN_RESULT_DESC = "If successful, this method returns 200 " +
                "response code.";
    }

    public static class UpdateUser {
        public static final String UPDATE_USER_NAME = "Update User";
        public static final String UPDATE_USER_DESC = "Updates user's properties.";
        public static final String UPDATED_USER_PRINCIPAL_NAME_DESC = "The new User Principal Name.";
        public static final String UPDATED_ON_PREMISES_IMMUTABLE_ID_DESC = "This property is used to associate an on-premises " +
                "Active Directory user account to their Azure AD user object. This property must be specified when " +
                "creating a new user account in the Graph if you are using a federated domain for the user’s userPrincipalName " +
                "(UPN) property. Important: The $ and _ characters cannot be used when specifying this property.";
        public static final String UPDATED_MAIL_NICKNAME_DESC = "The mail alias for the user. This property must be " +
                "specified when the user is created.";
        public static final String UPDATED_ACCOUNT_ENABLED = "This property must be set to 'true' if the account is enabled; " +
                "otherwise, 'false'. This property is required when a user is created." +
                "Default: true.";
        public static final String UPDATED_DISPLAY_NAME = "The name displayed in the address book for the user. This " +
                "property is required when a user is created and it cannot be cleared during updates.";
        public static final String UPDATE_USER_RETURN_RESULT = "The user's properties were updated successfully!";

    }
    public static class EnableUser {

        //Descriptions
        public static final String ENABLE_USER_DESC = "Enable a user from Active Directory.";
        public static final String ENABLE_USER_NAME = "Enable User";

        //Results
        public static final String ENABLE_USER_FAILURE_DESC = "There was an error while trying to enable user.";
        public static final String ENABLE_USER_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content " +
                "response code. It does not return anything in the response body.";
        public static final String ENABLE_USER_SUCCESS_RETURN_RESULT_DESC = "The user was successfully enabled.";


    }

    public static class DisableUser {

        //Descriptions
        public static final String DISABLE_USER_DESC = "Disable a user from Active Directory.";
        public static final String DISABLE_USER_NAME = "Disable User";

        //Results
        public static final String DISABLE_USER_FAILURE_DESC = "There was an error while trying to disable user.";
        public static final String DISABLE_USER_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content " +
                "response code. It does not return anything in the response body.";
        public static final String DISABLE_USER_SUCCESS_RETURN_RESULT_DESC = "The user was successfully disabled.";


    }

}