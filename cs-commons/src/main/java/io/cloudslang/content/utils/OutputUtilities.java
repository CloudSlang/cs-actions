/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utils;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by victor on 05.09.2016.
 */
public final class OutputUtilities {

    private OutputUtilities() {
    }

    /**
     * Creates a Map<String, String> with the RETURN_CODE Success(0), and the RETURN_RESULT the returnResult string value
     *
     * @param returnResult the return result
     * @return a Map<String, String> with the RETURN_CODE Success(0), and the RETURN_RESULT the returnResult string value
     */
    @NotNull
    public static Map<String, String> getSuccessResultsMap(@NotNull final String returnResult) {
        final Map<String, String> results = new HashMap<>();
        results.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
        results.put(OutputNames.RETURN_RESULT, returnResult);
        return results;
    }

    /**
     * Creates a Map<String, String> with the RETURN_CODE Failure(-1), and the RETURN_RESULT and EXCEPTION the <errorMessage> string value
     *
     * @param errorMessage the RETURN_RESULT and EXCEPTION for the map
     * @return a Map<String, String> with the RETURN_CODE Failure(-1), and the RETURN_RESULT the returnResult string value
     */
    @NotNull
    public static Map<String, String> getFailureResultsMap(@NotNull final String errorMessage) {
        final Map<String, String> results = new HashMap<>();
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(OutputNames.RETURN_RESULT, errorMessage);
        results.put(OutputNames.EXCEPTION, errorMessage);
        return results;
    }

    /**
     * Creates a Map<String, String> with the RETURN_CODE Failure(-1), RETURN_RESULT the <throwable> message and with EXCEPTION the full stackTrace of the <throwable>
     *
     * @param throwable Exception with the message for the RETURN_RESULT and the fullStackTrace for the EXCEPTION of the map
     * @return a Map<String, String> with the RETURN_CODE Failure(-1), RETURN_RESULT the <throwable> message and with EXCEPTION the full stackTrace of the <throwable>
     */
    @NotNull
    public static Map<String, String> getFailureResultsMap(@NotNull final Throwable throwable) {
        final Map<String, String> results = new HashMap<>();
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(OutputNames.RETURN_RESULT, throwable.getMessage());
        results.put(OutputNames.EXCEPTION, ExceptionUtils.getStackTrace(throwable));
        return results;
    }

    /**
     * Creates a Map<String, String> with the RETURN_CODE Failure(-1), RETURN_RESULT the <throwable> message and with EXCEPTION the full stackTrace of the <throwable>
     *
     * @param returnResult a specific error message
     * @param throwable Exception with the message for the RETURN_RESULT and the fullStackTrace for the EXCEPTION of the map
     * @return a Map<String, String> with the RETURN_CODE Failure(-1), RETURN_RESULT the <throwable> message and with EXCEPTION the full stackTrace of the <throwable>
     */
    @NotNull
    public static Map<String, String> getFailureResultsMap(@NotNull final String returnResult, @NotNull final Throwable throwable) {
        final Map<String, String> results = new HashMap<>();
        results.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        results.put(OutputNames.RETURN_RESULT, returnResult);
        results.put(OutputNames.EXCEPTION, ExceptionUtils.getStackTrace(throwable));
        return results;
    }
}
