/*
 * Copyright 2021-2025 Open Text
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

package io.cloudslang.content.microsoftAD.services;

import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import io.cloudslang.content.microsoftAD.entities.GetUserInputs;
import io.cloudslang.content.microsoftAD.entities.GetUserLicenseDetailsInputs;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpCommons {

    private static final ConnectionKeepAliveStrategy keepAliveStrategy =
            (httpResponse, httpContext) -> TimeValue.MAX_VALUE;

    private static HostnameVerifier x509HostnameVerifier(String hostnameVerifier) {
        switch (hostnameVerifier.toLowerCase()) {
            case ALLOW_ALL:
                return NoopHostnameVerifier.INSTANCE;
            case STRICT:
            case BROWSER_COMPATIBLE:
            default:
                return new DefaultHostnameVerifier();
        }
    }

    private static SSLConnectionSocketFactory buildSslSocketFactory(AzureActiveDirectoryCommonInputs commonInputs) {
        HostnameVerifier hostnameVerifier = x509HostnameVerifier(commonInputs.getX509HostnameVerifier());
        try {
            SSLContext sslContext;
            if (commonInputs.getTrustAllRoots().equals(BOOLEAN_TRUE)) {
                sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
            } else {
                sslContext = new SSLContextBuilder().loadTrustMaterial(
                        new File(commonInputs.getTrustKeystore()),
                        commonInputs.getTrustPassword().toCharArray()).build();
            }
            return new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In HC5 5.6.1, SSL is configured via the connection manager (see createHttpClient).
     * HttpClientBuilder no longer exposes setSSLContext / setDefaultHostnameVerifier.
     * This method is kept for API compatibility but SSL must be applied through
     * PoolingHttpClientConnectionManagerBuilder.setSSLSocketFactory().
     */
//    public static void setSSLContext(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
//        // SSL is configured in createHttpClient via buildSslSocketFactory + PoolingHttpClientConnectionManagerBuilder
//    }

    public static void setProxy(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
        if (commonInputs.getProxyHost().equals(EMPTY))
            return;
        try {
            int proxyPort = Integer.parseInt(commonInputs.getProxyPort());
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(commonInputs.getProxyHost(), proxyPort),
                    new UsernamePasswordCredentials(
                            commonInputs.getProxyUsername(),
                            commonInputs.getProxyPassword().toCharArray()));

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setProxy(new HttpHost(commonInputs.getProxyHost(), proxyPort));

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTimeout(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofMilliseconds(Integer.parseInt(commonInputs.getConnectTimeout()) * 1000L))
                    .setResponseTimeout(Timeout.ofMilliseconds(Integer.parseInt(commonInputs.getSocketTimeout()) * 1000L))
                    .build();
            httpClientBuilder.setDefaultRequestConfig(requestConfig);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setMaxConnections(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
        if (commonInputs.getKeepAlive().equals(BOOLEAN_TRUE))
            httpClientBuilder.setKeepAliveStrategy(keepAliveStrategy);
        try {
            httpClientBuilder.setConnectionManager(
                    PoolingHttpClientConnectionManagerBuilder.create()
                            .setMaxConnTotal(Integer.parseInt(commonInputs.getConnectionsMaxTotal()))
                            .setMaxConnPerRoute(Integer.parseInt(commonInputs.getConnectionsMaxPerRoute()))
                            .build());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static CloseableHttpClient createHttpClient(AzureActiveDirectoryCommonInputs commonInputs) {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        setProxy(httpClientBuilder, commonInputs);
        setTimeout(httpClientBuilder, commonInputs);

        if (commonInputs.getKeepAlive().equals(BOOLEAN_TRUE))
            httpClientBuilder.setKeepAliveStrategy(keepAliveStrategy);

        try {
            httpClientBuilder.setConnectionManager(
                    PoolingHttpClientConnectionManagerBuilder.create()
                            .setSSLSocketFactory(buildSslSocketFactory(commonInputs))
                            .setMaxConnTotal(Integer.parseInt(commonInputs.getConnectionsMaxTotal()))
                            .setMaxConnPerRoute(Integer.parseInt(commonInputs.getConnectionsMaxPerRoute()))
                            .build());
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }

        return httpClientBuilder.build();
    }

    public static Map<String, String> httpPost(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {

        Map<String, String> result = new HashMap<>();
        try (CloseableHttpClient httpClient = createHttpClient(commonInputs)) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            httpPost.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                result.put(STATUS_CODE, response.getCode() + EMPTY);
                if (response.getEntity() != null)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), UTF8));
                return result;
            }
        } catch (Exception e) {
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(EXCEPTION, e.toString());
            return result;
        }
    }

    public static Map<String, String> httpDelete(AzureActiveDirectoryCommonInputs commonInputs, String url) {
        Map<String, String> result = new HashMap<>();
        result.put(STATUS_CODE, EMPTY);
        result.put(RETURN_RESULT, EMPTY);
        try (CloseableHttpClient httpClient = createHttpClient(commonInputs)) {
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpDelete.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                int statusCode = response.getCode();
                result.put(STATUS_CODE, statusCode + EMPTY);
                if (statusCode < 200 || statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), UTF8));
                return result;
            }
        } catch (Exception e) {
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(EXCEPTION, e.toString());
            return result;
        }
    }

    public static Map<String, String> httpGet(AzureActiveDirectoryCommonInputs commonInputs, String url) {
        Map<String, String> result = new HashMap<>();
        result.put(STATUS_CODE, EMPTY);
        result.put(RETURN_RESULT, EMPTY);
        try (CloseableHttpClient httpClient = createHttpClient(commonInputs)) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                result.put(STATUS_CODE, response.getCode() + EMPTY);
                result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), UTF8));
                return result;
            }
        } catch (Exception e) {
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(EXCEPTION, e.toString());
            return result;
        }
    }

    public static Map<String, String> httpPatch(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {
        Map<String, String> result = new HashMap<>();
        try (CloseableHttpClient httpClient = createHttpClient(commonInputs)) {
            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            httpPatch.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
            try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {
                int statusCode = response.getCode();
                if (statusCode < 200 || statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), UTF8));
                result.put(STATUS_CODE, statusCode + EMPTY);
                return result;
            }
        } catch (Exception e) {
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(EXCEPTION, e.toString());
            return result;
        }
    }
}
