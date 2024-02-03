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


package io.cloudslang.content.httpclient.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.utils.ExecutionTimeout;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHost;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.httpclient.utils.Constants.ZERO;

public class HttpClientService {
    public static Map<String, String> execute(HttpClientInputs httpClientInputs) throws Exception {

        URI uri = UriBuilder.getUri(httpClientInputs);
        HttpUriRequestBase httpRequest = new HttpUriRequestBase(httpClientInputs.getMethod(), uri);
        SSLConnectionSocketFactory socketFactory = CustomSSLSocketFactory.createSSLSocketFactory(httpClientInputs);
        CustomConnectionManager customConnectionManager = new CustomConnectionManager();

        GlobalSessionObject globalSessionObject = httpClientInputs.getConnectionPoolSessionObject();
        if (globalSessionObject == null)
            customConnectionManager.setConnectionPoolHolder(new GlobalSessionObject());
        else
            customConnectionManager.setConnectionPoolHolder(httpClientInputs.getConnectionPoolSessionObject());

        String connectionKey = CustomConnectionManager.buildConnectionManagerMapKey(httpClientInputs.getTrustAllRoots(),
                httpClientInputs.getX509HostnameVerifier(),
                httpClientInputs.getKeystore(),
                httpClientInputs.getTrustKeystore());

        customConnectionManager.setConnectionManagerMapKey(connectionKey);


        SerializableSessionObject cookieStoreSessionObject = httpClientInputs.getCookieStoreSessionObject();
        if (cookieStoreSessionObject == null) {
            cookieStoreSessionObject = new SerializableSessionObject();
        }

        CookieStore cookieStore = CookieStoreBuilder.buildCookieStore(cookieStoreSessionObject, httpClientInputs.getUseCookies());

        PoolingHttpClientConnectionManager connectionManager = customConnectionManager.getConnectionManager(httpClientInputs, socketFactory, uri);

        CredentialsProvider credentialsProvider = CustomCredentialsProvider.getCredentialsProvider(httpClientInputs, uri);
        RequestConfig requestConfig = CustomRequestConfig.getDefaultRequestConfig(httpClientInputs);
        HttpClientContext context = CustomHttpClientContext.getHttpClientContext(httpClientInputs, credentialsProvider, uri);
        HttpEntity httpEntity = CustomEntity.getHttpEntity(httpClientInputs);
        httpRequest.setEntity(httpEntity);

        if (Boolean.parseBoolean(httpClientInputs.getPreemptiveAuth())) {
            AuthCache authCache = new BasicAuthCache();
            authCache.put(new HttpHost(uri.getScheme(), uri.getHost()), new BasicScheme());
            context.setAuthCache(authCache);
        }

        HeaderBuilder.headerBuiler(httpRequest, httpClientInputs);

        HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setDefaultCredentialsProvider(credentialsProvider)
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig);
        if (cookieStore != null)
            httpClientBuilder.setDefaultCookieStore(cookieStore);


        CloseableHttpClient httpclient = httpClientBuilder.build();

        Map<String, String> result = new HashMap<>();

        if (httpClientInputs.getExecutionTimeout().equals(ZERO))
            try (final CloseableHttpResponse response = httpclient.execute(httpRequest, context)) {
                ResponseHandler.consume(result, response, httpClientInputs.getResponseCharacterSet(), httpClientInputs.getDestinationFile());
                ResponseHandler.getResponseHeaders(result, response.getHeaders());
                ResponseHandler.getStatusResponse(result, response);
                ResponseHandler.getFinalLocationResponse(result, uri, context.getRedirectLocations().getAll());
            }
        else
            ExecutionTimeout.runWithTimeout(new Runnable() {
                @Override
                public void run() {
                    try (final CloseableHttpResponse response = httpclient.execute(httpRequest, context)) {
                        ResponseHandler.consume(result, response, httpClientInputs.getResponseCharacterSet(), httpClientInputs.getDestinationFile());
                        ResponseHandler.getResponseHeaders(result, response.getHeaders());
                        ResponseHandler.getStatusResponse(result, response);
                        ResponseHandler.getFinalLocationResponse(result, uri, context.getRedirectLocations().getAll());

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }, Integer.parseInt(httpClientInputs.getExecutionTimeout()), TimeUnit.SECONDS);


        if (cookieStore != null) {
            try {
                cookieStoreSessionObject.setValue(
                        CookieStoreBuilder.serialize(cookieStore));
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        result.put(RETURN_CODE, SUCCESS);
        return result;
    }
}
