/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.rft.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import io.cloudslang.content.rft.entities.RemoteSecureCopyInputs;
import io.cloudslang.content.rft.services.RemoteSecureCopyService;
import io.cloudslang.content.rft.utils.Constants;


import java.util.Map;

/**
 * The operation copies a file on the remote machine using the SCP protocol.
 *
 * Date: 3/12/2015
 *
 * @author lesant
 */
public class RemoteSecureCopyAction {

    /**
     * Executes a Shell command(s) on the remote machine using the SSH protocol.
     *
     * @param sourceHost The hostname or ip address of the source remote machine.
     * @param sourcePath The path to the file that needs to be copied from the source remote machine.
     * @param sourcePort The port number for running the command on the source remote machine.
     * @param sourceUsername The username of the account on the source remote machine.
     * @param sourcePassword The password of the user for the source remote machine.
     * @param sourcePrivateKeyFile The path to the private key file (OpenSSH type) on the source machine.
     * @param destinationHost The hostname or ip address of the destination remote machine.
     * @param destinationPath The path to the location where the file will be copied on the destination remote machine.
     * @param destinationPort The port number for running the command on the destination remote machine.
     * @param destinationUsername The username of the account on the destination remote machine.
     * @param destinationPassword The password of the user for the destination remote machine.
     * @param destinationPrivateKeyFile The path to the private key file (OpenSSH type) on the destination machine.
     * @param knownHostsPolicy The policy used for managing known_hosts file. Valid values: allow, strict, add. Default value: strict
     * @param knownHostsPath The path to the known hosts file.
     * @param timeout Time in milliseconds to wait for the command to complete. Default value is 90000 (90 seconds)
     * @param proxyHost The HTTP proxy host
     * @param proxyPort The HTTP proxy port
     *
     * @return - a map containing the output of the operation. Keys present in the map are:
     *     <br><b>returnResult</b> - The primary output.
     *     <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     *     <br><b>exception</b> - the exception message if the operation goes to failure.
     *
     */

    @Action(name = "Remote Secure Copy",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> copyTo(
            @Param(value = Constants.InputNames.SOURCE_HOST) String sourceHost,
            @Param(value = Constants.InputNames.SOURCE_PATH, required = true) String sourcePath,
            @Param(Constants.InputNames.SOURCE_PORT) String sourcePort,
            @Param(Constants.InputNames.SOURCE_USERNAME) String sourceUsername,
            @Param(value = Constants.InputNames.SOURCE_PASSWORD, encrypted = true) String sourcePassword,
            @Param(Constants.InputNames.SOURCE_PRIVATE_KEY_FILE) String sourcePrivateKeyFile,
            @Param(value = Constants.InputNames.DESTINATION_HOST) String destinationHost,
            @Param(value = Constants.InputNames.DESTINATION_PATH, required = true) String destinationPath,
            @Param(Constants.InputNames.DESTINATION_PORT) String destinationPort,
            @Param(value = Constants.InputNames.DESTINATION_USERNAME) String destinationUsername,
            @Param(value = Constants.InputNames.DESTINATION_PASSWORD, encrypted = true) String destinationPassword,
            @Param(Constants.InputNames.DESTINATION_PRIVATE_KEY_FILE) String destinationPrivateKeyFile,
            @Param(Constants.InputNames.KNOWN_HOSTS_POLICY) String knownHostsPolicy,
            @Param(Constants.InputNames.KNOWN_HOSTS_PATH) String knownHostsPath,
            @Param(Constants.InputNames.TIMEOUT) String timeout,
            @Param(Constants.InputNames.PROXY_HOST) String proxyHost,
            @Param(Constants.InputNames.PROXY_PORT) String proxyPort) {

        RemoteSecureCopyInputs remoteSecureCopyInputs = new RemoteSecureCopyInputs(sourcePath, destinationHost, destinationPath, destinationUsername);
        remoteSecureCopyInputs.setSrcHost(sourceHost);
        remoteSecureCopyInputs.setSrcPort(sourcePort);
        remoteSecureCopyInputs.setSrcPrivateKeyFile(sourcePrivateKeyFile);
        remoteSecureCopyInputs.setSrcUsername(sourceUsername);
        remoteSecureCopyInputs.setSrcPassword(sourcePassword);
        remoteSecureCopyInputs.setDestPort(destinationPort);
        remoteSecureCopyInputs.setDestPrivateKeyFile(destinationPrivateKeyFile);
        remoteSecureCopyInputs.setDestPassword(destinationPassword);
        remoteSecureCopyInputs.setKnownHostsPolicy(knownHostsPolicy);
        remoteSecureCopyInputs.setKnownHostsPath(knownHostsPath);
        remoteSecureCopyInputs.setTimeout(timeout);
        remoteSecureCopyInputs.setProxyHost(proxyHost);
        remoteSecureCopyInputs.setProxyPort(proxyPort);

        return new RemoteSecureCopyService().execute(remoteSecureCopyInputs);

    }

}
