/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.actions.scp;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.rft.entities.scp.SCPRemoteCopyFileInputs;
import io.cloudslang.content.rft.services.SCPService;
import io.cloudslang.content.utils.StringUtilities;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.rft.utils.Constants.*;
import static io.cloudslang.content.rft.utils.Descriptions.CommonInputsDescriptions.*;
import static io.cloudslang.content.rft.utils.Descriptions.CommonInputsDescriptions.PROXY_PORT_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SCPDescriptions.*;
import static io.cloudslang.content.rft.utils.Descriptions.SCPDescriptions.FAILURE_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SCPDescriptions.RETURN_RESULT_DESC;
import static io.cloudslang.content.rft.utils.Descriptions.SCPDescriptions.SUCCESS_DESC;
import static io.cloudslang.content.rft.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.rft.utils.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.rft.utils.Inputs.SCPInputs.*;
import static io.cloudslang.content.rft.utils.InputsValidation.verifySCPRemoteCopyFileInputs;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class SCPRemoteCopyFile {
    @Action(name = "SCP Remote Copy File", description = SCP_REMOTE_COPY_FILE_ACTION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = SOURCE_HOST, description = SOURCE_HOST_DESC, required = true) String sourceHost,
                                       @Param(value = SOURCE_PORT, description = SOURCE_PORT_DESC) String sourcePort,
                                       @Param(value = SOURCE_USERNAME, description = SOURCE_USERNAME_DESC, required = true) String sourceUsername,
                                       @Param(value = SOURCE_PASSWORD, description = SOURCE_PASSWORD_DESC, encrypted = true) String sourcePassword,
                                       @Param(value = SOURCE_PRIVATE_KEY, description = SOURCE_PRIVATE_KEY_DESC) String sourcePrivateKey,
                                       @Param(value = SOURCE_PATH, description = SOURCE_PATH_DESC, required = true) String sourcePath,
                                       @Param(value = DESTINATION_HOST, description = DESTINATION_HOST_DESC, required = true) String destinationHost,
                                       @Param(value = DESTINATION_PORT, description = DESTINATION_PORT_DESC) String destinationPort,
                                       @Param(value = DESTINATION_USERNAME, description = DESTINATION_USERNAME_DESC, required = true) String destinationUsername,
                                       @Param(value = DESTINATION_PASSWORD, description = DESTINATION_PASSWORD_DESC, encrypted = true) String destinationPassword,
                                       @Param(value = DESTINATION_PRIVATE_KEY, description = DESTINATION_PRIVATE_KEY_DESC) String destinationPrivateKey,
                                       @Param(value = DESTINATION_PATH, description = DESTINATION_PATH_DESC, required = true) String destinationPath,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, description = PROXY_PASSWORD_DESC, encrypted = true) String proxyPassword,
                                       @Param(value = KNOWN_HOSTS_POLICY, description = KNOWN_HOSTS_POLICY_DESC) String knownHostsPolicy,
                                       @Param(value = KNOWN_HOSTS_PATH, description = KNOWN_HOSTS_PATH_DESC) String knownHostsPath,
                                       @Param(value = CONNECTION_TIMEOUT, description = CONNECTION_TIMEOUT_DESC) String connectionTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout) {

        connectionTimeout = defaultIfEmpty(connectionTimeout, DEFAULT_CONNECTION_TIMEOUT);
        executionTimeout = defaultIfEmpty(executionTimeout, DEFAULT_EXECUTION_TIMEOUT);
        proxyHost = defaultIfEmpty(proxyHost,EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, String.valueOf(DEFAULT_PROXY_PORT));
        sourcePort = defaultIfEmpty(sourcePort, String.valueOf(DEFAULT_PORT));
        destinationPort = defaultIfEmpty(destinationPort, String.valueOf(DEFAULT_PORT));
        knownHostsPolicy = defaultIfEmpty(knownHostsPolicy,DEFAULT_KNOWN_HOSTS_POLICY);
        knownHostsPath = defaultIfEmpty(knownHostsPath,DEFAULT_KNOWN_HOSTS_PATH.toString());
        destinationPrivateKey = defaultIfEmpty(destinationPrivateKey,EMPTY);
        sourcePrivateKey = defaultIfEmpty(sourcePrivateKey, EMPTY);

        final List<String> exceptionMessages = verifySCPRemoteCopyFileInputs(sourceHost,sourcePort,sourcePath,destinationHost,destinationPort,destinationPath,
                connectionTimeout, executionTimeout, knownHostsPolicy);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        SCPRemoteCopyFileInputs inputs = new SCPRemoteCopyFileInputs.SCPRemoteCopyFileInputsBuilder()
                .sourceHost(sourceHost)
                .sourcePort(sourcePort)
                .sourceUsername(sourceUsername)
                .sourcePassword(sourcePassword)
                .sourcePath(sourcePath)
                .sourcePrivateKey(sourcePrivateKey)
                .destinationHost(destinationHost)
                .destinationPort(destinationPort)
                .destinationUsername(destinationUsername)
                .destinationPassword(destinationPassword)
                .destinationPrivateKey(destinationPrivateKey)
                .destinationPath(destinationPath)
                .executionTimeout(executionTimeout)
                .connectionTimeout(connectionTimeout)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .knownHostsPath(knownHostsPath)
                .knownHostsPolicy(knownHostsPolicy)
                .build();
        return new SCPService().executeSCPRemoteCopyFile(inputs);
    }
}
