/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.ldap.constants;

public class Descriptions {
    public static class Common {

        //inputs
        public static final String HOST_DESC = "The domain controller to connect to.";
        public static final String PROTOCOL_DESC = " The protocol to use when connecting to the Active Directory server. Valid values: " +
                "HTTP and HTTPS.";
        public static final String USERNAME_DESC = "The user to connect to Active Directory as.";
        public static final String PASSWORD_DESC = "The password of the user to connect to Active Directory.";
        public static final String TLS_VERSION_DESC = "The version of TLS to use. The value of this input will be ignored if 'protocol'" +
                "is set to 'HTTP'." +
                "Valid values: SSLv3, TLSv1, TLSv1.1, TLSv1.2." +
                "Default value: TLSv1.2.";
        public static final String ALLOWED_CIPHERS_DESC = " A list of ciphers to use. The value of this input will be ignored if 'tlsVersion' does\n" +
                "not contain 'TLSv1.2'.\n" +
                "Default value: TLS_DHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,\n" +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, " +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,\n" +
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256,\n" +
                "TLS_RSA_WITH_AES_128_CBC_SHA256.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL. A SSL " +
                "certificate is trust even if no trusted certification authority issued it." +
                "Valid values: true, false" +
                "Default value: true";
        public static final String TRUST_KEYSTORE_DESC = "The location of the TrustStore file." +
                "Example: %JAVA_HOME%/jre/lib/security/cacerts.";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file.";
        public static final String CONNECTION_TIMEOUT_DESC = "Time in milliseconds to wait for the connection to be made." +
                "Default value: 10000.";
        public static final String EXECUTION_TIMEOUT_DESC = "Time in milliseconds to wait for the connection to complete." +
                "Default value: 60000.";
        public static final String PROXY_HOST_DESC = "The proxy server used to access the web site.";
        public static final String PROXY_PORT_DESC = "The proxy server port." +
                "Default value: 8080.";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The proxy server password associated with the 'proxyUsername'" +
                " input value.";
        public static final String X_509_DESC = "Specifies the way the server hostname must match a domain name in the subject's Common" +
                "Name (CN) or subjectAltName field of the X.509 certificate. Set this to 'allow_all' to skip any checking, but you become " +
                "vulnerable to attacks. For the value 'browser_compatible' the hostname verifier works the same way as Curl" +
                "and Firefox. The hostname must match either the first CN, or any of the subject-alts. A wildcard can occur in the CN, and " +
                "in any of the subject-alts. The only difference between 'browser_compatible' and 'strict' is that a wildcard (such as " +
                "'*.foo.com') with 'browser_compatible' matches all subdomains, including 'a.b.foo.com'. From the" +
                "security perspective, to provide protection against possible Man-In-The-Middle attacks, we strongly recommend to use " +
                "'strict' option. " +
                "Default value: strict." +
                "Valid values: strict, browser_compatible, allow_all.";

        //outputs
        public static final String RETURN_CODE_DESC = "The return code of the operation. 0 if the operation succeeded," +
                " -1 if the operation fails.";
        public static final String EXCEPTION_DESC = "The exception message if the operation fails.";
    }

    public static class CreateComputerAccount {

        public static final String CREATE_COMPUTER_ACCOUNT_DESC = "Creates a new computer account in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN to add the " +
                "computer to. (i.e. OU=OUTest1,DC=battleground,DC=ad)";
        public static final String COMPUTER_COMMON_NAME_DESC = "The name of the computer (its CN).";
        public static final String SAM_ACCOUNT_NAME_DESC = "Computer's sAMAccountName (ex. MYHYPNOS$). If not provided " +
                "it will be assigned from computerCommonName.";
        public static final String ESCAPE_CHARS_DESC = "Specifies whether to escape the special Active Directory characters:\n" +
                "'#','=','\"','<','>',',','+',';','\\','\"''.\n" +
                "Default value: false.\n" +
                "Valid values: true, false.";

        //outputs
        public static final String RESULT_COMPUTER_DN_DESC = "The distinguished name of the newly created computer account.";
        public static final String RETURN_RESULT_DESC = "The return result of the operation.";

