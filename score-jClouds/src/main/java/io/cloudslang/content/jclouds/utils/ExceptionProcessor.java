package io.cloudslang.content.jclouds.utils;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Outputs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 7/13/2015.
 */
public final class ExceptionProcessor {
    public static Map<String, String> getExceptionResult(Exception exception) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String exceptionString = writer.toString()
                .replace(Constants.Miscellaneous.EMPTY + (char) 0x00, Constants.Miscellaneous.EMPTY);

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Outputs.RETURN_RESULT, exception.getMessage());
        returnResult.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
        returnResult.put(Outputs.EXCEPTION, exceptionString);

        return returnResult;
    }
}