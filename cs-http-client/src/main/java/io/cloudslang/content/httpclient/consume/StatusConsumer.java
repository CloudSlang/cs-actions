/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
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
import org.apache.hc.core5.http.HttpResponse;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 7/28/14
 */
public class StatusConsumer {
    private HttpResponse httpResponse;

    public StatusConsumer setStatusLine(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
        return this;
    }

    public void consume(Map<String, String> returnResult) {
        int statusCode = (this.httpResponse != null) ? this.httpResponse.getCode() : 0;
        returnResult.put(HttpClientService.STATUS_CODE, String.valueOf(statusCode));

        String protocolVersion = (this.httpResponse != null && this.httpResponse.getVersion() != null) ?
                this.httpResponse.getVersion().toString() : "";
        returnResult.put(HttpClientService.PROTOCOL_VERSION, protocolVersion);

        String reasonPhrase = (this.httpResponse != null && this.httpResponse.getReasonPhrase() != null) ?
                this.httpResponse.getReasonPhrase() : "";
        returnResult.put(HttpClientService.REASON_PHRASE, reasonPhrase);
    }
}
