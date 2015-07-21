/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.services.actions;

import io.cloudslang.content.rft.entities.*;
import io.cloudslang.content.rft.services.SCPService;
import io.cloudslang.content.rft.services.impl.SCPServiceImpl;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class ScoreRemoteSecureCopyTo {

    public Map<String, String> execute(RemoteSecureCopyInputs remoteSecureCopyInputs){
        Map<String, String> returnResult = new HashMap<>();
        SCPService service;

        try {
            int destinationPortNumber = StringUtils.toInt(remoteSecureCopyInputs.getDestinatinPort(), Constants.DEFAULT_PORT);
            String knownHostsPolicy = StringUtils.toNotEmptyString(remoteSecureCopyInputs.getKnownHostsPath(), Constants.DEFAULT_KNOWN_HOSTS_POLICY);
            Path knownHostsPath = StringUtils.toPath(remoteSecureCopyInputs.getKnownHostsPath(), Constants.DEFAULT_KNOWN_HOSTS_PATH);

            ConnectionDetails connection = new ConnectionDetails(remoteSecureCopyInputs.getDestinationHost(), destinationPortNumber, remoteSecureCopyInputs.getDestinationUsername(), remoteSecureCopyInputs.getDestinationPassword());
            KeyFile keyFile = getKeyFile(remoteSecureCopyInputs.getDestinationPrivateKeyFile(), remoteSecureCopyInputs.getDestinationPassword());
            KnownHostsFile knownHostsFile = new KnownHostsFile(knownHostsPath, knownHostsPolicy);


            int timeout = StringUtils.toInt(remoteSecureCopyInputs.getTimeout(), Constants.DEFAULT_TIMEOUT);

            service = new SCPServiceImpl(connection, keyFile, knownHostsFile, timeout);

            Boolean successfullyCopied = service.copy(remoteSecureCopyInputs.getSourcePath(), remoteSecureCopyInputs.getDestinationPath());

            if(successfullyCopied){
                populateResult(returnResult, remoteSecureCopyInputs);
            }else{
                populateResult(returnResult, Constants.NO_ACK_RECEIVED);
            }

        } catch (Exception e) {
            populateResult(returnResult, e);
        }
        return returnResult;
    }

    private KeyFile getKeyFile(String privateKeyFile, String privateKeyPassPhrase) {
        KeyFile keyFile = null;
        if (privateKeyFile != null && !privateKeyFile.isEmpty()) {
            if (privateKeyPassPhrase != null && !privateKeyPassPhrase.isEmpty()) {
                keyFile = new KeyFile(privateKeyFile, privateKeyPassPhrase);
            } else {
                keyFile = new KeyFile(privateKeyFile);
            }
        }
        return keyFile;
    }

    private void populateResult(Map<String, String> returnResult, Throwable e) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, e.getMessage());
        returnResult.put(Constants.OutputNames.EXCEPTION, StringUtils.getStackTraceAsString(e));
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
    }

    private void populateResult(Map<String, String> returnResult, RemoteSecureCopyInputs remoteSecureCopyInputs){
        returnResult.put(Constants.OutputNames.RETURN_RESULT, "File " + remoteSecureCopyInputs.getSourcePath() + " successfully copied to path " +
        remoteSecureCopyInputs.getDestinationPath() + " on " + remoteSecureCopyInputs.getDestinationHost() );
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        returnResult.put(Constants.OutputNames.EXCEPTION, Constants.EMPTY_STRING);
    }

    private void populateResult(Map<String, String> returnResult, String errorMessage) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, errorMessage);
        returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        returnResult.put(Constants.OutputNames.EXCEPTION, errorMessage);
    }

}