        //results
        public static final String SUCCESS_DESC = "The new computer account was created in the Active Directory.";
        public static final String FAILURE_DESC = "Failed to create the new computer account in the Active Directory.";
    }

    public static class DeleteComputerAccount {

        public static final String DELETE_COMPUTER_ACCOUNT_DESC = "Deletes a computer account from Active Directory.";

        //outputs
        public static final String RESULT_COMPUTER_DN_DELETED = "The distinguished name of the computer account that was deleted.";

        //results
        public static final String SUCCESS_DESC = "The computer account was successfully deleted.";
        public static final String FAILURE_DESC = "Failed to delete the computer account.";
    }

    public static class DisableComputerAccount {

        public static final String DISABLE_COMPUTER_ACCOUNT_DESC = "Disables a computer account in Active Directory.";

        //outputs
        public static final String RESULT_DISABLE_COMPUTER_DN_DESC = "The distinguished name of the computer account that was disabled.";

        //results
        public static final String SUCCESS_DESC = "The computer account was successfully disabled.";
        public static final String FAILURE_DESC = "Failed to disable the computer account.";
    }

    public static class EnableComputerAccount {

        public static final String ENABLE_COMPUTER_ACCOUNT_DESC = "Enables a computer account in Active Directory.";

        //outputs
        public static final String RESULT_ENABLE_COMPUTER_DN_DESC = "The distinguished name of the computer account that was enabled.";

        //results
        public static final String SUCCESS_DESC = "The computer account was successfully enabled.";
        public static final String FAILURE_DESC = "Failed to enable the computer account.";

    }

    public static class GetComputerAccountOU {

        public static final String GET_COMPUTER_ACCOUNT_OU_DESC = "Gets the name of the OU a computer account is in, in Active Directory.";

        //inputs
        public static final String ROOT_DISTINGUISHED_NAME_DESC = "The distinguished name of the root element whose subtree you want to search in.";
        public static final String COMPUTER_COMMON_NAME_DESC = "The name of the computer account.";

        //outputs
        public static final String RESULT_GET_COMPUTER_OU_DESC = "The name of the OU where the computer account is located.";

        //results
        public static final String SUCCESS_DESC = "The name of the OU the computer is in was retrieved successfully.";
        public static final String FAILURE_DESC = "Determining computer's OU failed.";
    }

    public static class IsComputerAccountEnabled {

        public static final String IS_COMPUTER_ACCOUNT_ENABLED_DESC = "Checks to see if a computer account is enabled in Active Directory.";

        //inputs
        public static final String IS_COMPUTER_ACCOUNT_ENABLED_DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN the computer is in (i.e. OU=OUTest1,DC=battleground,DC=ad).";
        public static final String IS_COMPUTER_ACCOUNT_ENABLED_COMPUTER_COMMON_NAME_DESC = "The name of the computer account to check.";

        //outputs
        public static final String IS_COMPUTER_ACCOUNT_ENABLED_RESULT_OU_DESC = "The distinguished Name of the computer account.";

        //results
        public static final String SUCCESS_DESC = "The computer account is enabled.";
        public static final String FAILURE_DESC = "The computer account is disabled or something went wrong.";
    }

    public static class MoveComputerAccountToOU {

        public static final String MOVE_COMPUTER_ACCOUNT_TO_OU_DESC = "Moves a computer account in a new OU in Active Directory.";

        //inputs
        public static final String MOVE_COMPUTER_ACCOUNT_DISTINGUISHED_NAME_DESC = "The distinguished name of the computer account we want to move.";
        public static final String MOVE_COMPUTER_ACCOUNT_COMMON_NAME_DESC = "The Organizational Unit that the computer account will be moved to.";

        //outputs
        public static final String RETURN_RESULT_DESC = "The new distinguished name (DN) of the computer account, after it was moved to the new OU.";

        //results
        public static final String SUCCESS_DESC = "Moving the computer account to a new OU was successful.";
        public static final String FAILURE_DESC = "Moving computer account to new OU failed.";
    }

