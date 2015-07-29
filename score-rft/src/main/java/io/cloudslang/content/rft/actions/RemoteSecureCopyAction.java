/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import io.cloudslang.content.rft.entities.RemoteSecureCopyInputs;
import io.cloudslang.content.rft.services.actions.ScoreRemoteSecureCopyTo;
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
     * @param srcHost The hostname or ip address of the source remote machine.
     * @param srcPath The path to the file that needs to be copied from the source remote machine.
     * @param srcPort The port number for running the command on the source remote machine.
     * @param srcUsername The username of the account on the source remote machine.
     * @param srcPassword The password of the user for the source remote machine.
     * @param srcPrivateKeyFile The path to the private key file (OpenSSH type) on the source machine.
     * @param destHost The hostname or ip address of the destination remote machine.
     * @param destPath The path to the location where the file will be copied on the destination remote machine.
     * @param destPort The port number for running the command on the destination remote machine.
     * @param destUsername The username of the account on the destination remote machine.
     * @param destPassword The password of the user for the destination remote machine.
     * @param destPrivateKeyFile The path to the private key file (OpenSSH type) on the destination machine.
     * @param knownHostsPolicy The policy used for managing known_hosts file. Valid values: allow, strict, add. Default value: allow
     * @param knownHostsPath The path to the known hosts file.
     * @param timeout Time in milliseconds to wait for the command to complete. Default value is 90000 (90 seconds)
     *
     * @return - a map containing the output of the operation. Keys present in the map are:
     *     <br><b>returnResult</b> - The primary output.
     *     <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     *     <br><b>exception</b> - the exception message if the operation goes to failure.
     *
     */

    @Action(name = "SCP Command",
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
            @Param(value = Constants.InputNames.SRC_HOST) String srcHost,
            @Param(value = Constants.InputNames.SRC_PATH, required = true) String srcPath,
            @Param(Constants.InputNames.SRC_PORT) String srcPort,
            @Param(Constants.InputNames.SRC_USERNAME) String srcUsername,
            @Param(value = Constants.InputNames.SRC_PASSWORD, encrypted = true) String srcPassword,
            @Param(Constants.InputNames.SRC_PRIVATE_KEY_FILE) String srcPrivateKeyFile,
            @Param(value = Constants.InputNames.DEST_HOST, required = true) String destHost,
            @Param(value = Constants.InputNames.DEST_PATH, required = true) String destPath,
            @Param(Constants.InputNames.DEST_PORT) String destPort,
            @Param(value = Constants.InputNames.DEST_USERNAME, required = true) String destUsername,
            @Param(value = Constants.InputNames.DEST_PASSWORD, encrypted = true) String destPassword,
            @Param(Constants.InputNames.DEST_PRIVATE_KEY_FILE) String destPrivateKeyFile,
            @Param(Constants.InputNames.KNOWN_HOSTS_POLICY) String knownHostsPolicy,
            @Param(Constants.InputNames.KNOWN_HOSTS_PATH) String knownHostsPath,
            @Param(Constants.InputNames.TIMEOUT) String timeout) {

        RemoteSecureCopyInputs remoteSecureCopyInputs = new RemoteSecureCopyInputs();
        remoteSecureCopyInputs.setSrcHost(srcHost);
        remoteSecureCopyInputs.setSrcPath(srcPath);
        remoteSecureCopyInputs.setSrcPort(srcPort);
        remoteSecureCopyInputs.setSrcPrivateKeyFile(srcPrivateKeyFile);
        remoteSecureCopyInputs.setSrcUsername(srcUsername);
        remoteSecureCopyInputs.setSrcPassword(srcPassword);
        remoteSecureCopyInputs.setDestHost(destHost);
        remoteSecureCopyInputs.setDestPath(destPath);
        remoteSecureCopyInputs.setDestPort(destPort);
        remoteSecureCopyInputs.setDestPrivateKeyFile(destPrivateKeyFile);
        remoteSecureCopyInputs.setDestUsername(destUsername);
        remoteSecureCopyInputs.setDestPassword(destPassword);
        remoteSecureCopyInputs.setKnownHostsPolicy(knownHostsPolicy);
        remoteSecureCopyInputs.setKnownHostsPath(knownHostsPath);
        remoteSecureCopyInputs.setTimeout(timeout);

        return new ScoreRemoteSecureCopyTo().execute(remoteSecureCopyInputs);

    }

}
