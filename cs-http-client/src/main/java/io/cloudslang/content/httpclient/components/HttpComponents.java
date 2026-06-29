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

package io.cloudslang.content.httpclient.components;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 10/1/14
 */
public class HttpComponents {
    private CloseableHttpClient closeableHttpClient;
    private HttpUriRequestBase httpRequestBase;
    private HttpClientContext httpClientContext;
    private PoolingHttpClientConnectionManager connManager;
    private CookieStore cookieStore;
    private URI uri;
    private HttpClientBuilder httpClientBuilder;

    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    public void setCloseableHttpClient(CloseableHttpClient closeableHttpClient) {
        this.closeableHttpClient = closeableHttpClient;
    }

    public HttpUriRequestBase getHttpRequestBase() {
        return httpRequestBase;
    }

    public void setHttpRequestBase(HttpUriRequestBase httpRequestBase) {
        this.httpRequestBase = httpRequestBase;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    public void setHttpClientContext(HttpClientContext httpClientContext) {
        this.httpClientContext = httpClientContext;
    }

    public PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }

    public void setConnManager(PoolingHttpClientConnectionManager connManager) {
        this.connManager = connManager;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookieStore) {
        this.cookieStore = cookieStore;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public void setHttpClientBuilder(HttpClientBuilder httpClientBuilder) {
        this.httpClientBuilder = httpClientBuilder;
    }

    public HttpClientBuilder getHttpClientBuilder() {
        return httpClientBuilder;
    }
}
