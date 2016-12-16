/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.utils;

import io.cloudslang.content.amazon.entities.constants.Outputs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

/**
 * Created by persdana
 * 7/13/2015.
 */
public final class ExceptionProcessor {
    public static Map<String, String> getExceptionResult(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String exceptionString = writer.toString().replace(EMPTY + (char) 0x00, EMPTY);

        return getResultsMap(exception, exceptionString);
    }

    private static Map<String, String> getResultsMap(Exception exception, String exceptionString) {
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Outputs.RETURN_RESULT, exception.getMessage());
        returnResult.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
        returnResult.put(Outputs.EXCEPTION, exceptionString);
        return returnResult;
    }
}
