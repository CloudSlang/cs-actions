/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.httpclient.utils.UrlEncoderUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.util.Map;

import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.EXCEPTION;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.FAILURE;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.FAILURE_RETURN_CODE;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.RETURN_CODE;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.RETURN_RESULT;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.SUCCESS;
import static io.cloudslang.content.httpclient.entities.HttpClientOutputs.SUCCESS_RETURN_CODE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/15/14
 */
public class URLEncoderAction {


    /**
     * This operation will percent encode (URL encode) the given text. This is used for encoding parts of a URI and for
     * the preparation of data of the application/x-www-form-urlencoded media type. This is conforming to RFC 3986.
     *
     * @param text         Any text like query or form values. Adding a whole URL will not work.
     * @param characterSet The character encoding used for URL encoding. Leave this UTF-8,
     *                     like the standard recommends and because the inputs are stored as UTF-8.
     *                     Default value: UTF-8
     * @return A map containing the output of the operation. Keys present in the map are:
     * returnResult  - The percent-encoded 'text'.
     * returnCode    - 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception     - In case of success response, this result is empty. In case of failure response,
     *                 this result contains the java stack trace of the runtime exception.
     */
    @Action(name = "URL Encoder",
            outputs = {
                    @Output(EXCEPTION),
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = "text", required = true) String text,
            @Param("characterSet") String characterSet) {

        try {
            if (StringUtils.isEmpty(characterSet)) {
                characterSet = UrlEncoderUtils.DEFAULT_CHARACTER_SET;
            }
            return getSuccessResultsMap(URLEncoder.encode(text, characterSet));
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