    public static class ResetComputerAccount {

        public static final String RESET_COMPUTER_ACCOUNT_DESC = "Resets a computer account in Active Directory, by resetting the password to an initial password.";

        //inputs
        public static final String COMPUTER_DISTINGUISHED_NAME_DESC = "The distinguished name of the computer account we want to reset.";

        //results
        public static final String SUCCESS_DESC = "Computer account reset successfully.";
        public static final String FAILURE_DESC = "Failed to reset computer account.";
    }

    public static class AddUserToGroup {

        public static final String ADD_USER_TO_GROUP_DESC = "Adds a user to a group in Active Directory.";

        //inputs
        public static final String GROUP_DISTINGUISHED_NAME_DESC = "The Distinguished Name of the group.";
        public static final String USER_DISTINGUISHED_NAME_DESC = "The Distinguished Name of the user to add.";

        //results
        public static final String SUCCESS_DESC = "Added user to group successfully.";
        public static final String FAILURE_DESC = "Failed to add user to group.";
    }

    public static class CreateGroup {

        public static final String CREATE_GROUP_DESC = "Creates a new group in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN to add the group to (i.e. OU=OUTest1,DC=battleground,DC=ad).";
        public static final String GROUP_COMMON_NAME_DESC = "The name of new group (its CN).";
        public static final String GROUP_TYPE_DESC = "The type of the new group. The groupType values represent: -2147483646 (Security Group - Global), -2147483644 (Security Group - Domain Local), -2147483640 (Security Group - Universal), 2 (Distribution Group - Global), 4 (Distribution Group - Domain Local), 8 (Distribution Group - Universal)\n" +
                "Valid values: -2147483646, -2147483644, -2147483640, 2, 4, 8\n" +
                "Default value: -2147483646";
        public static final String SAM_ACCOUNT_NAME_DESC = "The sAMAccountName of the new group.";
        public static final String ESCAPE_CHARS_DESC = "Add this input and set to true if you want the operation to escape the special AD chars.";

        //outputs
        public static final String GROUP_DISTINGUISHED_NAME_DESC = "The Distinguished Name of the newly created group.";

        //results
        public static final String SUCCESS_DESC = "Created a new group in Active Directory successfully.";
        public static final String FAILURE_DESC = "Failed to create a new group in Active Directory.";
    }

    public static class DeleteGroup {

        public static final String DELETE_GROUP_DESC = "Deletes a group in Active Directory.";

        //inputs
        public static final String GROUP_COMMON_NAME_DESC = "The name of the group (its CN).";

        //outputs
        public static final String RESULT_GROUP_DN_DESC = "The distinguished name of the deleted group.";

        //results
        public static final String SUCCESS_DESC = "Deleted the group from the Active Directory successfully.";
        public static final String FAILURE_DESC = "Failed to delete the group from Active Directory.";
    }

    public static class RemoveUserFromGroup {

        public static final String REMOVE_USER_FROM_GROUP = "Removes a user from a group in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "Distinguished Name of the group.";
        public static final String USER_DISTINGUISHED_NAME_DESC = "Distinguished Name of the user to remove.";

        //results
        public static final String SUCCESS_DESC = "Removed user from group successfully.";
        public static final String FAILURE_DESC = "Failed to remove user from group.";
    }

    public static class AuthenticateUser {

        public static final String AUTHENTICATE_USER_DESC = "Authenticates a user against Active Directory.";

        //inputs
        public static final String HOST_DESC = "The hostname or IP address of the Active Directory server.";
        public static final String ROOT_DISTINGUISHED_NAME_DESC = "The distinguished name of the root element whose subtree you want to search in (e.g. CN=Users,DC=domain,DC=com).";
        public static final String USERNAME_DESC = "The user's windows username. The only valid format is domain\\username.";
        public static final String PASSWORD_DESC = "The user's password.";

        //results
        public static final String SUCCESS_DESC = "User successfully authenticated against Active Directory.";
        public static final String FAILURE_DESC = "User could not be authenticated against Active Directory.";
    }

