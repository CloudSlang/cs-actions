/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.constants.*;
import io.cloudslang.content.abbyy.entities.ExportFormat;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ClientSideException;
import io.cloudslang.content.abbyy.exceptions.HttpException;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AbbyyApiImpl implements AbbyyApi {

    private final AbbyyResponseParser responseParser;
    private String lastStatusCode;


    public AbbyyApiImpl() throws ParserConfigurationException {
        this(null);
    }


    public AbbyyApiImpl(AbbyyResponseParser responseParser) throws ParserConfigurationException {
        this.responseParser = responseParser != null ? responseParser : new AbbyyResponseParserImpl();
    }


    public String getLastStatusCode() {
        return lastStatusCode;
    }


    public AbbyyResponse postRequest(@NotNull AbbyyRequest abbyyRequest, @NotNull String url) throws Exception {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(url)
                .authType(Headers.AUTH_TYPE_BASIC)
                .username(abbyyRequest.getApplicationId())
                .password(abbyyRequest.getPassword())
                .proxyHost(abbyyRequest.getProxyHost())
                .proxyPort(abbyyRequest.getProxyPort())
                .proxyUsername(abbyyRequest.getProxyUsername())
                .proxyPassword(abbyyRequest.getProxyPassword())
                .trustAllRoots(abbyyRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyRequest.getTrustKeystore())
                .trustPassword(abbyyRequest.getTrustPassword())
                .connectTimeout(abbyyRequest.getConnectTimeout())
                .socketTimeout(abbyyRequest.getSocketTimeout())
                .keepAlive(abbyyRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyRequest.getResponseCharacterSet())
                .sourceFile(abbyyRequest.getSourceFile().getAbsolutePath())
                .conentType(Headers.CONTENT_TYPE_OCTET_STREAM)
                .method(HttpMethods.POST)
                .build();
        HttpClientResponse httpResponse = HttpApi.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() == 401) {
            throw new ClientSideException(ExceptionMsgs.INVALID_CREDENTIALS);
        }

        return responseParser.parseResponse(httpResponse);
    }


    public AbbyyResponse getTaskStatus(@NotNull AbbyyRequest abbyyRequest, @NotNull String taskId) throws Exception {
        String url = new URIBuilder()
                .setScheme(abbyyRequest.getLocationId().getProtocol())
                .setHost(String.format(Urls.HOST_TEMPLATE, abbyyRequest.getLocationId(), Endpoints.GET_TASK_STATUS))
                .addParameter(QueryParams.TASK_ID, taskId)
                .build().toString();

        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(url)
                .authType(Headers.AUTH_TYPE_BASIC)
                .username(abbyyRequest.getApplicationId())
                .password(abbyyRequest.getPassword())
                .proxyHost(abbyyRequest.getProxyHost())
                .proxyPort(abbyyRequest.getProxyPort())
                .proxyUsername(abbyyRequest.getProxyUsername())
                .proxyPassword(abbyyRequest.getProxyPassword())
                .trustAllRoots(abbyyRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyRequest.getTrustKeystore())
                .trustPassword(abbyyRequest.getTrustPassword())
                .connectTimeout(abbyyRequest.getConnectTimeout())
                .socketTimeout(abbyyRequest.getSocketTimeout())
                .keepAlive(abbyyRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyRequest.getResponseCharacterSet())
                .sourceFile(abbyyRequest.getSourceFile().getAbsolutePath())
                .conentType(Headers.CONTENT_TYPE_OCTET_STREAM)
                .method(HttpMethods.GET)
                .build();

        HttpClientResponse httpResponse = HttpApi.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        return responseParser.parseResponse(httpResponse);
    }


    public String getResult(@NotNull AbbyyRequest abbyyRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                            @Nullable String downloadPath, boolean useSpecificCharSet) throws AbbyySdkException, IOException, HttpException {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .proxyHost(abbyyRequest.getProxyHost())
                .proxyPort(abbyyRequest.getProxyPort())
                .proxyUsername(abbyyRequest.getProxyUsername())
                .proxyPassword(abbyyRequest.getProxyPassword())
                .trustAllRoots(abbyyRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyRequest.getTrustKeystore())
                .trustPassword(abbyyRequest.getTrustPassword())
                .connectTimeout(abbyyRequest.getConnectTimeout())
                .socketTimeout(abbyyRequest.getSocketTimeout())
                .keepAlive(abbyyRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyRequest.getConnectionsMaxTotal())
                .responseCharacterSet(useSpecificCharSet ? abbyyRequest.getResponseCharacterSet() : null)
                .destinationFile(downloadPath)
                .method(HttpMethods.GET)
                .build();
        HttpClientResponse httpResponse = HttpApi.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() != 200) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        return httpResponse.getReturnResult();
    }


    public long getResultSize(@NotNull AbbyyRequest abbyyRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat)
            throws AbbyySdkException, IOException, HttpException {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .proxyHost(abbyyRequest.getProxyHost())
                .proxyPort(abbyyRequest.getProxyPort())
                .proxyUsername(abbyyRequest.getProxyUsername())
                .proxyPassword(abbyyRequest.getProxyPassword())
                .trustAllRoots(abbyyRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyRequest.getTrustKeystore())
                .trustPassword(abbyyRequest.getTrustPassword())
                .connectTimeout(abbyyRequest.getConnectTimeout())
                .socketTimeout(abbyyRequest.getSocketTimeout())
                .keepAlive(abbyyRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyRequest.getResponseCharacterSet())
                .method(HttpMethods.HEAD)
                .build();
        HttpClientResponse httpResponse = HttpApi.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() != 200) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        try {
            return Long.parseLong(httpResponse.getResponseHeaders().getProperty(Headers.CONTENT_LENGTH));
        } catch (Exception ex) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.CONTENT_LENGTH_COULD_NOT_BE_RETRIEVED, exportFormat));
        }
    }


    public String getResultChunk(@NotNull AbbyyRequest abbyyRequest, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                                 int startByteIndex, int endByteIndex) throws AbbyySdkException, IOException, HttpException {
        if (startByteIndex < 0) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NEGATIVE_NUMBER, "startByteIndex"));
        }
        if (endByteIndex < 0) {
            throw new IllegalArgumentException(String.format(ExceptionMsgs.NEGATIVE_NUMBER, "endByteIndex"));
        }
        if (startByteIndex > endByteIndex) {
            throw new IllegalArgumentException(ExceptionMsgs.INVALID_INTERVAL);
        }

        String headers = String.format(Headers.RANGE_TEMPLATE, startByteIndex, endByteIndex);

        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .proxyHost(abbyyRequest.getProxyHost())
                .proxyPort(abbyyRequest.getProxyPort())
                .proxyUsername(abbyyRequest.getProxyUsername())
                .proxyPassword(abbyyRequest.getProxyPassword())
                .trustAllRoots(abbyyRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyRequest.getTrustKeystore())
                .trustPassword(abbyyRequest.getTrustPassword())
                .connectTimeout(abbyyRequest.getConnectTimeout())
                .socketTimeout(abbyyRequest.getSocketTimeout())
                .keepAlive(abbyyRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyRequest.getResponseCharacterSet())
                .method(HttpMethods.GET)
                .headers(headers)
                .build();
        HttpClientResponse httpResponse = HttpApi.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() != 200 && httpResponse.getStatusCode() != 206) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        return httpResponse.getReturnResult();
    }
}
