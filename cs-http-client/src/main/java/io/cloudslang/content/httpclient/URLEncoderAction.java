/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.httpclient;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.httpclient.build.Utils;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class URLEncoderAction {

    public static final String RETURN_CODE = "returnCode";
    public static final String RETURN_RESULT = "returnResult";

    /**
     * This operation will percent encode (URL encode) the given text. This is used for encoding parts of a URI and for
     * the preparation of data of the application/x-www-form-urlencoded media type. This is conforming to RFC 3986.
     *
     * @param text         Any text like query or form values. Adding a whole URL will not work.
     * @param characterSet The character encoding used for URL encoding. Leave this UTF-8,
     *                     like the standard recommends and because the inputs are stored as UTF-8. Default value: UTF-8
     * @return a map containing the output of the operation. Keys present in the map are:
     * <br><br><b>returnResult</b>  - The percent-encoded 'text'.
     * In case of an error this output will contain the error message.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     */
    @Action(name = "URL Encoder",
            outputs = {
                    @Output(CSHttpClient.EXCEPTION),
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT)
            },
            responses = {
                    @Response(text = "success", field = RETURN_CODE, value = "0", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = "failure", field = RETURN_CODE, value = "-1", matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = "text", required = true) String text,
            @Param("characterSet") String characterSet) {

        try {
            if (StringUtils.isEmpty(characterSet)) {
                characterSet = Utils.DEFAULT_CHARACTER_SET;
            }
            String returnResult = URLEncoder.encode(text, characterSet);
            Map<String, String> result = new HashMap<>();
            result.put(RETURN_RESULT, returnResult);
            result.put(RETURN_CODE, "0");
            return result;
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RETURN_RESULT, message);
        returnResult.put(RETURN_CODE, "-1");
        returnResult.put("exception", eStr);
        return returnResult;
    }
}
