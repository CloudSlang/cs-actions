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

package io.cloudslang.content.ldap.actions.users;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.ldap.constants.InputNames;
import io.cloudslang.content.ldap.constants.OutputNames;
import io.cloudslang.content.ldap.entities.ResetUserPasswordInput;
import io.cloudslang.content.ldap.services.users.ResetUserPasswordService;
import io.cloudslang.content.ldap.utils.ResultUtils;

import java.util.Map;

public class ResetUserPasswordAction {

    /**
     * This operation creates a new user in Active Directory.
     *
     * @param host                  The IP or host name of the domain controller. The port number can be mentioned as well, along
     *                              with the host (hostNameOrIP:PortNumber).
     *                              Examples: test.example.com,  test.example.com:636, <IPv4Address>, <IPv6Address>,
     *                              [<IPv6Address>]:<PortNumber> etc.
     *                              Value format: The format of an IPv4 address is: [0-225].[0-225].[0-225].[0-225]. The format of an
     *                              IPv6 address is ####:####:####:####:####:####:####:####/### (with a prefix), where each #### is
     *                              a hexadecimal value between 0 to FFFF and the prefix /### is a decimal value between 0 to 128.
     *                              The prefix length is optional.
     * @param userDistinguishedName Distinguished name of the user whose password you want to change.
     *                              Example: CN=User, OU=OUTest1, DC=battleground, DC=ad).
     * @param userPassword          The new password. It must meet the following requirements:
     *                              - is at least six characters long
     *                              - contains characters from at least three of the following five categories: English uppercase
     *                              characters ('A' - 'Z'), English lowercase characters ('a' - 'z'), base 10 digits ('0' - '9'),
     *                              non-alphanumeric (For example: '!', '$', '#', or '%'), unicode characters
     *                              - does not contain three or more characters from the user's account name
     * @param username              The user to connect to Active Directory as.
     * @param password              The password of the user to connect to Active Directory.
     * @param protocol              The protocol to use when connecting to the Active Directory server.
     *                              Valid values: 'HTTP' and 'HTTPS'.
     * @param trustAllRoots         Specifies whether to enable weak security over SSL. A SSL certificate is trusted even if
     *                              no trusted certification authority issued it.
     *                              Default value: true.
     *                              Valid values: true, false.
     * @param trustKeystore         The location of the TrustStore file.
     *                              Example: %JAVA_HOME%/jre/lib/security/cacerts
     * @param trustPassword         The password associated with the TrustStore file.
     * @param connectionTimeout     Time in milliseconds to wait for the connection to be made.
     *                              Default value: 10000.
     * @param executionTimeout      Time in milliseconds to wait for the command to complete.
     *                              Default value: 90000.
     * @return a map containing the output of the operation. Keys present in the map are:
     * returnResult - The message 'Password Changed' in case of success or the error in case of failure.
     * returnCode - The return code of the operation. 0 if the operation succeeded, -1 if the operation fails.
     * exception - The exception message if the operation fails.
     */

    @Action(name = "Reset User Password",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.HOST, required = true) String host,
            @Param(value = InputNames.USER_DISTINGUISHED_NAME, required = true) String userDistinguishedName,
            @Param(value = InputNames.USER_PASSWORD, required = true, encrypted = true) String userPassword,
            @Param(value = InputNames.USERNAME) String username,
            @Param(value = InputNames.PASSWORD, encrypted = true) String password,
            @Param(value = InputNames.PROTOCOL) String protocol,
            @Param(value = InputNames.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = InputNames.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = InputNames.TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = InputNames.CONNECTION_TIMEOUT) String connectionTimeout,
            @Param(value = InputNames.EXECUTION_TIMEOUT) String executionTimeout){

        ResetUserPasswordInput.Builder inputBuilder = new ResetUserPasswordInput.Builder()
                .host(host)
                .userDistinguishedName(userDistinguishedName)
                .userPassword(userPassword)
                .username(username)
                .password(password)
                .protocol(protocol)
                .trustAllRoots(trustAllRoots)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .connectionTimeout(connectionTimeout)
                .executionTimeout(executionTimeout);
        try {
            return new ResetUserPasswordService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}

