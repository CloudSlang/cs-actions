/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.actions.variables;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.CreateVariableInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.hashicorp.terraform.entities.CreateVariableInputs.*;
import static io.cloudslang.content.hashicorp.terraform.services.VariableImpl.createVariable;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.CREATE_WORKSPACE_OPERATION_NAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateVariable.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.STATUS_CODE_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_HOST;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PASSWORD;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PORT;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_USERNAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateWorkspaceOutputs.WORKSPACE_ID;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CreateVariable {
    /**
     * Creates a variable in workspace.
     *
     * @param authToken              required - authentication token used to connect to Terraform API.
     * @param variableName           Optional - The name of the variable.
     * @param variableValue          Optional - The value of the variable.
     * @param variableCategory       Optional - Whether this is a Terraform or environment variable.
     *                               Valid values are "terraform" or "env".
     * @param sensitive              Optional - Whether the value is sensitive. If true then the variable is written once and not visible thereafter.
     *                               Default: false
     * @param hcl                    Optional - Whether to evaluate the value of the variable as a string of HCL code. Has no effect for environment variables.
     *                               Default: false
     * @param workspaceId            Optional - The Id of workspace.
     * @param requestBody            Optional - The request body of the workspace.
     * @param proxyHost              Optional - proxy server used to connect to Terraform API. If empty no proxy will be used.
     * @param proxyPort              Optional - proxy server port. You must either specify values for both proxyHost and
     *                               proxyPort inputs or leave them both empty.
     *                               Default: 8080
     * @param proxyUsername          Optional - proxy server user name.
     * @param proxyPassword          Optional - proxy server password associated with the proxyUsername input value.
     * @param trustAllRoots          Optional - Specifies whether to enable weak security over SSL/TSL.
     *                               Default: false
     * @param x509HostnameVerifier   Optional - Specifies the way the server hostname must match a domain name in
     *                               the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to
     *                               allow_all to skip any checking. For the value browser_compatible the hostname verifier
     *                               works the same way as Curl and Firefox. The hostname must match either the first CN, or any of
     *                               the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only
     *                               difference between browser_compatible and strict is that a wildcard (such as *.foo.com)
     *                               with browser_compatible matches all subdomains, including a.b.foo.com
     *                               Default: "strict"
     * @param trustKeystore          Optional - The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with, or from Certificate Authorities
     *                               that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https'
     *                               or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS);
     * @param trustPassword          Optional - The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied
     * @param connectTimeout         Optional - The time to wait for a connection to be established in seconds. A timeout value of '0' represents an infinite timeout
     *                               Default: 10000
     * @param socketTimeout          Optional - The timeout for waiting for data (a maximum period " +
     *                               inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' represents an infinite timeout
     *                               Default: 0
     * @param keepAlive              Optional - Specifies whether to create a shared connection that will be used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
     *                               execution it will close it
     *                               Default: true
     * @param connectionsMaxPerRoute Optional - The maximum limit of connections on a per route basis
     *                               Default: 2
     * @param connectionsMaxTotal    Optional - The maximum limit of connections in total
     *                               Default: 20
     * @param responseCharacterSet   Optional - The character encoding to be used for the HTTP response. If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used.If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
     *                               default value will be used. You should not use this for method=HEAD or OPTIONS.
     *                               Default : UTF-8
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */

    @Action(name = CREATE_WORKSPACE_OPERATION_NAME,
            description = CREATE_WORKSPACE_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = CREATE_WORKSPACE_RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = CREATE_WORKSPACE_EXCEPTION_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC),
                    @Output(value = WORKSPACE_ID, description = WORKSPACE_ID_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = VARIABLE_NAME, description = VARIABLE_NAME_DESC) String variableName,
                                       @Param(value = VARIABLE_VALUE, description = VARIABLE_VALUE_DESC) String variableValue,
                                       @Param(value = VARIABLE_CATEGORY, description = VARIABLE_CATEGORY_DESC) String variableCategory,
                                       @Param(value = SENSITIVE, description = SENSITIVE_DESC) boolean sensitive,
                                       @Param(value = HCL, description = HCL_DESC) boolean hcl,
                                       @Param(value = WORKSPACE_ID, description = WORKSPACE_ID_DESC) String workspaceId,
                                       @Param(value = VARIABLE_REQUEST_BODY, description = VARIABLE_REQUEST_BODY_DESC) String requestBody,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
                                       @Param(value = X509_HOSTNAME_VERIFIER, description = X509_DESC) String x509HostnameVerifier,
                                       @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true, description = TRUST_PASSWORD_DESC) String trustPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = SOCKET_TIMEOUT, description = SOCKET_TIMEOUT_DESC) String socketTimeout,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSC_CHARACTER_SET_DESC) String responseCharacterSet) {
        authToken = defaultIfEmpty(authToken, EMPTY);
        variableName = defaultIfEmpty(variableName, EMPTY);
        variableValue = defaultIfEmpty(variableValue, EMPTY);
        variableCategory = defaultIfEmpty(variableCategory, EMPTY);
        workspaceId = defaultIfEmpty(workspaceId, BOOLEAN_TRUE);
        requestBody = defaultIfEmpty(requestBody, EMPTY);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGEIT);
        connectTimeout = defaultIfEmpty(connectTimeout, ZERO);
        socketTimeout = defaultIfEmpty(socketTimeout, ZERO);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);


        try {
            final Map<String, String> result = createVariable(CreateVariableInputs.builder()
                    .variableName(variableName)
                    .variableValue(variableValue)
                    .variableCategory(variableCategory)
                    .sensitive(sensitive)
                    .hcl(hcl)
                    .workspaceId(workspaceId)
                    .commonInputs(Inputs.builder()
                            .authToken(authToken)
                            .requestBody(requestBody)
                            .proxyHost(proxyHost)
                            .proxyPort(proxyPort)
                            .proxyUsername(proxyUsername)
                            .proxyPassword(proxyPassword)
                            .trustAllRoots(trustAllRoots)
                            .x509HostnameVerifier(x509HostnameVerifier)
                            .trustKeystore(trustKeystore)
                            .trustPassword(trustPassword)
                            .connectTimeout(connectTimeout)
                            .socketTimeout(socketTimeout)
                            .keepAlive(keepAlive)
                            .connectionsMaxPerRoot(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .responseCharacterSet(responseCharacterSet)
                            .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);
            return getOperationResults(result, returnMessage, returnMessage, returnMessage);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
