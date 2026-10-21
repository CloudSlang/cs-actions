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
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.DefaultHostnameVerifier;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpCommons {

    private static HostnameVerifier x509HostnameVerifier(String hostnameVerifier) {
        String x509HostnameVerifierStr = hostnameVerifier.toLowerCase();
        switch (x509HostnameVerifierStr) {
            case STRICT:
            case BROWSER_COMPATIBLE:
                return new DefaultHostnameVerifier();
            case ALLOW_ALL:
                return NoopHostnameVerifier.INSTANCE;
            default:
                return new DefaultHostnameVerifier();
        }
    }


    private static SSLConnectionSocketFactory createSSLSocketFactory(AzureActiveDirectoryCommonInputs commonInputs) {
        HostnameVerifier hostnameVerifier = x509HostnameVerifier(commonInputs.getX509HostnameVerifier());
        SSLContext sslContext;
        if (commonInputs.getTrustAllRoots().equals(BOOLEAN_TRUE)) {
            try {
                sslContext = new SSLContextBuilder()
                        .loadTrustMaterial(null, new TrustAllStrategy())
                        .build();
            } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                sslContext = new SSLContextBuilder().loadTrustMaterial(
                        new File(commonInputs.getTrustKeystore()),
                        commonInputs.getTrustPassword().toCharArray()).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                .setHostnameVerifier(hostnameVerifier)
                .build();
    }

    public static void setProxy(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        if (commonInputs.getProxyHost().equals(EMPTY))
            return;

        try {

            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            HttpHost proxy = new HttpHost(commonInputs.getProxyHost(), Integer.parseInt(commonInputs.getProxyPort()));
            credentialsProvider.setCredentials(
                    new AuthScope(proxy),
                    new UsernamePasswordCredentials(commonInputs.getProxyUsername(), commonInputs.getProxyPassword().toCharArray()));

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            httpClientBuilder.setProxy(proxy);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTimeout(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Timeout.ofSeconds(Long.parseLong(commonInputs.getConnectTimeout())))
                    .setResponseTimeout(Timeout.ofSeconds(Long.parseLong(commonInputs.getSocketTimeout())))
                    .build();

            httpClientBuilder.setDefaultRequestConfig(requestConfig);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setMaxConnections(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        if (commonInputs.getKeepAlive().equals(BOOLEAN_TRUE))
            httpClientBuilder.setKeepAliveStrategy((response, context) -> TimeValue.ofMilliseconds(Long.MAX_VALUE));
    }

    private static PoolingHttpClientConnectionManager createConnectionManager(AzureActiveDirectoryCommonInputs commonInputs) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", createSSLSocketFactory(commonInputs))
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setMaxTotal(Integer.parseInt(commonInputs.getConnectionsMaxTotal()));
        connectionManager.setDefaultMaxPerRoute(Integer.parseInt(commonInputs.getConnectionsMaxPerRoute()));
        return connectionManager;
    }

    public static CloseableHttpClient createHttpClient(AzureActiveDirectoryCommonInputs commonInputs) {

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(createConnectionManager(commonInputs));
        setProxy(httpClientBuilder, commonInputs);
        setTimeout(httpClientBuilder, commonInputs);
        setMaxConnections(httpClientBuilder, commonInputs);

        return httpClientBuilder.build();
    }

    public static Map<String, String> httpPost(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {

        Map<String, String> result = new HashMap<>();

        try (CloseableHttpClient httpClient = createHttpClient(commonInputs)) {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity stringEntity = new StringEntity(body, ContentType.create(APPLICATION_JSON, StandardCharsets.UTF_8));
            httpPost.setEntity(stringEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                result.put(STATUS_CODE, response.getCode() + EMPTY);
                if (!(response.getEntity() == null))
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
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
                result.put(STATUS_CODE, response.getCode() + EMPTY);
                //if status code is 204, no return result is provided, otherwise there is a return result
                int statusCode = response.getCode();
                if (statusCode < 200 || statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
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
                result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));
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

            StringEntity stringEntity = new StringEntity(body, ContentType.create(APPLICATION_JSON, StandardCharsets.UTF_8));

            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            httpPatch.setEntity(stringEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {

                int statusCode = response.getCode();
                if (statusCode < 200 || statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8));

                result.put(STATUS_CODE, response.getCode() + EMPTY);

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
