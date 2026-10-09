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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
@ExtendWith(MockitoExtension.class)
public class HttpResponseConsumerTest {

    private static final String CONTENT_TYPE = "text/plain;charset=UTF-8";
    private static final String RETURN_RESULT = "returnResult";
    private HttpResponseConsumer httpResponseConsumer;
    @Mock
    private HttpResponse httpResponseMock;
    private Map<String, String> result;

    @BeforeEach
    public void setUp() {
        httpResponseConsumer = new HttpResponseConsumer();
        result = new HashMap<>();
    }

    @Test
    public void consume() throws IOException {
        setHttpResponseEntity("text/plain;charset=", "doc");
        httpResponseConsumer
                .setHttpResponse(httpResponseMock)
                .setDestinationFile(null)
                .setResponseCharacterSet(null)
                .consume(result);
        assertEquals("doc", result.get(RETURN_RESULT));
    }

    @Test
    public void consumeWithContentType() throws IOException {
        setHttpResponseEntity(CONTENT_TYPE, "doc");
        httpResponseConsumer
                .setHttpResponse(httpResponseMock)
                .setDestinationFile(null)
                .setResponseCharacterSet(null)
                .consume(result);
        assertEquals("doc", result.get(RETURN_RESULT));
    }

    @Test
    public void consumeWithDestinationFile() throws Exception {
        String fileName = "http-response-consumer-test.txt";
        setHttpResponseEntity(CONTENT_TYPE, "file content");

        try {
            httpResponseConsumer
                    .setHttpResponse(httpResponseMock)
                    .setDestinationFile(fileName)
                    .setResponseCharacterSet(null)
                    .consume(result);

            assertEquals("file content", java.nio.file.Files.readString(new File(fileName).toPath()));
            assertNull(result.get(RETURN_RESULT));
        } finally {
            new File(fileName).delete();
        }
    }

    private void setHttpResponseEntity(String contentType, String content) {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream(content.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        Header contentTypeHeader = new HeaderEntity("Content-Type", contentType);
        entity.setContentType(contentTypeHeader);
        when(httpResponseMock.getEntity()).thenReturn(entity);
    }

}