    public static class CreateUser {

        public static final String CREATE_USER_DESC = "Creates a new user in Active Directory.";

        //inputs
        public static final String USER_CN_DESC = "The CN, generally the full name of user.\n" +
                "Example: Bob Smith";
        public static final String USER_PASS_DESC = "The password for the new user. It must meet the following requirements:\n" +
                "- is at least six characters long\n" +
                "- contains characters from at least three of the following five categories: English uppercase\n" +
                "characters ('A' - 'Z'), English lowercase characters ('a' - 'z'), base 10 digits ('0' - '9'),\n" +
                "non-alphanumeric (For example: '!', '$', '#', or '%'), unicode characters\n" +
                "- does not contain three or more characters from the user's account name";
        public static final String SAM_ACCOUNT_NAME_DESC = "The sAMAccountName. If this input is empty, the value will be assigned from input \"userCommonName\".";
        public static final String HOST_DESC = "The IP or host name of the domain controller. The port number can be mentioned as well, along with the host (hostNameOrIP:PortNumber).\n" +
                "Examples: test.example.com,  test.example.com:636, <IPv4Address>, <IPv6Address>, [<IPv6Address>]:<PortNumber> etc.\n" +
                "Value format: The format of an IPv4 address is: [0-225].[0-225].[0-225].[0-225]. " +
                "The format of an IPv6 address is ####:####:####:####:####:####:####:####/### (with a prefix), " +
                "where each #### is a hexadecimal value between 0 to FFFF and the prefix /### is a decimal value between 0 to 128. " +
                "The prefix length is optional.";
        public static final String DISTINGUISHED_NAME_DESC = " The Organizational Unit DN or Common Name DN to add the user to.\n" +
                "Example: OU=OUTest1,DC=battleground,DC=ad";
        public static final String ESCAPE_CHARS_DESC = "Add this input and set to true if you want the operation to escape the special AD chars.";

        //outputs
        public static final String RESULT_USER_DN_DESC = "The distinguished name of the newly created user.";
        public static final String RETURN_RESULT_CREATE_USER = " A message with the common name of the newly created user in case of success " +
                "or the error message in case of failure.";

        //results
        public static final String SUCCESS_DESC = "New user successfully created in Active Directory.";
        public static final String FAILURE_DESC = "New user could not be created in Active Directory.";
    }

    public static class DeleteUser {

        public static final String DELETE_USER_DESC = "Deletes a user from Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN the user that should be deleted is in.\n" +
                "Example: OU=OUTest1,DC=battleground,DC=ad";

        //outputs
        public static final String RETURN_RESULT_DELETE_USER = " A message with the common name of the deleted user in case of success " +
                "or the error message in case of failure.";
        public static final String RESULT_USER_DN_DELETED_DESC = "The distinguished name of the deleted created user.";

        //results
        public static final String SUCCESS_DESC = "The user was successfully deleted.";
        public static final String FAILURE_DESC = "Failed to delete the user.";
    }

    public static class DisableUser {

        public static final String DISABLE_USER_DESC = "This operation disables a user account in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN the user is in.\n" +
                "Example: OU=OUTest1,DC=battleground,DC=ad";

        //outputs
        public static final String USER_DN_DESC = "The distinguished name of the user that was disabled.";

        //results
        public static final String SUCCESS_DESC = "The user was successfully disabled.";
        public static final String FAILURE_DESC = "Failed to disable the user.";
    }

    public static class EnableUser {

        public static final String ENABLE_USER_DESC = "This operation enables a user account in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = " The Organizational Unit Distinguished Name (DN) or Common Name DN the user is in.\n" +
                "Example: OU=OUTest1, DC=battleground, DC=ad";

        //outputs
        public static final String RETURN_RESULT_DESC = "A message specifying the success or failure of the operation.";
        public static final String USER_DN_DESC = "The distinguished name of the user that was enabled.";
        public static final String RETURN_CODE_DESC = "This is the primary output. This is the exit code of the operation.";

