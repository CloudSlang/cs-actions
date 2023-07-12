/*
 * Copyright 2021-2023 Open Text
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
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
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

    private static ConnectionKeepAliveStrategy keepAliveStrategy = (httpResponse, httpContext) -> Long.MAX_VALUE;

    private static HostnameVerifier x509HostnameVerifier(String hostnameVerifier) {
        String x509HostnameVerifierStr = hostnameVerifier.toLowerCase();
        HostnameVerifier x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
        switch (x509HostnameVerifierStr) {
            case STRICT:
                x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
                break;
            case BROWSER_COMPATIBLE:
                x509HostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                break;
            case ALLOW_ALL:
                x509HostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                break;
        }
        return x509HostnameVerifier;
    }


    public static void setSSLContext(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        HostnameVerifier hostnameVerifier = x509HostnameVerifier(commonInputs.getX509HostnameVerifier());

        if (commonInputs.getTrustAllRoots().equals(BOOLEAN_TRUE)) {

            try {

                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
                //SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, commonInputs.getTLSVersion,commonInputs.getCipherSuites,hostnameVerifier);
                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                httpClientBuilder.setSSLSocketFactory(socketFactory);

            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            } catch (KeyManagementException e) {
                throw new RuntimeException(e);
            }

        } else {

            try {

                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                        new File(commonInputs.getTrustKeystore()),
                        commonInputs.getTrustPassword().toCharArray()).build();

                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                //SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, commonInputs.getTLSVersion,commonInputs.getCipherSuites,hostnameVerifier);
                httpClientBuilder.setSSLSocketFactory(socketFactory);


            } catch (Exception e) {

                throw new RuntimeException(e);
            }

        }
    }

    public static void setProxy(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        if (commonInputs.getProxyHost().equals(EMPTY))
            return;

        try {

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(commonInputs.getProxyHost(), Integer.parseInt(commonInputs.getProxyPort())),
                    new UsernamePasswordCredentials(commonInputs.getProxyUsername(), commonInputs.getProxyPassword()));

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

            httpClientBuilder.setProxy(new HttpHost(commonInputs.getProxyHost(), Integer.parseInt(commonInputs.getProxyPort())));

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTimeout(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        try {

            //Timeout is specified in millis in the javadoc
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Integer.parseInt(commonInputs.getConnectTimeout()) * 1000)
                    .setSocketTimeout(Integer.parseInt(commonInputs.getSocketTimeout()) * 1000)
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

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(Integer.parseInt(commonInputs.getConnectionsMaxTotal()));
            connectionManager.setDefaultMaxPerRoute(Integer.parseInt(commonInputs.getConnectionsMaxPerRoute()));

            httpClientBuilder.setConnectionManager(connectionManager);

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpClient createHttpClient(AzureActiveDirectoryCommonInputs commonInputs) {

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        setProxy(httpClientBuilder, commonInputs);
        setSSLContext(httpClientBuilder, commonInputs);
        setTimeout(httpClientBuilder, commonInputs);
        setMaxConnections(httpClientBuilder, commonInputs);

        return httpClientBuilder.build();
    }

    public static Map<String, String> httpPost(AzureActiveDirectoryCommonInputs commonInputs, String url, String body) {

        Map<String, String> result = new HashMap<>();

        try (CloseableHttpClient httpClient = (CloseableHttpClient) createHttpClient(commonInputs)) {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            StringEntity stringEntity = new StringEntity(body, UTF8);
            stringEntity.setContentType(APPLICATION_JSON);
            httpPost.setEntity(stringEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);
                if (!(response.getEntity() == null))
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
        try (CloseableHttpClient httpClient = (CloseableHttpClient) createHttpClient(commonInputs)) {
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpDelete.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);
                //if status code is 204, no return result is provided, otherwise there is a return result
                int statusCode = response.getStatusLine().getStatusCode();
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
        try (CloseableHttpClient httpClient = (CloseableHttpClient) createHttpClient(commonInputs)) {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpGet.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);
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

        try (CloseableHttpClient httpClient = (CloseableHttpClient) createHttpClient(commonInputs)) {

            StringEntity stringEntity = new StringEntity(body, UTF8);
            stringEntity.setContentType(APPLICATION_JSON);

            HttpPatch httpPatch = new HttpPatch(url);
            httpPatch.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpPatch.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            httpPatch.setEntity(stringEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPatch)) {

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode < 200 || statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), UTF8));

                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);

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
