/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.azure.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.azure.utils.DateUtilities;
import io.cloudslang.content.azure.utils.InputsValidation;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.azure.services.AuthorizationTokenImpl.getToken;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.EXPIRY;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.IDENTIFIER;
import static io.cloudslang.content.azure.utils.AuthorizationInputNames.PRIMARY_OR_SECONDARY_KEY;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * Created by victor on 27.09.2016.
 */
public class GetSharedAccessKeyToken {

    /**
     * Generates the shared access key token for Azure API calls.
     *
     * @param identifier            The Identifier text box in the Credentials section of the Service Management API tab
     *                              of System Settings
     * @param primaryOrSecondaryKey The Primary Key or the Secondary Key in the Credentials section of the Service
     *                              Management API tab of System Settings
     * @param expiry                The expiration date and time for the access token, the value must be in the format "MM/DD/YYYY H:MM PM|AM"
     *                              Example: 08/04/2014 10:03 PM
     * @return The SharedAccessKey token for Azure
     */
    @Action(name = "Get shared access key token for Azure",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = IDENTIFIER, required = true) final String identifier,
                                       @Param(value = PRIMARY_OR_SECONDARY_KEY, required = true) final String primaryOrSecondaryKey,
                                       @Param(value = EXPIRY, required = true) final String expiry) {
        final List<String> exceptionMessages = InputsValidation.verifySharedAccessInputs(identifier, primaryOrSecondaryKey, expiry);
        if (!exceptionMessages.isEmpty()) {
            final String errorMessage = StringUtilities.join(exceptionMessages, System.lineSeparator());
            return getFailureResultsMap(errorMessage);
        }
        try {
            final Date expiryDate = DateUtilities.parseDate(expiry.trim());
            return getSuccessResultsMap(getToken(identifier.trim(), primaryOrSecondaryKey.trim(), expiryDate));
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
