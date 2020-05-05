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
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.http.client.utils.URIBuilder;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AbbyyAPI {

    private String lastStatusCode;
    private final AbbyyResponseParser responseParser;


    public AbbyyAPI() throws ParserConfigurationException {
        responseParser = new XmlResponseParser();
    }


    public String getLastStatusCode() {
        return lastStatusCode;
    }


    public AbbyyResponse postRequest(AbbyyRequest abbyyInitialRequest, String url) throws Exception {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(url)
                .authType(Headers.AUTH_TYPE_BASIC)
                .username(abbyyInitialRequest.getApplicationId())
                .password(abbyyInitialRequest.getPassword())
                .proxyHost(abbyyInitialRequest.getProxyHost())
                .proxyPort(abbyyInitialRequest.getProxyPort())
                .proxyUsername(abbyyInitialRequest.getProxyUsername())
                .proxyPassword(abbyyInitialRequest.getProxyPassword())
                .trustAllRoots(abbyyInitialRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInitialRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyInitialRequest.getTrustKeystore())
                .trustPassword(abbyyInitialRequest.getTrustPassword())
                .connectTimeout(abbyyInitialRequest.getConnectTimeout())
                .socketTimeout(abbyyInitialRequest.getSocketTimeout())
                .keepAlive(abbyyInitialRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInitialRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInitialRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyInitialRequest.getResponseCharacterSet())
                .sourceFile(abbyyInitialRequest.getSourceFile().getAbsolutePath())
                .conentType(Headers.CONTENT_TYPE_OCTET_STREAM)
                .method(HttpMethods.POST)
                .build();
        HttpClientResponse httpResponse = HttpSDK.execute(httpRequest);

        if (ReturnCodes.FAILURE.equals(httpResponse.getReturnCode()) || httpResponse.getStatusCode() == null) {
            throw new AbbyySdkException(httpResponse.getException());
        }
        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() == 401) {
            throw new ClientSideException(ExceptionMsgs.INVALID_CREDENTIALS);
        }

        return responseParser.parseResponse(httpResponse);
    }


    public AbbyyResponse getTaskStatus(AbbyyRequest abbyyInitialRequest, String taskId) throws Exception {
        String url = new URIBuilder()
                .setScheme(abbyyInitialRequest.getLocationId().getProtocol())
                .setHost(String.format(MiscConstants.HOST_TEMPLATE, abbyyInitialRequest.getLocationId(), Endpoints.GET_TASK_STATUS))
                .addParameter(QueryParams.TASK_ID, taskId)
                .build().toString();

        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(url)
                .authType(Headers.AUTH_TYPE_BASIC)
                .username(abbyyInitialRequest.getApplicationId())
                .password(abbyyInitialRequest.getPassword())
                .proxyHost(abbyyInitialRequest.getProxyHost())
                .proxyPort(abbyyInitialRequest.getProxyPort())
                .proxyUsername(abbyyInitialRequest.getProxyUsername())
                .proxyPassword(abbyyInitialRequest.getProxyPassword())
                .trustAllRoots(abbyyInitialRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInitialRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyInitialRequest.getTrustKeystore())
                .trustPassword(abbyyInitialRequest.getTrustPassword())
                .connectTimeout(abbyyInitialRequest.getConnectTimeout())
                .socketTimeout(abbyyInitialRequest.getSocketTimeout())
                .keepAlive(abbyyInitialRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInitialRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInitialRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyInitialRequest.getResponseCharacterSet())
                .sourceFile(abbyyInitialRequest.getSourceFile().getAbsolutePath())
                .conentType(Headers.CONTENT_TYPE_OCTET_STREAM)
                .method(HttpMethods.GET)
                .build();

        HttpClientResponse httpResponse = HttpSDK.execute(httpRequest);

        if (ReturnCodes.FAILURE.equals(httpResponse.getReturnCode()) || httpResponse.getStatusCode() == null) {
            throw new AbbyySdkException(httpResponse.getException());
        }
        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        return responseParser.parseResponse(httpResponse);
    }


    public String getResult(AbbyyRequest abbyyInitialRequest, String resultUrl, ExportFormat exportFormat,
                            String downloadPath, boolean useSpecificCharSet) throws AbbyySdkException, IOException {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(MiscConstants.ANONYMOUS_AUTH_TYPE)
                .proxyHost(abbyyInitialRequest.getProxyHost())
                .proxyPort(abbyyInitialRequest.getProxyPort())
                .proxyUsername(abbyyInitialRequest.getProxyUsername())
                .proxyPassword(abbyyInitialRequest.getProxyPassword())
                .trustAllRoots(abbyyInitialRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInitialRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyInitialRequest.getTrustKeystore())
                .trustPassword(abbyyInitialRequest.getTrustPassword())
                .connectTimeout(abbyyInitialRequest.getConnectTimeout())
                .socketTimeout(abbyyInitialRequest.getSocketTimeout())
                .keepAlive(abbyyInitialRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInitialRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInitialRequest.getConnectionsMaxTotal())
                .responseCharacterSet(useSpecificCharSet ? abbyyInitialRequest.getResponseCharacterSet() : null)
                .destinationFile(downloadPath)
                .method(HttpMethods.GET)
                .build();
        HttpClientResponse httpResponse = HttpSDK.execute(httpRequest);

        if (httpResponse.getStatusCode() == null || httpResponse.getStatusCode() != 200) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }
        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        return httpResponse.getReturnResult();
    }


    public long getResultSize(AbbyyRequest abbyyInitialRequest, String resultUrl, ExportFormat exportFormat)
            throws AbbyySdkException, IOException {
        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(MiscConstants.ANONYMOUS_AUTH_TYPE)
                .proxyHost(abbyyInitialRequest.getProxyHost())
                .proxyPort(abbyyInitialRequest.getProxyPort())
                .proxyUsername(abbyyInitialRequest.getProxyUsername())
                .proxyPassword(abbyyInitialRequest.getProxyPassword())
                .trustAllRoots(abbyyInitialRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInitialRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyInitialRequest.getTrustKeystore())
                .trustPassword(abbyyInitialRequest.getTrustPassword())
                .connectTimeout(abbyyInitialRequest.getConnectTimeout())
                .socketTimeout(abbyyInitialRequest.getSocketTimeout())
                .keepAlive(abbyyInitialRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInitialRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInitialRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyInitialRequest.getResponseCharacterSet())
                .method(HttpMethods.HEAD)
                .build();
        HttpClientResponse httpResponse = HttpSDK.execute(httpRequest);

        if (httpResponse.getStatusCode() == null || httpResponse.getStatusCode() != 200) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }
        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        try {
            return Long.parseLong(httpResponse.getResponseHeaders().getProperty(Headers.CONTENT_LENGTH));
        } catch (Exception ex) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.CONTENT_LENGTH_COULD_NOT_BE_RETRIEVED, exportFormat));
        }
    }


    public String getResultChunk(AbbyyRequest abbyyInitialRequest, String resultUrl, ExportFormat exportFormat,
                                 int startByteIndex, int endByteIndex) throws AbbyySdkException, IOException {
        String headers = String.format(Headers.RANGE_TEMPLATE, startByteIndex, endByteIndex);

        HttpClientRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .authType(MiscConstants.ANONYMOUS_AUTH_TYPE)
                .proxyHost(abbyyInitialRequest.getProxyHost())
                .proxyPort(abbyyInitialRequest.getProxyPort())
                .proxyUsername(abbyyInitialRequest.getProxyUsername())
                .proxyPassword(abbyyInitialRequest.getProxyPassword())
                .trustAllRoots(abbyyInitialRequest.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInitialRequest.getX509HostnameVerifier())
                .trustKeystore(abbyyInitialRequest.getTrustKeystore())
                .trustPassword(abbyyInitialRequest.getTrustPassword())
                .connectTimeout(abbyyInitialRequest.getConnectTimeout())
                .socketTimeout(abbyyInitialRequest.getSocketTimeout())
                .keepAlive(abbyyInitialRequest.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInitialRequest.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInitialRequest.getConnectionsMaxTotal())
                .responseCharacterSet(abbyyInitialRequest.getResponseCharacterSet())
                .method(HttpMethods.GET)
                .headers(headers)
                .build();
        HttpClientResponse httpResponse = HttpSDK.execute(httpRequest);

        if (httpResponse.getStatusCode() == null ||
                (httpResponse.getStatusCode() != 200 && httpResponse.getStatusCode() != 206)) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }
        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        return httpResponse.getReturnResult();
    }
}
