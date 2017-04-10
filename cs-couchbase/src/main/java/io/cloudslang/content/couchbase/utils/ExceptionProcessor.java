/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class ExceptionProcessor {
    private ExceptionProcessor() {
        // prevent instantiation
    }

    public static Map<String, String> getExceptionResultsMap(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String exceptionString = writer.toString().replace(EMPTY + (char) 0x00, EMPTY);

        return getFailureResultsMap(exception, exceptionString);
    }

    private static Map<String, String> getFailureResultsMap(Exception exception, String exceptionString) {
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RETURN_RESULT, exception.getMessage());
        returnResult.put(RETURN_CODE, FAILURE);
        returnResult.put(EXCEPTION, exceptionString);

        return returnResult;
    }
}