/*
 * (c) Copyright 2022 Micro Focus
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
/*
 * (c) Copyright 2022 Micro Focus
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
package io.cloudslang.content.cyberark.utils;

public final class Constants {

    public static final class CommonConstants {

        //Inputs
        public static final String HOST = "hostname";
        public static final String PROTOCOL = "protocol";
        public static final String AUTH_TOKEN = "authToken";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String TLS_VERSION = "tlsVersion";
        public static final String ALLOWED_CIPHERS = "allowedCiphers";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String KEEP_ALIVE = "keepAlive";
        public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
        public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";

        public static final String HOST_DESCRIPTION = "The hostname or IP address of the host.";
        public static final String PROTOCOL_DESCRIPTION = "Specifies what protocol is used to execute commands on the remote host.\n" +
                "Valid values: http, https\n " +
                "Default value: https";
        public static final String AUTH_TOKEN_DESCRIPTION = "Token used to authenticate to the cyberark environment.";
        public static final String PROXY_HOST_DESCRIPTION = "The proxy server used to access the host.";
        public static final String PROXY_PORT_DESCRIPTION = "The proxy server port.\n Default value:8080";
        public static final String PROXY_USERNAME_DESCRIPTION = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESCRIPTION = "The proxy server password associated with the proxyUsername input value.";
        public static final String TLS_VERSION_DESCRIPTION = "The version of TLS to use. The value of this input will be ignored if 'protocol'" +
                "is set to 'HTTP'. This capability is provided “as is”, please see product documentation for further information." +
                "Valid values: TLSv1, TLSv1.1, TLSv1.2. \n" +
                "Default value: TLSv1.2.  \n";
        public static final String ALLOWED_CIPHERS_DESCRIPTION = "A list of ciphers to use. The value of this input will be ignored " +
                "if 'tlsVersion' does " +
                "not contain 'TLSv1.2'. This capability is provided “as is”, please see product documentation for further security considerations." +
                "In order to connect successfully to the target host, it should accept at least one of the following ciphers. If this is not the case, it is " +
                "the user's responsibility to configure the host accordingly or to update the list of allowed ciphers. \n" +
                "Default value: TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, " +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, " +
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256, " +
                "TLS_RSA_WITH_AES_128_CBC_SHA256.";
        public static final String TRUST_ALL_ROOTS_DESCRIPTION = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_HOSTNAME_VERIFIER_DESCRIPTION = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESCRIPTION = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. \n " +
                "Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESCRIPTION = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESCRIPTION = "The pathname of the Java KeyStore file. You only need this if " +
                "the server requires client authentication. If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String KEYSTORE_PASSWORD_DESCRIPTION = "The password associated with the KeyStore file. If " +
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.";
        public static final String CONNECT_TIMEOUT_DESCRIPTION = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String EXECUTION_TIMEOUT_DESCRIPTION = "The amount of time (in seconds) to allow the client to complete the execution " +
                "of an API call. A value of '0' disables this feature. \n" +
                "Default: 60  \n";
        public static final String KEEP_ALIVE_DESCRIPTION = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                "execution it will close it.";
        public static final String CONNECTIONS_MAX_PER_ROUTE_DESCRIPTION = "The maximum limit of connections on a per route basis.";
        public static final String CONNECTIONS_MAX_TOTAL_DESCRIPTION = "The maximum limit of connections in total.";

        //Outputs
        public static final String RETURN_RESULT = "returnResult";
        public static final String STATUS_CODE = "statusCode";
        public static final String RETURN_CODE = "returnCode";
        public static final String EXCEPTION = "exception";

        public static final String RETURN_RESULT_DESCRIPTION = "returnResult";
        public static final String STATUS_CODE_DESCRIPTION = "statusCode";
        public static final String RETURN_CODE_DESCRIPTION = "returnCode";
        public static final String EXCEPTION_DESCRIPTION = "exception";
    }

    public static final class GetAuthTokenConstants {

        public static final String GET_AUTH_TOKEN = "Get Auth Token";

        public static final String GET_AUTH_TOKEN_DESCRIPTION = "This method authenticates a user to Privilege Cloud and returns a " +
                "token that can be used in subsequent web services calls. In addition, this method enables you to set a " +
                "new password.";

        //Inputs
        public static final String PASSWORD = "password";
        public static final String NEW_PASSWORD = "newPassword";
        public static final String CONCURRENT_SESSION = "concurrentSession";

        public static final String USERNAME_DESCRIPTION = "The name of the user who is logging in to Privilege Cloud.";
        public static final String PASSWORD_DESCRIPTION = "The password used by the user to log in to Privilege Cloud.";
        public static final String NEW_PASSWORD_DESCRIPTION = "Set this parameter with a new password to change the user's password.";
        public static final String CONCURRENT_SESSION_DESCRIPTION = "Set this parameter to True to enable the user to open " +
                "multiple connection sessions simultaneously.\n" +
                "Up to 300 concurrent sessions are supported.\n" +
                "Valid values: True/False\n" +
                "Default: False";
    }

    public static final class GetPasswordValueConstants {

        public static final String GET_PASSWORD_VALUE = "Get Password Value";

        public static final String GET_PASSWORD_VALUE_DESCRIPTION = "This service enables applications to retrieve secrets from the Central Credential Provider.";

        //Inputs
        public static final String APP_ID = "appId";
        public static final String QUERY = "query";
        public static final String QUERY_FORMAT = "queryFormat";

        public static final String APP_ID_DESCRIPTION = "Specifies the unique ID of the application issuing the password request.";
        public static final String QUERY_DESCRIPTION = "Defines a free query using account properties, including Safe, " +
                "folder, and object. When this method is specified, all other search criteria (Safe/Folder/" +
                "Object/UserName/Address/PolicyID/Database) are ignored and only the account properties that are " +
                "specified in the query are passed to the Central Credential Provider in the password request.";
        public static final String QUERY_FORMAT_DESCRIPTION = "Defines the query format, which can optionally use regular expressions. Possible values are:\n" +
                "Valid values: Exact/Regexp\n" +
                "Default: Exact";

        //Outputs
        public static final String PASSWORD_VALUE = "passwordValue";
    }

    public static final class GetAccountsConstants {

        public static final String GET_ACCOUNTS = "Get Accounts";

        public static final String GET_ACCOUNTS_DESCRIPTION = "This method returns a list of all the accounts in Privilege Cloud. " +
                "The user who runs this web service requires List Accounts permissions in the Safe.";

        //Inputs
        public static final String SEARCH = "search";
        public static final String SEARCH_TYPE = "searchType";
        public static final String SORT = "sort";
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
        public static final String FILTER = "filter";
        public static final String SAVED_FILTER = "savedFilter";

        public static final String SEARCH_DESCRIPTION = "A list of keywords to search for in accounts, separated by a space.";
        public static final String SEARCH_TYPE_DESCRIPTION = "Get accounts that either contain or start with the value specified in the Search parameter.\n" +
                "Valid values: contains/startswith";
        public static final String SORT_DESCRIPTION = "The property or properties that you want to sort returned accounts, " +
                "followed by asc (default) or desc to control sort direction. Separate multiple properties with commas, " +
                "up to a maximum of three properties.";
        public static final String OFFSET_DESCRIPTION = "Offset of the first account that is returned in the collection of results.\n" +
                "Default value: 0";
        public static final String LIMIT_DESCRIPTION = "The maximum number of returned accounts. The maximum number that you can specify is 1000.\n" +
                "When used together with the Offset parameter, this value determines the number of accounts to return, starting from the first account that is returned.\n" +
                "Default value: 50";
        public static final String FILTER_DESCRIPTION = "Search for accounts using a filter.\n" +
                "To use more than one filter, use the AND operator.";
        public static final String SAVED_FILTER_DESCRIPTION = "Search for accounts using a saved filter(s).\n" +
                "Search using any of the following saved filter types.\n" +
                "Regular\n" +
                "Recently\n" +
                "New\n" +
                "Link\n" +
                "Deleted\n" +
                "PolicyFailures\n" +
                "AccessedByUsers\n" +
                "ModifiedByUsers\n" +
                "ModifiedByCPM\n" +
                "DisabledPasswordByUser\n" +
                "DisabledPasswordByCPM\n" +
                "ScheduledForChange\n" +
                "ScheduledForVerify\n" +
                "ScheduledForReconcile\n" +
                "SuccessfullyReconciled\n" +
                "FailedChange\n" +
                "FailedVerify\n" +
                "FailedReconcile\n" +
                "LockedOrNew\n" +
                "Locked\n" +
                "Favorites";
    }

    public static final class ChangeCredentialsInVault{
        public static final String CHANGE_CREDENTIALS_IN_SAFE = "Change Credentials In Vault";

        public static final String CHANGE_CREDENTIALS_IN_SAFE_DESCRIPTION = "This method enables users to set account credentials and change them in the Safe. This will not affect credentials on the target device.\n" +
                "\n" +
                "The user who runs this web service requires Update password value permission in the Safe where the privileged account is stored";
        public static final String NEW_CREDENTIALS = "newCredentials";

        public static final String NEW_CREDENTIALS_DESCRIPTION = "The new account credentials that will be allocated to the account in the Vault.\n" +
                "\n" +
                "Note: \n" +
                "\n" +
                "Digits are never placed as the first or last character of the password, regardless of the password policy or specifications.\n" +
                "If the specified password contains leading and/or trailing white spaces, they will automatically be removed.";
    }

    public static final class AddAccountConstants {
        public static final String ADD_ACCOUNT = "Add Account";
        public static final String ADD_ACCOUNT_DESCRIPTION = "This method adds a new privileged account to Privilege Cloud.";


        //Inputs
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String PLATFORM_ID = "platformId";
        public static final String SAFE_NAME = "safeName";
        public static final String SECRET_TYPE = "secretType";
        public static final String SECRET = "secret";
        public static final String PLATFORM_ACCOUNT_PROPERTIES = "platformAccountProperties";
        public static final String SECRET_MANAGEMENT = "secretManagement";
        public static final String REMOTE_MACHINE_ACCESS = "remoteMachineAccess";


        //Input descriptions
        public static final String NAME_DESCRIPTION = "The name of the account.";
        public static final String ADDRESS_DESCRIPTION = "The name or address of the machine where the account will be used.";
        public static final String USERNAME_DESCRIPTION = "Account's user name.";
        public static final String PLATFORM_ID_DESCRIPTION = "The platform assigned to this account.";
        public static final String SAFE_NAME_DESCRIPTION = "The Safe where the account is created.";
        public static final String SECRET_TYPE_DESCRIPTION = "The type of password.";
        public static final String SECRET_DESCRIPTION = "The password value. This will not be returned in the API output.";
        public static final String PLATFORM_ACCOUNT_PROPERTIES_DESCRIPTION = "Object containing key-value pairs to associate with the account, " +
                "as defined by the account platform. These properties are validated against the mandatory and optional properties of " +
                "the specified platform's definition. Optional properties that do not exist on the account will not be returned here. " +
                "Internal properties are not returned.\n" +
                "\n";
        public static final String SECRET_MANAGEMENT_DESCRIPTION = "JSON having secret management properties";
        public static final String REMOTE_MACHINE_ACCESS_DESCRIPTION = "JSON having remote machine access properties";


    }

    public static final class DeleteAccountConstants {
        public static final String DELETE_ACCOUNT = "Delete Account";
        public static final String DELETE_ACCOUNT_DESCRIPTION = "This method deletes a specific account in the Vault";


        //Inputs
        public static final String ID = "id";


        //Input descriptions
        public static final String ID_DESCRIPTION = "The account's unique ID, composed of the SafeID and internal AccountID of the account to delete.";


    }

    public static class GetAllSafesConstants {
        public static final String GET_ALL_SAFES = "Get All Safes";
        public static final String GET_ALL_SAFES_DESCRIPTION = "This method returns a list of all Safes in the Vault that the user has permissions for.";

        //Inputs
        public static final String SEARCH = "search";
        public static final String SORT = "sort";
        public static final String OFFSET = "offset";
        public static final String LIMIT = "limit";
        public static final String INCLUDE_ACCOUNTS = "includeAccounts";
        public static final String EXTENDED_DETAILS = "extendedDetails";

        //Input descriptions

        public static final String SEARCH_DESCRIPTION = "A list of keywords to search for in safes, separated by a space.";
        public static final String SORT_DESCRIPTION = "Sorts according to the safeName property in ascending order (default) " +
                "or descending order to control the sort direction. Valid values: asc, desc";
        public static final String OFFSET_DESCRIPTION = "Offset of the first safe that is returned in the collection of results.\n" +
                "Default value: 0";
        public static final String LIMIT_DESCRIPTION = "The maximum number of returned safes.\n" +
                "When used together with the Offset parameter, this value determines the number of safes to return, starting from the first safe that is returned.\n" +
                "Default value: 25";

        public static final String INCLUDE_ACCOUNTS_DESCRIPTION = "Whether or not to return accounts for each Safe " +
                "as part of the response. If not sent, the value is False.";
        public static final String EXTENDED_DETAILS_DESCRIPTION = "Whether or not to return all Safe details or only safeName " +
                "as part of the response. If not sent, the value is True.";
    }

    public static class AddMemberConstants {

        public static final String ADD_MEMBER = "Add Member";
        public static final String ADD_MEMBER_DESCRIPTION = "This method adds an existing user or group as a Safe member.\n" +
                "The user who runs this web service must have Manage Safe Members permissions in Privilege Cloud.";

        //Inputs
        public static final String SAFE_URL_ID = "safeUrlId";
        public static final String MEMBER_NAME = "memberName";
        public static final String SEARCH_IN = "searchIn";
        public static final String MEMBERSHIP_EXPIRATION_DATE = "membershipExpirationDate";
        public static final String PERMISSIONS = "permissions";
        public static final String IS_READ_ONLY = "isReadOnly";
        public static final String MEMBER_TYPE = "memberType";

        //Input descriptions
        public static final String SAFE_URL_ID_DESCRIPTION = "safeUrlId";
        public static final String MEMBER_NAME_DESCRIPTION = "Privilege Cloud user name, Domain user name or group name of the Safe member.\n" +
                "The following characters cannot be used in the Safe member name: \\ / : * < > “ | ? % & +";
        public static final String SEARCH_IN_DESCRIPTION  = "Privilege Cloud (Vault) or the domain where the user or group was found.\n" +
                "Default value: Vault";
        public static final String MEMBERSHIP_EXPIRATION_DATE_DESCRIPTION  = "The member's expiration date for this Safe. For members that do not have an expiration date, this value will be null.";
        public static final String PERMISSIONS_DESCRIPTION  = "The permissions that the user or group has on this Safe.\n" +
                "List of key=value pairs delimited by ';'.\n"+
                "Example: useAccounts=true;retrieveAccounts=false;listAccounts=false";
        public static final String IS_READ_ONLY_DESCRIPTION  = "Whether or not the current user can update the permissions of the member.\n" +
                "Valid values: true, false\n" +
                "Default value: false";
        public static final String MEMBER_TYPE_DESCRIPTION  = "The member type.\n" +
                "Valid values: User, Group\n" +
                "Default value: User";
    }

    public static class AddSafeConstants {

        public static final String ADD_SAFE = "Add Safe";
        public static final String ADD_SAFE_DESCRIPTION = "This method adds a new Safe to Privilege Cloud.\n" +
                "The user who runs this web service must have Add Safes permissions in Privilege Cloud.";

        //Inputs
        public static final String SAFE_NAME = "safeName";
        public static final String DESCRIPTION = "description";
        public static final String LOCATION = "location";
        public static final String OLAC_ENABLED = "olacEnabled";
        public static final String MANAGING_CPM = "managingCPM";
        public static final String NUMBER_OF_VERSION_RETENTION = "numberOfVersionsRetention";
        public static final String NUMBER_OF_DAYS_RETENTION = "numberOfDaysRetention";
        public static final String AUTO_PURGE_ENABLED = "autoPurgeEnabled";

        //Input descriptions
        public static final String SAFE_NAME_DESCRIPTION = "The unique name of the Safe.\n" +
                "The following characters cannot be used in the Safe name: \\ / : * < > . | ? “% & +";
        public static final String DESCRIPTION_DESCRIPTION = "The description of the Safe.";
        public static final String LOCATION_DESCRIPTION = "The location of the Safe in the Vault.";
        public static final String OLAC_ENABLED_DESCRIPTION = "Whether or not to enable Object Level Access Control for the new Safe.\n" +
                "Valid values: true, false\n" +
                "Default value: false";
        public static final String MANAGING_CPM_DESCRIPTION = "The name of the CPM user who will manage the new Safe.";
        public static final String NUMBER_OF_VERSION_RETENTION_DESCRIPTION = "The number of retained versions of every password that is stored in the Safe.";
        public static final String NUMBER_OF_DAYS_RETENTION_DESCRIPTION = "The number of days that password versions are saved in the Safe.\n" +
                "Default value: 7";
        public static final String AUTO_PURGE_ENABLED_DESCRIPTION = "Whether or not to automatically purge files after the end of the Object History Retention Period defined in the Safe properties.\n" +
                "Report Safes and PSM Recording Safes are created automatically with AutoPurgeEnabled set to Yes.\n" +
                "These Safes cannot be managed by the CPM.\n" +
                "Valid values: true, false\n" +
                "Default value: false";
    }

    public static class DeleteSafeConstants {

        public static final String DELETE_SAFE = "Delete Safe";
        public static final String DELETE_SAFE_DESCRIPTION = "This method deletes a Safe from the Vault.\n" +
                "The user who runs this web service must have Manage Safe permissions in the Safe.";

        //Inputs
        public static final String SAFE_URL_ID = "safeUrlId";

        //Input descriptions
        public static final String SAFE_URL_ID_DESCRIPTION = "The unique ID of the Safe.";
    }

    public static final class OtherConstants {

        //API endpoints
        public static final String GET_AUTH_TOKEN_ENDPOINT = "/PasswordVault/API/auth/Cyberark/Logon";
        public static final String GET_ACCOUNTS_ENDPOINT = "/PasswordVault/API/Accounts";
        public static final String GET_PASSWORD_VALUE_ENDPOINT = "/AIMWebService/api/Accounts";
        public static final String ADD_ACCOUNT_ENDPOINT = "/PasswordVault/API/Accounts";
        public static final String DELETE_ACCOUNT_ENDPOINT = "/PasswordVault/API/Accounts";
        public static final String GET_ALL_SAFES_ENDPOINT = "/PasswordVault/API/safes";
        public static final String ADD_MEMBER_ENDPOINT = "/PasswordVault/API/Safes/";
        public static final String ADD_SAFE_ENDPOINT = "/PasswordVault/API/Safes";
        public static final String DELETE_SAFE_ENDPOINT = "/PasswordVault/API/Safes/";
        public static final String CHANGE_CREDENTIALS_IN_VAULT_ENDPOINT = "/Password/Update";

        //Common inputs
        public static final String USERNAME = "username";

        //Other
        public static final String PROTOCOL_DELIMITER = "://";
        public static final String FORWARD_SLASH = "/";
        public static final String ANONYMOUS = "ANONYMOUS";
        public static final String CONTENT_TYPE = "Content-Type:";
        public static final String APPLICATION_JSON = "application/json";
        public static final String COMMA = ",";
        public static final String AUTHORIZATION = "Authorization:";
        public static final String CONTENT = "Content";
        public static final String MEMBERS = "/Members";
        public static final String EQUALS = "=";
        public static final String SEMICOLON = ";";
    }

    public static final class GetSafeDetailsConstants {
        public static final String GET_SAFE_DETAILS_ENDPOINT = "/PasswordVault/api/Safes/";
        public static final String GET_SAFE_DETAILS = "Get Safe Details";
        public static final String GET_SAFE_DETAILS_DESCRIPTION = "This operation returns information about a specific Safe in Privilege Cloud.";
        public static final String SAFE_URL_ID = "safeUrlId";
        public static final String SAFE_URL_ID_DESCRIPTION = "The unique ID of the Safe.";
        public static final String INCLUDE_ACCOUNTS = "includeAccounts";
        public static final String INCLUDE_ACCOUNTS_DESCRIPTION = "Whether or not to return accounts for each Safe as part of the response. If not sent, the value will be False.";
        public static final String USE_CACHE = "useCache";
        public static final String USE_CACHE_DESCRIPTION = "Whether or not to retrieve the cache from a session.";
    }

    public static final class GetAllSafeMembersConstants{
        public static final String GET_ALL_SAFE_MEMBERS = "Get All Safe Members";

        public static final String GET_ALL_SAFE_MEMBERS_DESCRIPTION = "This method returns a list of the members of a Safe.\n" +
                "\n" +
                "The user who runs this web service must have View Safe Members permissions in the Safe.";

        public static final String SAFE_URL_ID = "safeUrlId";

        public static final String SAFE_URL_ID_DESCRIPTION = "The unique ID of the Safe used when calling Safe APIs.";

        public static final String SEARCH_DESCRIPTION = "Searches according to the Safe name. Search is performed according to the REST standard (search=\"search word\").";

        public static final String SORT_DESCRIPTION = "Sorts according to the memberName property in ascending order (default) or descending order to control the sort direction.";
        public static final String OFFSET_DESCRIPTION = "Offset of the first member that is returned in the collection of results.\n" +
                "Default value: 0";
        public static final String LIMIT_DESCRIPTION = "The maximum number of  of members that are returned." +
                " When used together with the offset parameter, this value determines the number of Safes to return," +
                " starting from the first Safe that is returned.\n"+
                "Default value: 25";
        public static final String FILTER_DESCRIPTION = "Search for accounts using a filter.\n" +
                "To use more than one filter, use the AND operator.";


    }
}

