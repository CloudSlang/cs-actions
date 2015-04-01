/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.content.httpclient.consume;

import io.cloudslang.content.httpclient.ScoreHttpClient;
import org.apache.http.Header;

import java.util.Map;

public class HeadersConsumer {
    private Header[] headers;

    public HeadersConsumer setHeaders(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        StringBuilder result = new StringBuilder();
        if (headers != null) {
            for (Header header : headers) {
                result.append(header.toString()).append("\r\n");
            }
            if (result.length() != 0) {
                result.delete(result.length() - 2, result.length());
            }
        }
        returnResult.put(ScoreHttpClient.RESPONSE_HEADERS, result.toString());
    }
}