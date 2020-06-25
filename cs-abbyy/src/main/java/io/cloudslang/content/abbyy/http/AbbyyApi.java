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
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.requests.HttpClientRequest;
import io.cloudslang.content.abbyy.entities.requests.HttpRequest;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ClientSideException;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class AbbyyApi {

    private final AbbyyResponseParser responseParser;
    private String lastStatusCode;


    public AbbyyApi() throws ParserConfigurationException {
        this(null);
    }


    public AbbyyApi(AbbyyResponseParser responseParser) throws ParserConfigurationException {
        this.responseParser = responseParser != null ? responseParser : new AbbyyResponseParser();
    }


    public String getLastStatusCode() {
        return lastStatusCode;
    }


    public AbbyyResponse request(@NotNull AbbyyInput abbyyInput) throws Exception {

        HttpRequest httpRequest = new HttpClientRequest.Builder()
                .url(abbyyInput.getUrl())
                .tlsVersion(abbyyInput.getTlsVersion())
                .allowedCyphers(abbyyInput.getAllowedCyphers())
                .authType(abbyyInput.getAuthType())
                .preemptiveAuth(abbyyInput.isPreemptiveAuth())
                .username(abbyyInput.getUsername())
                .password(abbyyInput.getPassword())
                .kerberosConfigFile(abbyyInput.getKerberosConfigFile())
                .kerberosLoginConfFile(abbyyInput.getKerberosLoginConfFile())
                .kerberosSkipPortForLookup(abbyyInput.getKerberosSkipPortForLookup())
                .proxyHost(abbyyInput.getProxyHost())
                .proxyPort(abbyyInput.getProxyPort())
                .proxyUsername(abbyyInput.getProxyUsername())
                .proxyPassword(abbyyInput.getProxyPassword())
                .trustAllRoots(abbyyInput.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInput.getX509HostnameVerifier())
                .trustKeystore(abbyyInput.getTrustKeystore())
                .trustPassword(abbyyInput.getTrustPassword())
                .keystore(abbyyInput.getKeystore())
                .keystorePassword(abbyyInput.getKeystorePassword())
                .connectTimeout(abbyyInput.getConnectTimeout())
                .socketTimeout(abbyyInput.getSocketTimeout())
                .useCookies(abbyyInput.isUseCookies())
                .keepAlive(abbyyInput.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInput.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInput.getConnectionsMaxTotal())
                .responseCharacterSet(StandardCharsets.UTF_8.displayName())
                .headers(abbyyInput.getHeaders())
                .followRedirects(abbyyInput.isFollowRedirects())
                .queryParams(abbyyInput.getQueryParams())
                .queryParamsAreURLEncoded(abbyyInput.isQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(abbyyInput.isQueryParamsAreFormEncoded())
                .formParams(abbyyInput.getFormParams())
                .formParamsAreURLEncoded(abbyyInput.isFormParamsAreURLEncoded())
                .sourceFile(abbyyInput.getSourceFile())
                .body(abbyyInput.getBody())
                .contentType(abbyyInput.getContentType())
                .requestCharacterSet(abbyyInput.getRequestCharacterSet())
                .multipartBodies(abbyyInput.getMultipartBodies())
                .multipartBodiesContentType(abbyyInput.getMultipartBodiesContentType())
                .multipartFiles(abbyyInput.getMultipartFiles())
                .multipartFilesContentType(abbyyInput.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(abbyyInput.isMultipartValuesAreURLEncoded())
                .chunkedRequestEntity(abbyyInput.isChunkedRequestEntity())
                .method(abbyyInput.getMethod())
                .build();

        HttpClientResponse httpResponse = HttpClient.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() == 401) {
            throw new ClientSideException(ExceptionMsgs.INVALID_CREDENTIALS);
        }

        return responseParser.parseResponse(httpResponse);
    }


    public AbbyyResponse getTaskStatus(@NotNull AbbyyInput abbyyInput, @NotNull String taskId) throws Exception {
        String url = new URIBuilder()
                .setScheme(abbyyInput.getLocationId().getProtocol())
                .setHost(String.format(Urls.HOST_TEMPLATE, abbyyInput.getLocationId(), Endpoints.GET_TASK_STATUS))
                .addParameter(QueryParams.TASK_ID, taskId)
                .build().toString();

        HttpRequest httpRequest = new HttpClientRequest.Builder()
                .url(url)
                .tlsVersion(abbyyInput.getTlsVersion())
                .allowedCyphers(abbyyInput.getAllowedCyphers())
                .authType(abbyyInput.getAuthType())
                .preemptiveAuth(abbyyInput.isPreemptiveAuth())
                .username(abbyyInput.getUsername())
                .password(abbyyInput.getPassword())
                .kerberosConfigFile(abbyyInput.getKerberosConfigFile())
                .kerberosLoginConfFile(abbyyInput.getKerberosLoginConfFile())
                .kerberosSkipPortForLookup(abbyyInput.getKerberosSkipPortForLookup())
                .proxyHost(abbyyInput.getProxyHost())
                .proxyPort(abbyyInput.getProxyPort())
                .proxyUsername(abbyyInput.getProxyUsername())
                .proxyPassword(abbyyInput.getProxyPassword())
                .trustAllRoots(abbyyInput.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInput.getX509HostnameVerifier())
                .trustKeystore(abbyyInput.getTrustKeystore())
                .trustPassword(abbyyInput.getTrustPassword())
                .keystore(abbyyInput.getKeystore())
                .keystorePassword(abbyyInput.getKeystorePassword())
                .connectTimeout(abbyyInput.getConnectTimeout())
                .socketTimeout(abbyyInput.getSocketTimeout())
                .useCookies(abbyyInput.isUseCookies())
                .keepAlive(abbyyInput.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInput.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInput.getConnectionsMaxTotal())
                .headers(abbyyInput.getHeaders())
                .responseCharacterSet(StandardCharsets.UTF_8.displayName())
                .followRedirects(abbyyInput.isFollowRedirects())
                .queryParams(abbyyInput.getQueryParams())
                .queryParamsAreURLEncoded(abbyyInput.isQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(abbyyInput.isQueryParamsAreFormEncoded())
                .formParams(abbyyInput.getFormParams())
                .formParamsAreURLEncoded(abbyyInput.isFormParamsAreURLEncoded())
                .sourceFile(abbyyInput.getSourceFile())
                .body(abbyyInput.getBody())
                .contentType(Headers.CONTENT_TYPE_XML)
                .requestCharacterSet(abbyyInput.getRequestCharacterSet())
                .multipartBodies(abbyyInput.getMultipartBodies())
                .multipartBodiesContentType(abbyyInput.getMultipartBodiesContentType())
                .multipartFiles(abbyyInput.getMultipartFiles())
                .multipartFilesContentType(abbyyInput.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(abbyyInput.isMultipartValuesAreURLEncoded())
                .chunkedRequestEntity(abbyyInput.isChunkedRequestEntity())
                .method(HttpMethods.GET)
                .build();

        HttpClientResponse httpResponse = HttpClient.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());

        return responseParser.parseResponse(httpResponse);
    }


    public String getResult(@NotNull AbbyyInput abbyyInput, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                            @Nullable String downloadPath, boolean useSpecificCharSet) throws AbbyySdkException, IOException, HttpClientException, URISyntaxException {
        HttpRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .tlsVersion(abbyyInput.getTlsVersion())
                .allowedCyphers(abbyyInput.getAllowedCyphers())
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .preemptiveAuth(abbyyInput.isPreemptiveAuth())
                .username(abbyyInput.getUsername())
                .password(abbyyInput.getPassword())
                .kerberosConfigFile(abbyyInput.getKerberosConfigFile())
                .kerberosLoginConfFile(abbyyInput.getKerberosLoginConfFile())
                .kerberosSkipPortForLookup(abbyyInput.getKerberosSkipPortForLookup())
                .proxyHost(abbyyInput.getProxyHost())
                .proxyPort(abbyyInput.getProxyPort())
                .proxyUsername(abbyyInput.getProxyUsername())
                .proxyPassword(abbyyInput.getProxyPassword())
                .trustAllRoots(abbyyInput.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInput.getX509HostnameVerifier())
                .trustKeystore(abbyyInput.getTrustKeystore())
                .trustPassword(abbyyInput.getTrustPassword())
                .keystore(abbyyInput.getKeystore())
                .keystorePassword(abbyyInput.getKeystorePassword())
                .connectTimeout(abbyyInput.getConnectTimeout())
                .socketTimeout(abbyyInput.getSocketTimeout())
                .useCookies(abbyyInput.isUseCookies())
                .keepAlive(abbyyInput.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInput.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInput.getConnectionsMaxTotal())
                .headers(abbyyInput.getHeaders())
                .responseCharacterSet(useSpecificCharSet ? abbyyInput.getResponseCharacterSet() : null)
                .followRedirects(abbyyInput.isFollowRedirects())
                .queryParams(abbyyInput.getQueryParams())
                .queryParamsAreURLEncoded(abbyyInput.isQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(abbyyInput.isQueryParamsAreFormEncoded())
                .formParams(abbyyInput.getFormParams())
                .formParamsAreURLEncoded(abbyyInput.isFormParamsAreURLEncoded())
                .sourceFile(abbyyInput.getSourceFile())
                .destinationFile(downloadPath != null ? Paths.get(downloadPath) : null)
                .body(abbyyInput.getBody())
                .requestCharacterSet(abbyyInput.getRequestCharacterSet())
                .multipartBodies(abbyyInput.getMultipartBodies())
                .multipartBodiesContentType(abbyyInput.getMultipartBodiesContentType())
                .multipartFiles(abbyyInput.getMultipartFiles())
                .multipartFilesContentType(abbyyInput.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(abbyyInput.isMultipartValuesAreURLEncoded())
                .chunkedRequestEntity(abbyyInput.isChunkedRequestEntity())
                .method(HttpMethods.GET)
                .build();

        HttpClientResponse httpResponse = HttpClient.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() != 200) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        return httpResponse.getReturnResult();
    }


    public long getResultSize(@NotNull AbbyyInput abbyyInput, @NotNull String resultUrl, @NotNull ExportFormat exportFormat)
            throws AbbyySdkException, IOException, HttpClientException, URISyntaxException {

        HttpRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .tlsVersion(abbyyInput.getTlsVersion())
                .allowedCyphers(abbyyInput.getAllowedCyphers())
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .preemptiveAuth(abbyyInput.isPreemptiveAuth())
                .username(abbyyInput.getUsername())
                .password(abbyyInput.getPassword())
                .kerberosConfigFile(abbyyInput.getKerberosConfigFile())
                .kerberosLoginConfFile(abbyyInput.getKerberosLoginConfFile())
                .kerberosSkipPortForLookup(abbyyInput.getKerberosSkipPortForLookup())
                .proxyHost(abbyyInput.getProxyHost())
                .proxyPort(abbyyInput.getProxyPort())
                .proxyUsername(abbyyInput.getProxyUsername())
                .proxyPassword(abbyyInput.getProxyPassword())
                .trustAllRoots(abbyyInput.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInput.getX509HostnameVerifier())
                .trustKeystore(abbyyInput.getTrustKeystore())
                .trustPassword(abbyyInput.getTrustPassword())
                .keystore(abbyyInput.getKeystore())
                .keystorePassword(abbyyInput.getKeystorePassword())
                .connectTimeout(abbyyInput.getConnectTimeout())
                .socketTimeout(abbyyInput.getSocketTimeout())
                .useCookies(abbyyInput.isUseCookies())
                .keepAlive(abbyyInput.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInput.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInput.getConnectionsMaxTotal())
                .headers(abbyyInput.getHeaders())
                .responseCharacterSet(StandardCharsets.UTF_8.displayName())
                .followRedirects(abbyyInput.isFollowRedirects())
                .queryParams(abbyyInput.getQueryParams())
                .queryParamsAreURLEncoded(abbyyInput.isQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(abbyyInput.isQueryParamsAreFormEncoded())
                .formParams(abbyyInput.getFormParams())
                .formParamsAreURLEncoded(abbyyInput.isFormParamsAreURLEncoded())
                .sourceFile(abbyyInput.getSourceFile())
                .body(abbyyInput.getBody())
                .contentType(Headers.CONTENT_TYPE_XML)
                .requestCharacterSet(abbyyInput.getRequestCharacterSet())
                .multipartBodies(abbyyInput.getMultipartBodies())
                .multipartBodiesContentType(abbyyInput.getMultipartBodiesContentType())
                .multipartFiles(abbyyInput.getMultipartFiles())
                .multipartFilesContentType(abbyyInput.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(abbyyInput.isMultipartValuesAreURLEncoded())
                .chunkedRequestEntity(abbyyInput.isChunkedRequestEntity())
                .method(HttpMethods.HEAD)
                .build();

        HttpClientResponse httpResponse = HttpClient.execute(httpRequest);

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


    public String getResultChunk(@NotNull AbbyyInput abbyyInput, @NotNull String resultUrl, @NotNull ExportFormat exportFormat,
                                 int startByteIndex, int endByteIndex) throws AbbyySdkException, IOException, HttpClientException, URISyntaxException {
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

        HttpRequest httpRequest = new HttpClientRequest.Builder()
                .url(resultUrl)
                .tlsVersion(abbyyInput.getTlsVersion())
                .allowedCyphers(abbyyInput.getAllowedCyphers())
                .authType(Headers.AUTH_TYPE_ANONYMOUS)
                .preemptiveAuth(abbyyInput.isPreemptiveAuth())
                .username(abbyyInput.getUsername())
                .password(abbyyInput.getPassword())
                .kerberosConfigFile(abbyyInput.getKerberosConfigFile())
                .kerberosLoginConfFile(abbyyInput.getKerberosLoginConfFile())
                .kerberosSkipPortForLookup(abbyyInput.getKerberosSkipPortForLookup())
                .proxyHost(abbyyInput.getProxyHost())
                .proxyPort(abbyyInput.getProxyPort())
                .proxyUsername(abbyyInput.getProxyUsername())
                .proxyPassword(abbyyInput.getProxyPassword())
                .trustAllRoots(abbyyInput.isTrustAllRoots())
                .x509HostnameVerifier(abbyyInput.getX509HostnameVerifier())
                .trustKeystore(abbyyInput.getTrustKeystore())
                .trustPassword(abbyyInput.getTrustPassword())
                .keystore(abbyyInput.getKeystore())
                .keystorePassword(abbyyInput.getKeystorePassword())
                .connectTimeout(abbyyInput.getConnectTimeout())
                .socketTimeout(abbyyInput.getSocketTimeout())
                .useCookies(abbyyInput.isUseCookies())
                .keepAlive(abbyyInput.isKeepAlive())
                .connectionsMaxPerRoute(abbyyInput.getConnectionsMaxPerRoute())
                .connectionsMaxTotal(abbyyInput.getConnectionsMaxTotal())
                .headers(headers)
                .responseCharacterSet(StandardCharsets.UTF_8.displayName())
                .followRedirects(abbyyInput.isFollowRedirects())
                .queryParams(abbyyInput.getQueryParams())
                .queryParamsAreURLEncoded(abbyyInput.isQueryParamsAreURLEncoded())
                .queryParamsAreFormEncoded(abbyyInput.isQueryParamsAreFormEncoded())
                .formParams(abbyyInput.getFormParams())
                .formParamsAreURLEncoded(abbyyInput.isFormParamsAreURLEncoded())
                .sourceFile(abbyyInput.getSourceFile())
                .body(abbyyInput.getBody())
                .contentType(abbyyInput.getContentType())
                .requestCharacterSet(abbyyInput.getRequestCharacterSet())
                .multipartBodies(abbyyInput.getMultipartBodies())
                .multipartBodiesContentType(abbyyInput.getMultipartBodiesContentType())
                .multipartFiles(abbyyInput.getMultipartFiles())
                .multipartFilesContentType(abbyyInput.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(abbyyInput.isMultipartValuesAreURLEncoded())
                .chunkedRequestEntity(abbyyInput.isChunkedRequestEntity())
                .method(HttpMethods.GET)
                .build();

        HttpClientResponse httpResponse = HttpClient.execute(httpRequest);

        lastStatusCode = String.valueOf(httpResponse.getStatusCode());
        if (httpResponse.getStatusCode() != 200 && httpResponse.getStatusCode() != 206) {
            throw new AbbyySdkException(String.format(ExceptionMsgs.PROCESSING_RESULT_COULD_NOT_BE_RETRIEVED, exportFormat));
        }

        return httpResponse.getReturnResult();
    }
}
