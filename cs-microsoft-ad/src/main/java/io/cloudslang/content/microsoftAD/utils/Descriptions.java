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

        public static final String SUCCESS_DESCRIPTION = "Request went successfully.";
        public static final String FAILURE_DESCRIPTION = "There was an error while trying to do the request.";
        public static final String STATUS_CODE_200_OK_DESC = "The HTTP status code for Azure API request, if successful, " +
                "this method returns 200 response code.";
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
                "\n" + "The principal name needs to comply with the format accepted by Active Directory. " +
                "The allowed characters are A-Z, a-z, 0-9 and ' . - _ ! # ^ ~ . The length must not exceed " +
                "113 characters in total, and there should be 64 characters or less before @ .\n" +
                "Example: someuser@contoso.com";
        public static final String FORCE_CHANGE_PASSWORD_DESC = "In case the value for the input is true, the user " +
                "must change the password on the next login. \nDefault value: false. \nNOTE: For Azure B2C " +
                "tenants, set to false and instead use custom policies and user flows to force password reset at " +
                "first sign in.";
        public static final String PASSWORD_DESC = "The password for the user. This property is required when a user is " +
                "created.\n The password needs to comply with the format accepted by Active Directory. " +
                "The allowed characters are A-Z, a-z, 0-9, blank space and @ # $ % ^ & * - _ ! + = [ ] { } | \\ : ' , . ? /" +
                " ` ~ \" ( ) ; < >. The length must be between 8 and 256 characters in total, and at least three of the" +
                " following conditions must be met: lowercase characters, uppercase characters, numbers, symbols." +
                " The password must also satisfy minimum requirements as specified by the user’s passwordPolicies " +
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


    }

    public static class UpdateUser {
        public static final String UPDATE_USER_NAME = "Update User";
        public static final String UPDATE_USER_DESC = "Updates user's properties.";
        public static final String UPDATED_USER_PRINCIPAL_NAME_DESC = "The new User Principal Name." +
                "The principal name needs to comply with the format accepted by Active Directory. " +
                "The allowed characters are A-Z, a-z, 0-9 and ' . - _ ! # ^ ~ . The length must not " +
                "exceed 113 characters in total, and there should be 64 characters or less before @ .\n" +
                "Example: someuser@contoso.com";
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
        public static final String UPDATE_USER_FAILURE_RETURN_RESULT = "There was an error while updating the user's properties!";

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

    public static class ResetUserPassword {

        //Descriptions
        public static final String RESET_USER_PASSWORD_DESC = "Reset the password for an Active Directory user.";
        public static final String RESET_USER_PASSWORD_NAME = "Reset User Password";

        //Inputs
        public static final String FORCE_CHANGE_PASSWORD_DESC = "The new password for the user. The password must satisfy " +
                "minimum requirements as specified by the user’s passwordPolicies " +
                "property. By default, a strong password is required.";

        //Results
        public static final String RESET_USER_PASSWORD_FAILURE_DESC = "There was an error while trying to reset the user password.";
        public static final String RESET_USER_PASSWORD_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content " +
                "response code. It does not return anything in the response body.";
        public static final String RESET_USER_PASSWORD_SUCCESS_RETURN_RESULT_DESC = "The user's password was successfully updated.";
    }

    public static class IsUserInGroup {

        //Descriptions
        public static final String IS_USER_IN_GROUP_DESC = "Return all the groups that the user is a member of.";
        public static final String IS_USER_IN_GROUP_NAME = "Is User In Group";

        public static final String IS_USER_ID_DESC = "The ID of the user to perform the action on.";

        public static final String IS_USER_IN_GROUP_SECURITY_GROUPS = "True if only security groups that the user is a " +
                "member of should be returned, false to specify that all groups should be returned.";

        //Results
        public static final String NO_GROUP_MEMBER = "The user does not belong to any group.";
        public static final String IS_USER_IN_GROUP_RETURN_RESULT_DESC = "If successful this method returns the IDs of " +
                "the groups that the user is a member of. If the user does not belong to any" +
                "group, a suggestive message will be displayed.";
    }

    public static class AssignUserLicense {

        //Descriptions
        public static final String ASSIGN_USER_LICENSE_DESC = "Add subscriptions for the user. You can also enable specific plans associated with a subscription.";
        public static final String ASSIGN_USER_LICENSE_NAME = "Assign User License";

        //Inputs
        public static final String ASSIGNED_LICENSES_DESC = "A collection of assignedLicense objects that specify " +
                "the licenses to add. You can disable plans associated with a license by setting the disabledPlans " +
                "property on an assignedLicense object.";
        public static final String INVALID_JSON_INPUT_DESC = "The value provided for the assigned licenses input is " +
                "an invalid JSON.";

        //Results
        public static final String ASSIGN_USER_LICENSE_FAILURE_DESC = "There was an error while trying to assign license.";
        public static final String ASSIGN_USER_LICENSE_RETURN_RESULT_DESC = "If successful, this method returns 200 " +
                "response code and a user object in the response body.";
        public static final String ASSIGN_USER_LICENSE_SUCCESS_RETURN_RESULT_DESC = "The license was successfully assigned.";

    }

    public static class ChangeUserPassword {

        //Descriptions
        public static final String CHANGE_USER_PASSWORD_NAME = "Change User Password";
        public static final String CHANGE_USER_PASSWORD_DESC = "Enables the user to update their own password. Any user " +
                "can update their password without belonging to any administrator role.";

        //Inputs
        public static final String CURRENT_PASSWORD_DESC = "The user's current password.";
        public static final String NEW_PASSWORD_DESC = "The new password. The password needs to comply with the " +
                "format accepted by Active Directory. The allowed characters are A-Z, a-z, 0-9, blank " +
                "space and @ # $ % ^ & * - _ ! + = [ ] { } | \\ : ' , . ? / ` ~ \" ( ) ; < >. The length " +
                "must be between 8 and 256 characters in total, and at least three of the following conditions " +
                "must be met: lowercase characters, uppercase characters, numbers, symbols.";

        //Results
        public static final String CHANGE_USER_PASSWORD_FAILURE_DESC = "There was an error while trying to change the user's password.";
        public static final String CHANGE_USER_PASSWORD_RETURN_RESULT_DESC = "If successful, this method returns 204 No Content " +
                "response code. It does not return anything in the response body.";
        public static final String CHANGE_USER_PASSWORD_SUCCESS_RETURN_RESULT_DESC = "The user's password was successfully changed.";
    }

    public static class RemoveUserLicense {

        //Descriptions
        public static final String REMOVE_USER_LICENSE_DESC = "Remove subscriptions for the user.";
        public static final String REMOVE_USER_LICENSE_NAME = "Remove User License";

        //Inputs
        public static final String REMOVED_LICENSES_DESC = "A comma separated list of skuIds that need to be removed.";
        public static final String INVALID_STRING_ARRAY_INPUT_DESC = "The value provided for the removed licenses input is " +
                "an invalid string array.";

        //Results
        public static final String REMOVE_USER_LICENSE_FAILURE_DESC = "There was an error while trying to remove license.";
        public static final String REMOVE_USER_LICENSE_RETURN_RESULT_DESC = "If successful, this method returns 200 " +
                "response code and a user object in the response body.";
        public static final String REMOVE_USER_LICENSE_SUCCESS_RETURN_RESULT_DESC = "The license was successfully removed.";


    }

    public static class GetUserLicenseDetails {

        public static final String GET_USER_LICENSE_DETAILS_NAME = "Get User License Details";
        public static final String GET_USER_LICENSE_DETAILS_DESC = "Retrieve a list of license details objects for a given user.";

        public static final String GET_USER_LICENSE_DETAILS_RETURN_RESULT = "A list of license details objects for a given user.";

        public static final String QUERY_PARAMS_DESCRIPTION = "This input can be used to filter the response using query " +
                "parameters in the form of a comma delimited list. \n" +
                "For a complete list of available query parameters please check the Microsoft Graph documentation. \n" +
                "Example: id,skuId,skuPartNumber";
    }


    public static class ListAvailableSkus {

        //Descriptions
        public static final String LIST_AVAILABLE_SKUS_DESC = "Get the list of commercial subscriptions that " +
                "an organization has acquired.";
        public static final String LIST_AVAILABLE_SKUS_NAME = "List Available Skus";

        //Output descriptions
        public static final String AVAILABLE_SKUIDS_LIST_DESC = "Get a comma separated list of skuIds. " +
                "If the query parameter does not select skuIds, this " +
                "field will be empty";

        //Results
        public static final String LIST_AVAILABLE_SKUS_SUCCESS_DESC = "Request went successfully.";
        public static final String LIST_AVAILABLE_SKUS_FAILURE_DESC = "There was an error while trying to do the request.";
        public static final String LIST_AVAILABLE_SKUS_RETURN_RESULT_DESC = "If successful, this operation returns the " +
                "complete API response.";
    }

}