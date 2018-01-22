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

import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * User: Adina Tusa
 * Date: 8/19/14
 */
public class HeadersConsumerTest {
    private static final String RESPONSE_HEADERS = "responseHeaders";
    private HeadersConsumer headersConsumer;

    @Before
    public void setUp() {
        headersConsumer = new HeadersConsumer();
    }

    @Test
    public void consume() {
        Map<String, String> returnResult = new HashMap<>();
        List<Header> headers = new ArrayList<>();
        Header header1 = new HeaderEntity("name1", "value1");
        Header header2 = new HeaderEntity("name2", "value2");
        headers.add(header1);
        headers.add(header2);
        headersConsumer
                .setHeaders(headers.toArray(new Header[headers.size()]))
                .consume(returnResult);
        assertEquals(returnResult.get(RESPONSE_HEADERS), "name1:value1\r\nname2:value2");
    }

    @Test
    public void consumeWithEmptyHeaders() {
        Map<String, String> returnResult = new HashMap<>();
        headersConsumer
                .setHeaders(null)
                .consume(returnResult);
        assertEquals(returnResult.get(RESPONSE_HEADERS), "");
    }


}
