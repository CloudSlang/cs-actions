/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.rft.remote_copy;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.rft.utils.Constants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.rft.utils.Descriptions.CommonInputsDescriptions.*;
import static io.cloudslang.content.rft.utils.Descriptions.RemoteCopyDescriptions.*;
import static io.cloudslang.content.rft.utils.Descriptions.SFTPDescriptions.*;
import static io.cloudslang.content.rft.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.rft.utils.Inputs.RemoteCopyInputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class RemoteCopyAction {

    @Action(name = "Remote Copy Action", description = REMOTE_COPY_ACTION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_REMOTE_COPY_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = SRC_HOST, description = SRC_HOST_DESC) String sourceHost,
                                       @Param(value = SRC_PORT, description = SRC_PORT_DESC) String sourcePort,
                                       @Param(value = SRC_USERNAME, description = SRC_USERNAME_DESC) String sourceUsername,
                                       @Param(value = SRC_PASSWORD, description = SRC_PASSWORD_DESC, encrypted = true) String sourcePassword,
                                       @Param(value = SRC_PRIVATE_KEY_FILE, description = SRC_PRIVATE_KEY_FILE_DESC) String sourcePrivateKeyFile,
                                       @Param(value = SRC_PATH, description = SRC_PATH_DESC) String sourcePath,
                                       @Param(value = SRC_PROTOCOL, description = SRC_PROTOCOL_DESC) String sourceProtocol,
                                       @Param(value = SRC_TIMEOUT, description = SRC_TIMEOUT_DESC) String sourceTimeout,
                                       @Param(value = SRC_CHARACTER_SET, description = SRC_CHARACTER_SET_DESC) String sourceCharacterSet,
                                       @Param(value = DEST_HOST, description = DEST_HOST_DESC) String destinationHost,
                                       @Param(value = DEST_PORT, description = DEST_PORT_DESC) String destinationPort,
                                       @Param(value = DEST_USERNAME, description = DEST_USERNAME_DESC) String destinationUsername,
                                       @Param(value = DEST_PASSWORD, description = DEST_PASSWORD_DESC, encrypted = true) String destinationPassword,
                                       @Param(value = DEST_PRIVATE_KEY_FILE, description = DEST_PRIVATE_KEY_FILE_DESC) String destinationPrivateKeyFile,
                                       @Param(value = DEST_PATH, description = DEST_PATH_DESC) String destinationPath,
                                       @Param(value = DEST_PROTOCOL, description = DEST_PROTOCOL_DESC) String destinationProtocol,
                                       @Param(value = DEST_TIMEOUT, description = DEST_TIMEOUT_DESC) String destinationTimeout,
                                       @Param(value = DEST_CHARACTER_SET, description = DEST_CHARACTER_SET_DESC) String destinationCharacterSet,
                                       @Param(value = FILE_TYPE, description = TYPE_DESC) String fileType,
                                       @Param(value = PASSIVE, description = PASSIVE_DESC) String passive,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, description = PROXY_PASSWORD_DESC, encrypted = true) String proxyPassword,
                                       @Param(value = CONNECTION_TIMEOUT, description = CONNECTION_TIMEOUT_DESC) String connectionTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String executionTimeout) {

        proxyPort = defaultIfEmpty(proxyPort, String.valueOf(DEFAULT_PROXY_PORT));
        passive = defaultIfEmpty(passive, String.valueOf(Boolean.FALSE));
//        connectionTimeout = defaultIfEmpty(connectionTimeout, Constants.CONNECTION_TIMEOUT);
//        executionTimeout = defaultIfEmpty(executionTimeout, Constants.EXECUTION_TIMEOUT);

        RemoteCopyInputs inputs = new RemoteCopyInputs.RemoteCopyBuilder()
                .sourceHost(sourceHost)
                .sourcePort(sourcePort)
                .sourceUsername(sourceUsername)
                .sourcePassword(sourcePassword)
                .sourcePrivateKeyFile(sourcePrivateKeyFile)
                .sourcePath(sourcePath)
                .sourceProtocol(sourceProtocol)
                .sourceTimeout(sourceTimeout)
                .sourceCharacterSet(sourceCharacterSet)
                .destinationHost(destinationHost)
                .destinationPort(destinationPort)
                .destinationUsername(destinationUsername)
                .destinationPassword(destinationPassword)
                .destinationPrivateKeyFile(destinationPrivateKeyFile)
                .destinationPath(destinationPath)
                .destinationProtocol(destinationProtocol)
                .destinationTimeout(destinationTimeout)
                .destinationCharacterSet(destinationCharacterSet)
                .fileType(fileType)
                .passive(passive)
                .build();

        return new RemoteCopyService().execute(inputs);
    }
}