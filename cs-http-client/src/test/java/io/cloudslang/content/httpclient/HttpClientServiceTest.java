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





package io.cloudslang.content.httpclient;


import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.components.HttpComponents;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * User: bancl
 * Date: 10/16/2015
 */
@ExtendWith(MockitoExtension.class)
public class HttpClientServiceTest {

    private HttpClientService httpClientService;
    @Mock
    private HttpClientInputs httpClientInputs;
    @Mock
    private HttpRequestBase httpRequestBase;
    @Mock
    private HttpComponents httpComponents;
    @Mock
    private CloseableHttpClient closeableHttpClient;
    @Mock
    private HttpClientContext httpClientContext;
    @Mock
    private CloseableHttpResponse httpResponse;
    private String responseCharacterSet;
    private String destinationFile;
    @Mock
    private URI uri;
    @Mock
    private Map<String, String> result;
    @Mock
    private CookieStore cookieStore;
    @Mock
    private SerializableSessionObject serializableSessionObject;
    @Mock
    private PoolingHttpClientConnectionManager connManager;

    @BeforeEach
    public void setUp() {
        httpClientService = new HttpClientService() {
            @Override
            public HttpComponents buildHttpComponents(HttpClientInputs inputs) {
                return httpComponents;
            }

            @Override
            public CloseableHttpResponse execute(CloseableHttpClient client, HttpRequestBase request,
                                                HttpClientContext context) {
                return httpResponse;
            }

            @Override
            public Map<String, String> parseResponse(CloseableHttpResponse response, String characterSet,
                                                    String file, URI responseUri, HttpClientContext context,
                                                    CookieStore responseCookieStore,
                                                    SerializableSessionObject sessionObject) {
                return result;
            }
        };

        when(httpComponents.getHttpRequestBase()).thenReturn(httpRequestBase);
        when(httpComponents.getCloseableHttpClient()).thenReturn(closeableHttpClient);
        when(httpComponents.getHttpClientContext()).thenReturn(httpClientContext);
        when(httpComponents.getUri()).thenReturn(uri);
        when(httpComponents.getCookieStore()).thenReturn(cookieStore);
        when(httpComponents.getConnManager()).thenReturn(connManager);

        when(httpClientInputs.getExecutionTimeout()).thenReturn("0");
        when(httpClientInputs.getResponseCharacterSet()).thenReturn(responseCharacterSet);
        when(httpClientInputs.getDestinationFile()).thenReturn(destinationFile);
        when(httpClientInputs.getCookieStoreSessionObject()).thenReturn(serializableSessionObject);
    }

    @Test
    public void executeKeepAliveTrue() throws Exception {
        when(httpClientInputs.getKeepAlive()).thenReturn("true");
        Map<String, String> result1 = httpClientService.execute(httpClientInputs);
        assertEquals(result, result1);
        Mockito.verify(httpRequestBase, times(1)).releaseConnection();
    }

    @Test
    public void executeKeepAliveFalse() throws Exception {
        when(httpClientInputs.getKeepAlive()).thenReturn("false");
        Map<String, String> result1 = httpClientService.execute(httpClientInputs);
        assertEquals(result, result1);
        Mockito.verify(httpResponse, times(1)).close();
        Mockito.verify(httpRequestBase, times(1)).releaseConnection();
        Mockito.verify(connManager, times(1)).closeExpiredConnections();
    }
}
