/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.rft.services;


import io.cloudslang.content.rft.entities.*;
import io.cloudslang.content.rft.utils.Constants;
import io.cloudslang.content.rft.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 7/21/2015
 *
 * @author lesant
 */
public class RemoteSecureCopyService {

    public Map<String, String> execute(RemoteSecureCopyInputs remoteSecureCopyInputs){

        Map<String, String> returnResult = new HashMap<>();
        String resultMessage, errorMessage, returnCode;

        try {

            SCPCopier copier = new SCPCopier(remoteSecureCopyInputs);

            boolean successfullyCopied = copier.copyFromRemoteToRemote();

            if(successfullyCopied){
                resultMessage = "File " + remoteSecureCopyInputs.getSrcPath() + " successfully copied to path " +
                        remoteSecureCopyInputs.getDestPath() + " on " + remoteSecureCopyInputs.getDestHost();
                errorMessage = Constants.EMPTY_STRING;
                returnCode = Constants.ReturnCodes.RETURN_CODE_SUCCESS;

            }else{
                resultMessage = Constants.NO_ACK_RECEIVED;
                errorMessage = Constants.NO_ACK_RECEIVED;
                returnCode = Constants.ReturnCodes.RETURN_CODE_FAILURE;
            }
            populateResult(returnResult, resultMessage, errorMessage, returnCode);

        } catch (Exception e) {
            populateResult(returnResult, e.getMessage(), StringUtils.getStackTraceAsString(e), Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }
        return returnResult;
    }

    private void populateResult(Map<String, String> returnResult, String resultMessage, String errorMessage, String returnCode){
        returnResult.put(Constants.OutputNames.RETURN_RESULT, resultMessage);
        returnResult.put(Constants.OutputNames.EXCEPTION, errorMessage);
        returnResult.put(Constants.OutputNames.RETURN_CODE, returnCode);
    }

}
