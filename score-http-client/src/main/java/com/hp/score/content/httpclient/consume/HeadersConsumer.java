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
package com.hp.score.content.httpclient.consume;

import com.hp.score.content.httpclient.ScoreHttpClient;
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