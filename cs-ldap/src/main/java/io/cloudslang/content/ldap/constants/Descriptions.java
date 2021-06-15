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

    //inputs
    public static final String HOST_DESC = "The domain controller to connect to.";
    public static final String DISTINGUISHED_NAME_DESC = "The Organizational Unit DN or Common Name DN to add the " +
            "computer to. (i.e. OU=OUTest1,DC=battleground,DC=ad)";
    public static final String COMPUTER_COMMON_NAME_DESC = "The name of the computer (its CN).";
    public static final String SAM_ACCOUNT_NAME_DESC = "Computer's sAMAccountName (ex. MYHYPNOS$). If not provided " +
            "it will be assigned from computerCommonName.";
    public static final String PROTOCOL_DESC = " The protocol to use when connecting to the Active Directory server. Valid values: " +
            "'HTTP' and 'HTTPS'.";
    public static final String USERNAME_DESC = "The user to connect to Active Directory as.";
    public static final String PASSWORD_DESC = "The password of the user to connect to Active Directory.";
    public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL. A SSL " +
            "certificate is trust even if no trusted certification authority issued it." +
            "Valid values: true, false" +
            "Default value: true";
    public static final String TRUST_KEYSTORE_DESC = "The location of the TrustStore file." +
            "Example: %JAVA_HOME%/jre/lib/security/cacerts.";
    public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file.";
    public static final String ESCAPE_CHARS_DESC = "Specifies whether to escape the special Active Directory characters:\n" +
            "'#','=','\"','<','>',',','+',';','\\','\"''.\n" +
            "Default value: false.\n" +
            "Valid values: true, false.";
    public static final String CONNECTION_TIMEOUT_DESC = "Time in milliseconds to wait for the connection to be made." +
            "Default value: 10000.";
    public static final String EXECUTION_TIMEOUT_DESC = "Time in milliseconds to wait for the connection to complete." +
            "Default value: 90000.";
    public static final String USER_CN_DESC = "The CN, generally the full name of user.\n" +
            "Example: Bob Smith";
    public static final String USER_PASS_DESC = "The password for the new user. It must meet the following requirements:\n" +
            "- is at least six characters long\n" +
            "- contains characters from at least three of the following five categories: English uppercase\n" +
            "characters ('A' - 'Z'), English lowercase characters ('a' - 'z'), base 10 digits ('0' - '9'),\n" +
            "non-alphanumeric (For example: '!', '$', '#', or '%'), unicode characters\n" +
            "- does not contain three or more characters from the user's account name";

    //results
    public static final String RETURN_CODE_DESC = "The return code of the operation. 0 if the operation succeeded," +
            " -1 if the operation fails.";
    public static final String EXCEPTION_DESC = "The exception message if the operation fails.";
    public static final String RETURN_RESULT_DESC = "The return result of the operation.";
    public static final String RESULT_COMPUTER_DN_DESC = "The distinguished name of the newly created computer account.";
    public static final String RESULT_COMPUTER_DN_DELETED = "The distinguished name of the computer account that was deleted.";
    public static final String RESULT_DISABLE_COMPUTER_DN_DESC = "The distinguished name of the computer account that was disabled.";
    public static final String RESULT_USER_DN_DESC = "The distinguished name of the newly created user.";
    public static final String RETURN_RESULT_CREATE_USER = " A message with the common name of the newly created user in case of success " +
            "or the error message in case of failure.";
    public static final String RETURN_RESULT_DELETE_USER = " A message with the common name of the deleted user in case of success " +
            "or the error message in case of failure.";
    public static final String RESULT_USER_DN_DELETED_DESC = "The distinguished name of the deleted created user.";
    public static final String RETURN_RESULT_ENABLE_USER = " A message with the common name of the enabled user in case of success " +
            "or the error message in case of failure.";
    public static final String RESULT_USER_DN_ENABLED_DESC = "The distinguished name of the enabled user.";

    //computers
    public static final String CREATE_COMPUTER_ACCOUNT_DESC = "Creates a new computer account in Active Directory.";
    public static final String DELETE_COMPUTER_ACCOUNT_DESC = "Deletes a computer account from Active Directory.";
    public static final String DISABLE_COMPUTER_ACCOUNT_DESC = "Disables a computer account in Active Directory.";

    //users
    public static final String CREATE_USER_DESC = "Creates a new user in Active Directory.";
    public static final String DELETE_USER_DESC = "Deletes a user from Active Directory.";
    public static final String ENABLE_USER_DESC = "Enables a user from Active Directory.";
}