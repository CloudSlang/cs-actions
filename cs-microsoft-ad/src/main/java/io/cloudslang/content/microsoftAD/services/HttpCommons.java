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
import com.google.gson.JsonObject;
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
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.microsoftAD.utils.Constants.*;
import static io.cloudslang.content.microsoftAD.utils.Constants.STATUS_CODE;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class HttpCommons {
    public static void setSSLContext(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
        if (commonInputs.getTrustKeystore() == EMPTY)
            return;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
                    new File(commonInputs.getTrustKeystore()),
                    commonInputs.getTrustPassword().toCharArray()).build();
            HostnameVerifier hostnameVerifier = new DefaultHostnameVerifier();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            httpClientBuilder.setSSLSocketFactory(socketFactory).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setProxy(HttpClientBuilder httpClientBuilder, AzureActiveDirectoryCommonInputs commonInputs) {
        if (commonInputs.getTrustKeystore() == EMPTY)
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
    public static HttpClient createHttpClient(AzureActiveDirectoryCommonInputs commonInputs) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Integer.parseInt(commonInputs.getConnectTimeout()))
                .setSocketTimeout(Integer.parseInt(commonInputs.getSocketTimeout()))
                .build();
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig);
        setProxy(httpClientBuilder, commonInputs);
        setSSLContext(httpClientBuilder, commonInputs);
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
                result.put(RETURN_RESULT, EntityUtils.toString(response.getEntity()));
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
        try (CloseableHttpClient httpClient = (CloseableHttpClient) createHttpClient(commonInputs)) {
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, BEARER + commonInputs.getAuthToken());
            httpDelete.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
            try (CloseableHttpResponse response = httpClient.execute(httpDelete)) {
                result.put(STATUS_CODE, response.getStatusLine().getStatusCode() + EMPTY);
                return result;
            }
        } catch (IOException e) {
            result.put(STATUS_CODE, EMPTY);
            result.put(RETURN_RESULT, e.getMessage());
            return result;
        }
    }
}
