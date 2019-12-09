/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.httpclient.consume;

import io.cloudslang.content.httpclient.services.HttpClientService;
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
        returnResult.put(HttpClientService.STATUS_CODE, String.valueOf(statusLine));

        String protocolVersion = (this.statusLine != null && this.statusLine.getProtocolVersion() != null) ?
                this.statusLine.getProtocolVersion().toString() : "";
        returnResult.put(HttpClientService.PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (this.statusLine != null && this.statusLine.getReasonPhrase() != null) ?
                this.statusLine.getReasonPhrase() : "";
        returnResult.put(HttpClientService.REASON_PHRASE, reasonPhrase);
    }
}
