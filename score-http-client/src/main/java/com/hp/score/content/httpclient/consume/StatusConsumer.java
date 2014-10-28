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
import org.apache.http.StatusLine;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/28/14
 */
public class StatusConsumer {
    private StatusLine statusLine;

    public StatusConsumer setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        int statusLine = (this.statusLine != null) ? this.statusLine.getStatusCode() : 0;
        returnResult.put(ScoreHttpClient.STATUS_CODE, String.valueOf(statusLine));

        String protocolVersion = (this.statusLine != null && this.statusLine.getProtocolVersion() != null) ?
                this.statusLine.getProtocolVersion().toString() : "";
        returnResult.put(ScoreHttpClient.PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (this.statusLine != null && this.statusLine.getReasonPhrase() != null) ?
                this.statusLine.getReasonPhrase() : "";
        returnResult.put(ScoreHttpClient.REASON_PHRASE, reasonPhrase);
    }
}
