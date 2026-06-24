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

import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ProtocolVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
public class StatusConsumerTest {

    private StatusConsumer statusConsumer;

    @Before
    public void setUp() {
        statusConsumer = new StatusConsumer();
    }

    @Test
    public void consume() {
        Map<String, String> returnResult = new HashMap<>();
        HttpResponse response = mock(HttpResponse.class);
        when(response.getCode()).thenReturn(200);
        when(response.getVersion()).thenReturn(new ProtocolVersion("HTTP", 1, 1));
        when(response.getReasonPhrase()).thenReturn("OK");
        statusConsumer.setStatusLine(response).consume(returnResult);
    }

    @Test
    public void consumeWithNulls() {
        Map<String, String> returnResult = new HashMap<>();
        statusConsumer.setStatusLine(null).consume(returnResult);
    }
}
