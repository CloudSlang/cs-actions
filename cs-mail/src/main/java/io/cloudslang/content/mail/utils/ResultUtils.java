package io.cloudslang.content.mail.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public final class ResultUtils {

    public static Map<String, String> fromException(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String exStr = writer.toString().replace(Constants.Strings.EMPTY + (char) 0x00, Constants.Strings.EMPTY);

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Constants.Outputs.RETURN_RESULT, e.getMessage());
        returnResult.put(Constants.Outputs.RETURN_CODE, Constants.ReturnCodes.FAILURE_RETURN_CODE);
        returnResult.put(Constants.Outputs.EXCEPTION, exStr);
        return returnResult;
    }
}
