package io.cloudslang.content.jclouds.utilities;

import io.cloudslang.content.jclouds.entities.outputs.Outputs;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 7/13/2015.
 */
public final class ExceptionProcessor {

    public static Map<String, String> getExceptionResult(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Outputs.RETURN_RESULT, e.getMessage());
        returnResult.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
        returnResult.put(Outputs.EXCEPTION, eStr);
        return returnResult;
    }
}
