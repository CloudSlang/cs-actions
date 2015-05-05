/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package org.openscore.content.rft.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import org.openscore.content.rft.entities.RemoteSecureCopyInputs;
import org.openscore.content.rft.services.actions.ScoreRemoteSecureCopyTo;
import org.openscore.content.rft.utils.Constants;


import java.util.Map;

/**
 * The operation copies a file on the remote machine using the SCP protocol.
 *
 * Date: 3/12/2015
 *
 * @author lesant
 */
public class RemoteSecureCopyAction {


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
            @Param(value = Constants.InputNames.SOURCE_HOST) String sourceHost,
            @Param(value = Constants.InputNames.SOURCE_PATH, required = true) String sourcePath,
            @Param(Constants.InputNames.SOURCE_PORT) String sourcePort,
            @Param(Constants.InputNames.SOURCE_PRIVATE_KEY_FILE) String sourcePrivateKeyFile,
            @Param(Constants.InputNames.SOURCE_USERNAME) String sourceUsername,
            @Param(value = Constants.InputNames.SOURCE_PASSWORD, encrypted = true) String sourcePassword,
            @Param(value = Constants.InputNames.DESTINATION_HOST, required = true) String destinationHost,
            @Param(value = Constants.InputNames.DESTINATION_PATH, required = true) String destinationPath,
            @Param(Constants.InputNames.DESTINATION_PORT) String destinationPort,
            @Param(Constants.InputNames.DESTINATION_PRIVATE_KEY_FILE) String destinationPrivateKeyFile,
            @Param(value = Constants.InputNames.DESTINATION_USERNAME, required = true) String destinationUsername,
            @Param(value = Constants.InputNames.DESTINATION_PASSWORD, encrypted = true) String destinationPassword) {

        RemoteSecureCopyInputs remoteSecureCopyInputs = new RemoteSecureCopyInputs();
        remoteSecureCopyInputs.setSourceHost(sourceHost);
        remoteSecureCopyInputs.setSourcePath(sourcePath);
        remoteSecureCopyInputs.setSourcePort(sourcePort);
        remoteSecureCopyInputs.setSourcePrivateKeyFile(sourcePrivateKeyFile);
        remoteSecureCopyInputs.setSourceUsername(sourceUsername);
        remoteSecureCopyInputs.setSourcePassword(sourcePassword);
        remoteSecureCopyInputs.setDestinationHost(destinationHost);
        remoteSecureCopyInputs.setDestinationPath(destinationPath);
        remoteSecureCopyInputs.setDestinatinPort(destinationPort);
        remoteSecureCopyInputs.setDestinationPrivateKeyFile(destinationPrivateKeyFile);
        remoteSecureCopyInputs.setDestinationUsername(destinationUsername);
        remoteSecureCopyInputs.setDestinationPassword(destinationPassword);
        return new ScoreRemoteSecureCopyTo().execute(remoteSecureCopyInputs);

    }

}


