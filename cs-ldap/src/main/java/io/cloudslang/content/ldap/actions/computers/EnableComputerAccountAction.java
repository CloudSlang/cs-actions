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
package io.cloudslang.content.ldap.actions.computers;

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
import io.cloudslang.content.ldap.entities.EnableComputerAccountInput;
import io.cloudslang.content.ldap.services.computers.EnableComputerAccountService;
import io.cloudslang.content.ldap.utils.ResultUtils;

import java.util.Map;

import static io.cloudslang.content.ldap.constants.Descriptions.Common.*;
import static io.cloudslang.content.ldap.constants.Descriptions.EnableComputerAccount.*;
import static io.cloudslang.content.ldap.constants.Descriptions.CreateComputerAccount.RETURN_RESULT_DESC;
import static io.cloudslang.content.ldap.constants.Descriptions.CreateComputerAccount.DISTINGUISHED_NAME_DESC;
import static io.cloudslang.content.ldap.constants.Descriptions.CreateComputerAccount.COMPUTER_COMMON_NAME_DESC;

public class EnableComputerAccountAction {
    /**
     * Enables a computer account in Active Directory.
     *
     * @param host               The domain controller to connect to.
     * @param distinguishedName  The Organizational Unit DN or Common Name DN to add the computer to.
     *                           (i.e. OU=OUTest1,DC=battleground,DC=ad)
     * @param computerCommonName The name of the computer (its CN).
     * @param username           The user to connect to Active Directory as.
     * @param password           The password of the user to connect to Active Directory.
     * @param protocol           The protocol to use when connecting to the Active Directory server.
     *                           Valid values: 'HTTP' and 'HTTPS'.
     * @param trustAllRoots      Specifies whether to enable weak security over SSL. A SSL certificate is trusted
     *                           even if no trusted certification authority issued it.
     *                           Valid values: true, false.
     *                           Default value: true.
     * @param trustKeystore      The location of the TrustStore file.
     *                           Example: %JAVA_HOME%/jre/lib/security/cacerts.
     * @param trustPassword      The password associated with the TrustStore file.
     * @param connectionTimeout  Time in milliseconds to wait for the connection to be made.
     *                           Default value: 10000.
     * @param executionTimeout   Time in milliseconds to wait for the command to complete.
     *                           Default value: 90000.
     * @return a map containing the output of the operations. Keys present in the map are:
     * returnResult - The return result of the operation.
     * returnCode - The return code of the operation. 0 if the operation succeeded, -1 if the operation fails.
     * exception - The exception message if the operation fails.
     * computerDN - The distinguished name of the computer account that was enabled.
     */
    @Action(name = "Enable Computer Account", description = ENABLE_COMPUTER_ACCOUNT_DESC,
            outputs = {
                    @Output(value = OutputNames.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = OutputNames.RESULT_COMPUTER_DN, description = RESULT_ENABLE_COMPUTER_DN_DESC),
                    @Output(value = OutputNames.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = OutputNames.EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.HOST, required = true, description = HOST_DESC) String host,
            @Param(value = InputNames.DISTINGUISHED_NAME, required = true, description = DISTINGUISHED_NAME_DESC) String distinguishedName,
            @Param(value = InputNames.COMPUTER_COMMON_NAME, required = true, description = COMPUTER_COMMON_NAME_DESC) String computerCommonName,
            @Param(value = InputNames.USERNAME, description = USERNAME_DESC) String username,
            @Param(value = InputNames.PASSWORD, encrypted = true, description = PASSWORD_DESC) String password,
            @Param(value = InputNames.PROTOCOL, description = PROTOCOL_DESC) String protocol,
            @Param(value = InputNames.TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
            @Param(value = InputNames.TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
            @Param(value = InputNames.TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
            @Param(value = InputNames.CONNECTION_TIMEOUT, description = CONNECTION_TIMEOUT_DESC) String connectionTimeout,
            @Param(value = InputNames.EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout) {
        EnableComputerAccountInput.Builder inputBuilder = new EnableComputerAccountInput.Builder()
                .host(host)
                .distinguishedName(distinguishedName)
                .computerCommonName(computerCommonName)
                .username(username)
                .password(password)
                .protocol(protocol)
                .trustAllRoots(trustAllRoots)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .connectionTimeout(connectionTimeout)
                .executionTimeout(executionTimeout);
        try {
            return new EnableComputerAccountService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}
