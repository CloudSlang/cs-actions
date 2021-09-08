/*
 * (c) Copyright 2019 Micro Focus, L.P.
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


package io.cloudslang.content.microsoftAD.services;

import com.sun.corba.se.impl.legacy.connection.DefaultSocketFactory;
import io.cloudslang.content.microsoftAD.entities.AzureActiveDirectoryCommonInputs;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import sun.security.ssl.SSLSocketFactoryImpl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpCommons {

    private final static HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AbstractVerifier() {
        @Override
        public void verify(String s, String[] strings, String[] strings1) throws SSLException {}
        public final String toString() { return "ALLOW_ALL"; }
    };

    private final static HostnameVerifier STRICT_HOSTNAME_VERIFIER = new AbstractVerifier() {
        @Override
        public void verify(String s, String[] strings, String[] strings1) throws SSLException {
            this.verify(s, strings, strings1, true);
        }
        public final String toString() { return "STRICT"; }
    };

    private final static HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = new AbstractVerifier() {
        @Override
        public void verify(String s, String[] strings, String[] strings1) throws SSLException {
            this.verify(s, strings, strings1, false);
        }
        public final String toString() { return "BROWSER_COMPATIBLE"; }
    };

    private static HostnameVerifier x509HostnameVerifier(String hostnameVerifier) {
        String x509HostnameVerifierStr = hostnameVerifier.toLowerCase();
        HostnameVerifier x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
        switch (x509HostnameVerifierStr) {
            case "strict":
                x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
                break;
            case "browser_compatible":
                x509HostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                break;
            case "allow_all":
                x509HostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                break;
        }
        return x509HostnameVerifier;
    }

    public static void setSSLContext(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        HostnameVerifier hostnameVerifier = x509HostnameVerifier(commonInputs.getX509HostnameVerifier());

        if (commonInputs.getTrustAllRoots().equals(BOOLEAN_TRUE)) {

            try {

                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) hostnameVerifier).build();
                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                //SSLSocketFactory sslSocketFactory = new SSLSocketFactory();
                httpClientBuilder.setSSLSocketFactory(socketFactory).build();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }

        } else {

            try {

                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                        new File(commonInputs.getTrustKeystore()),
                        commonInputs.getTrustPassword().toCharArray()).build();

                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
                httpClientBuilder.setSSLSocketFactory(socketFactory);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void setProxy(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        if (commonInputs.getTrustKeystore().equals(EMPTY))
            return;

        try {

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(commonInputs.getProxyHost(), Integer.parseInt(commonInputs.getProxyPort())),
                    new UsernamePasswordCredentials(commonInputs.getProxyUsername(), commonInputs.getProxyPassword()));

            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void setTimeout(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        if (commonInputs.getKeepAlive().equals(BOOLEAN_TRUE))
            httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        try {

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(Integer.parseInt(commonInputs.getConnectTimeout()))
                    .setSocketTimeout(Integer.parseInt(commonInputs.getSocketTimeout()))
                    .build();

            httpClientBuilder.setDefaultRequestConfig(requestConfig);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void setMaxConnections(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {

        try {

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(Integer.parseInt(commonInputs.getConnectionsMaxTotal()));
            connectionManager.setDefaultMaxPerRoute(Integer.parseInt(commonInputs.getConnectionsMaxPerRoute()));

            httpClientBuilder.setConnectionManager(connectionManager);

        } catch (NumberFormatException e) {
            e.printStackTrace();
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
            httpPost.setEntity(new StringEntity(body));

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {

                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);
                result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), commonInputs.getResponseCharacterSet()));

                return result;
            }
        } catch (IOException e) {

            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());

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
                if (statusCode <= 200 && statusCode > 300)
                    result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity(), commonInputs.getResponseCharacterSet()));
                return result;
            }
        } catch (IOException e) {
            return result;
        }
    }
}
