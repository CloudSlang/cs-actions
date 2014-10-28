/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package com.hp.score.content.httpclient;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
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

    @Action(name = "URL Encoder",
            outputs = {
                    @Output(ScoreHttpClient.EXCEPTION),
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
                characterSet = "UTF-8";
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
