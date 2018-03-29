/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
        returnResult.put(HttpClientService.RESPONSE_HEADERS, result.toString());
    }
}