        //results
        public static final String SUCCESS_DESC = "The user was successfully enabled.";
        public static final String FAILURE_DESC = "Failed to enable the user.";
    }

    public static class IsUserEnabled {

        public static final String IS_USER_ENABLED_DESC = "This operation checks to see if a user account is enabled in Active Directory.";

        //inputs
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit Distinguished Name (DN) or Common Name DN the user is in.\n" +
                "Example: OU=OUTest1,DC=battleground,DC=ad.";

        //outputs
        public static final String RETURN_RESULT_DESC = "A message specifying the status of the user.";
        public static final String USER_DN_DESC = "The distinguished name of the user.";
        public static final String RETURN_CODE_DESC = "This is the primary output. This is the exit code of the operation.";

        //results
        public static final String SUCCESS_DESC = "The user is enabled.";
        public static final String FAILURE_DESC = "The user is disabled or something went wrong.";
    }

    public static class ResetUserPassword {

        public static final String RESET_USER_PASSWORD_DESC = "This operation resets a user's password in Active Directory.";

        //inputs
        public static final String USER_PASSWORD_DESC = "The new password (must meet complexity requirements specified in notes section).";
        public static final String DISTINGUISHED_NAME_DESC = "Distinguished name of the user whose password you want to change.\n" +
                "Example: CN=User, OU=OUTest1, DC=battleground, DC=ad).";

        //outputs
        public static final String RETURN_RESULT_DESC = "It contains the \"Password Changed\" message if the operations successfully completes, or an error message otherwise.";
        public static final String RETURN_CODE_DESC = "This is the primary output. It contains the value 0 if the operation successfully completes and -1 otherwise.";

        //results
        public static final String SUCCESS_DESC = "The password has been reset successfully.";
        public static final String FAILURE_DESC = "Failed to reset the password.";
    }

    public static class UpdateUserDetails {

        public static final String UPDATE_USER_DETAILS_DESC = "Adds attributes to a new user in Active Directory.\n" +
                "Can be used to edit the provided inputs of a new user or to add custom attributes to a user, by providing a list of attributes and values, separated by new line in this format: attribute:value.\n" +
                "Make sure to provide valid Active Directory attributes.";

        //inputs
        public static final String HOST_DESC = "Name or IP address of LDAP server to query. Can also be used in the form \" host:port\".";
        public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN of the user to set attributes to\n" +
                "Example: OU=OUTest1,DC=battleground,DC=ad.";
        public static final String FIRST_NAME_DESC = "User first name to change.";
        public static final String LAST_NAME_DESC = "User last name to change.";
        public static final String DISPLAY_NAME_DESC = "User display name to change.";
        public static final String STREET_DESC = "User street.";
        public static final String CITY_DESC = "City of the user.";
        public static final String STATE_OR_PROVINCE_DESC = "User state or province.";
        public static final String ZIP_OR_POSTAL_CODE_DESC = "User zip or postal code.";
        public static final String COUNTRY_OR_REGION_DESC = "User country or region. The format for this input should be " +
                "countryName,countryAbbreviation,countryCode. countryName sets the value for the \"co\" property, countryAbbreviation" +
                " sets the \"c\" property using the two-letter country code, countryCode sets the \"countryCode\" property using the numeric " +
                "value of the country.";
        public static final String ATTRIBUTES_LIST_DESC = "The list of the attributes to set to the user. " +
                "Should be provided in the following format: attribute:value, separated by new line. " +
                "Make sure that the attributes are valid Active Directory attributes.\n" +
                "Example: \n" +
                "streetAddress:My Address\n" +
                "postalCode:123456";

        //outputs
        public static final String RETURN_RESULT_DESC = "This will contain the response entity. In case of an error this output will contain the error message.";
        public static final String RETURN_CODE_DESC = "This is the primary output. It contains the value 0 if the operation successfully completes and -1 otherwise.";

        //results
        public static final String SUCCESS_DESC = "The attributes were successfully updated.";
        public static final String FAILURE_DESC = "There was a problem while updating the attribute.";
    }

}