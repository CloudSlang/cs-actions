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

package io.cloudslang.content.hashicorp.terraform.actions.workspaces;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.CreateWorkspaceInputs;
import io.cloudslang.content.hashicorp.terraform.utils.Inputs;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.hashicorp.terraform.entities.CreateWorkspaceInputs.*;
import static io.cloudslang.content.hashicorp.terraform.entities.CreateWorkspaceInputs.INGRESS_SUBMODULES;
import static io.cloudslang.content.hashicorp.terraform.services.WorkspaceImpl.createWorkspace;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.POLLING_INTERVAL_DEFAULT;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.CREATE_WORKSPACE_OPERATION_NAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.WORKSPACE_ID_JSON_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.ASYNC_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.CreateWorkspace.INGRESS_SUBMODULES_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.OAUTH_TOKEN_ID_DESCRIPTION;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.STATUS_CODE_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.ASYNC;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_HOST;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PASSWORD;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_PORT;
import static io.cloudslang.content.hashicorp.terraform.utils.Inputs.PROXY_USERNAME;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.CreateWorkspaceOutputs.WORKSPACE_ID;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.ListOAuthClientOutputs.OAUTH_TOKEN_ID;
import static io.cloudslang.content.httpclient.entities.HttpClientInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class CreateWorkspace {

    /**
     * Creates a workspace which represent running infrastructure managed by Terraform.
     *
     * @param authToken              required - authentication token used to connect to Terraform API.
     *
     * @param organizationName       required - The name of the Terraform organization.
     *
     * @param workspaceName          Optional - The name of the workspace, which can only include letters, numbers, -, and _.
     *                                          This will be used as an identifier and must be unique in the organization.
     *
     * @param workspaceDescription   Optional - A description of the workspace to be created.
     *
     * @param autoApply              Optional - Whether to automatically apply changes when a Terraform plan is successful, with some exceptions.
     *                               Default: false
     *
     * @param fileTriggersEnabled    Optional - Whether to filter runs based on the changed files in a VCS push. If enabled, the working-directory
     *                                          and trigger-prefixes describe a set of paths which must contain changes for a VCS
     *                                          push to trigger a run. If disabled, any push will trigger a run.
     *                               Default: true
     *
     * @param workingDirectory       Optional - A relative path that Terraform will execute within. This defaults to the root of your repository
     *                                         and is typically set to a subdirectory matching the environment when multiple environments exist within the same repository.
     *
     * @param triggerPrefixes        Optional - List of repository-root-relative paths which should be tracked for changes, in addition to the working directory.
     *
     * @param queueAllRuns           Optional - Whether runs should be queued immediately after workspace creation. When set to false, runs triggered by a VCS change
     *                                          will not be queued until at least one run is manually queued.
     *                               Default: false
     *
     * @param speculativeEnabled     Optional - Whether this workspace allows speculative plans. Setting this to false prevents Terraform Cloud from running plans
     *                                          on pull requests, which can improve security if the VCS repository is public or includes untrusted contributors.
     *                               Default: true
     *
     * @param ingressSubmodules      Optional - Whether submodules should be fetched when cloning the VCS repository.
     *                               Default: false
     *
     * @param vcsRepoId              Optional - A reference to your VCS repository in the format :org/:repo where :org and :repo refer to the organization and
     *                                          repository in your VCS provider..
     *
     * @param vcsBranchName          Optional - The repository branch that Terraform will execute from.
     *                                          If omitted or submitted as an empty string, this defaults to the repository's default branch (e.g. master).
     *
     * @param oauthTokenId           Optional - The VCS Connection (OAuth Connection + Token) to use. This ID can be obtained from the oauth-tokens endpoint.
     *
     * @param requestBody            Optional - The request body of the workspace.
     *
     * @param terraformVersion       Optional - The version of Terraform to use for this workspace. Upon creating a workspace,
     *                                          the latest version is selected unless otherwise specified (e.g. \"0.11.1\").
     *                               Default: 0.12.1
     *
     * @param proxyHost              Optional - proxy server used to connect to Terraform API. If empty no proxy will be used.
     *
     * @param proxyPort              Optional - proxy server port. You must either specify values for both proxyHost and
     *                                          proxyPort inputs or leave them both empty.
     *                               Default: 8080
     *
     * @param proxyUsername          Optional - proxy server user name.
     *
     * @param proxyPassword          Optional - proxy server password associated with the proxyUsername input value.
     *
     * @param trustAllRoots          Optional - Specifies whether to enable weak security over SSL/TSL.
     *                               Default: false
     *
     * @param x509HostnameVerifier   Optional - Specifies the way the server hostname must match a domain name in
     *                                          the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to
     *                                          allow_all to skip any checking. For the value browser_compatible the hostname verifier
     *                                          works the same way as Curl and Firefox. The hostname must match either the first CN, or any of
     *                                          the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only
     *                                          difference between browser_compatible and strict is that a wildcard (such as *.foo.com)
     *                                          with browser_compatible matches all subdomains, including a.b.foo.com
     *                               Default: "strict"
     *
     * @param trustKeystore          Optional - The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with, or from Certificate Authorities
     *                                          that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https'
     *                                          or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS);
     *
     * @param trustPassword          Optional - The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied
     *
     * @param connectTimeout         Optional - The time to wait for a connection to be established in seconds. A timeout value of '0' represents an infinite timeout
     *                               Default: 10000
     *
     * @param socketTimeout          Optional - The timeout for waiting for data (a maximum period inactivity between two consecutive data packets),
     *                                          in seconds. A socketTimeout value of '0' represents an infinite timeout
     *                               Default: 0
     *
     * @param executionTimeout       Optional - The amount of time (in milliseconds) to allow the client to complete the execution
     *                                          of an API call. A value of '0' disables this feature.
     *                               Default: 60000
     *
     * @param pollingInterval        Optional - The time, in seconds, to wait before a new request that verifies if the operation finished
     *                                          is executed.
     *                               Default: 1000
     *
     * @param async                  Optional - Whether to run the operation is async mode.
     *                               Default: false
     *
     * @param keepAlive              Optional - Specifies whether to create a shared connection that will be used in subsequent calls. If keepAlive is false, the already open connection will be used and after
     *                               execution it will close it
     *                               Default: true
     *
     * @param connectionsMaxPerRoute Optional - The maximum limit of connections on a per route basis
     *                               Default: 2
     *
     * @param connectionsMaxTotal    Optional - The maximum limit of connections in total
     *                               Default: 20
     *
     * @param responseCharacterSet   Optional - The character encoding to be used for the HTTP response. If responseCharacterSet is empty, the charset from the
     *                               'Content-Type' HTTP response header will be used.If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the
     *                               default value will be used. You should not use this for method=HEAD or OPTIONS.
     *                               Default : UTF-8
     *
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
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, encrypted = true, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = ORGANIZATION_NAME, required = true, description = ORGANIZATION_NAME_DESC) String organizationName,
                                       @Param(value = WORKSPACE_NAME, description = WORKSPACE_NAME_DESC) String workspaceName,
                                       @Param(value = WORKSPACE_DESCRIPTION, description = WORKSPACE_DESCRIPTION_DESC) String workspaceDescription,
                                       @Param(value = AUTO_APPLY, description = AUTO_APPLY_DESC) String autoApply,
                                       @Param(value = FILE_TRIGGERS_ENABLED, description = FILE_TRIGGERS_ENABLED_DESC) String fileTriggersEnabled,
                                       @Param(value = WORKING_DIRECTORY, description = WORKING_DIRECTORY_DESC) String workingDirectory,
                                       @Param(value = TRIGGER_PREFIXES, description = TRIGGER_PREFIXES_DESC) String triggerPrefixes,
                                       @Param(value = QUEUE_ALL_RUNS, description = QUEUE_ALL_RUNS_DESC) String queueAllRuns,
                                       @Param(value = SPECULATIVE_ENABLED, description = SPECULATIVE_ENABLED_DESC) String speculativeEnabled,
                                       @Param(value = INGRESS_SUBMODULES, description = INGRESS_SUBMODULES_DESC) String ingressSubmodules,
                                       @Param(value = VCS_REPO_ID, description = VCS_REPO_ID_DESC) String vcsRepoId,
                                       @Param(value = VCS_BRANCH_NAME, description = VCS_BRANCH_NAME_DESC) String vcsBranchName,
                                       @Param(value = OAUTH_TOKEN_ID, description = OAUTH_TOKEN_ID_DESCRIPTION) String oauthTokenId,
                                       @Param(value = TERRAFORM_VERSION, description = TERAAFORM_VERSION_DESC) String terraformVersion,
                                       @Param(value = REQUEST_BODY, description = WORKSPACE_REQUEST_BODY_DESC) String requestBody,
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
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout,
                                       @Param(value = POLLING_INTERVAL, description = POLLING_INTERVAL_DESC) String pollingInterval,
                                       @Param(value = ASYNC, description = ASYNC_DESC) String async,
                                       @Param(value = KEEP_ALIVE, description = KEEP_ALIVE_DESC) String keepAlive,
                                       @Param(value = CONNECTIONS_MAX_PER_ROUTE, description = CONN_MAX_ROUTE_DESC) String connectionsMaxPerRoute,
                                       @Param(value = CONNECTIONS_MAX_TOTAL, description = CONN_MAX_TOTAL_DESC) String connectionsMaxTotal,
                                       @Param(value = RESPONSE_CHARACTER_SET, description = RESPONSE_CHARACTER_SET_DESC) String responseCharacterSet) {
        authToken = defaultIfEmpty(authToken, EMPTY);
        organizationName = defaultIfEmpty(organizationName, EMPTY);
        workspaceName = defaultIfEmpty(workspaceName, EMPTY);
        workspaceDescription = defaultIfEmpty(workspaceDescription, EMPTY);
        autoApply = defaultIfEmpty(autoApply, BOOLEAN_FALSE);
        fileTriggersEnabled = defaultIfEmpty(fileTriggersEnabled, BOOLEAN_TRUE);
        workingDirectory = defaultIfEmpty(workingDirectory, EMPTY);
        triggerPrefixes = defaultIfEmpty(triggerPrefixes, EMPTY);
        queueAllRuns = defaultIfEmpty(queueAllRuns, BOOLEAN_FALSE);
        speculativeEnabled = defaultIfEmpty(speculativeEnabled, BOOLEAN_TRUE);
        ingressSubmodules = defaultIfEmpty(ingressSubmodules, BOOLEAN_FALSE);
        vcsRepoId = defaultIfEmpty(vcsRepoId, EMPTY);
        vcsBranchName = defaultIfEmpty(vcsBranchName, EMPTY);
        oauthTokenId = defaultIfEmpty(oauthTokenId, EMPTY);
        requestBody = defaultIfEmpty(requestBody, EMPTY);
        terraformVersion = defaultIfEmpty(terraformVersion, TERRAFORM_VERSION_CONSTANT);
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
        executionTimeout = defaultIfEmpty(executionTimeout, EXEC_TIMEOUT);
        pollingInterval = defaultIfEmpty(pollingInterval, POLLING_INTERVAL_DEFAULT);
        async = defaultIfEmpty(async, BOOLEAN_FALSE);
        keepAlive = defaultIfEmpty(keepAlive, BOOLEAN_TRUE);
        connectionsMaxPerRoute = defaultIfEmpty(connectionsMaxPerRoute, CONNECTIONS_MAX_PER_ROUTE_CONST);
        connectionsMaxTotal = defaultIfEmpty(connectionsMaxTotal, CONNECTIONS_MAX_TOTAL_CONST);
        responseCharacterSet = defaultIfEmpty(responseCharacterSet, UTF8);


        try {
            final Map<String, String> result = createWorkspace(CreateWorkspaceInputs.builder()
                    .workspaceName(workspaceName)
                    .workspaceDescription(workspaceDescription)
                    .autoApply(autoApply)
                    .fileTriggersEnabled(fileTriggersEnabled)
                    .workingDirectory(workingDirectory)
                    .triggerPrefixes(triggerPrefixes)
                    .queueAllRuns(queueAllRuns)
                    .speculativeEnabled(speculativeEnabled)
                    .ingressSubmodules(ingressSubmodules)
                    .vcsRepoId(vcsRepoId)
                    .vcsBranchName(vcsBranchName)
                    .oauthTokenId(oauthTokenId)
                    .commonInputs(Inputs.builder()
                            .organizationName(organizationName)
                            .authToken(authToken)
                            .terraformVersion(terraformVersion)
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
                            .executionTimeout(executionTimeout)
                            .pollingInterval(pollingInterval)
                            .async(async)
                            .keepAlive(keepAlive)
                            .connectionsMaxPerRoot(connectionsMaxPerRoute)
                            .connectionsMaxTotal(connectionsMaxTotal)
                            .responseCharacterSet(responseCharacterSet)
                            .build())
                    .build());

            final String returnMessage = result.get(RETURN_RESULT);
            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final Integer statusCode = Integer.parseInt(result.get(STATUS_CODE));
            if (statusCode >= 200 && statusCode < 300) {
                final String workspaceId = JsonPath.read(returnMessage, WORKSPACE_ID_JSON_PATH);
                if (!workspaceId.isEmpty()) {
                    results.put(WORKSPACE_ID, workspaceId);
                } else {
                    results.put(WORKSPACE_ID, EMPTY);
                }
            }
            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
