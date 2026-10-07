/*
 * Copyright 2020-2024 Open Text
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


package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.constants.HttpClientOutputNames;
import io.cloudslang.content.abbyy.entities.requests.HttpRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import io.cloudslang.content.abbyy.utils.EncodingUtils;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static io.cloudslang.content.abbyy.constants.DefaultInputValues.ZERO;

class HttpClient {
    public static HttpClientResponse execute(@NotNull HttpRequest request) throws IOException, HttpClientException, URISyntaxException {
        HttpClientInputs inputs = HttpClientInputs.builder()
                .url(request.getUrl())
                .method(request.getMethod())
                .authType(request.getAuthType())
                .username(request.getUsername())
                .password(request.getPassword())
                .preemptiveAuth(String.valueOf(request.isPreemptiveAuth()))
                .proxyHost(request.getProxyHost())
                .proxyPort(String.valueOf(request.getProxyPort()))
                .proxyUsername(request.getProxyUsername())
                .proxyPassword(request.getProxyPassword())
                .tlsVersion(request.getTlsVersion())
                .allowedCiphers(request.getAllowedCyphers())
                .trustAllRoots(String.valueOf(request.isTrustAllRoots()))
                .x509HostnameVerifier(request.getX509HostnameVerifier())
                .trustKeystore(request.getTrustKeystore())
                .trustPassword(request.getTrustPassword())
                .keystore(request.getKeystore())
                .keystorePassword(request.getKeystorePassword())
                .connectTimeout(String.valueOf(request.getConnectTimeout()))
                .responseTimeout(String.valueOf(request.getSocketTimeout()))
                .executionTimeout(ZERO)
                .keepAlive(String.valueOf(request.isKeepAlive()))
                .connectionsMaxPerRoute(String.valueOf(request.getConnectionsMaxPerRoute()))
                .connectionsMaxTotal(String.valueOf(request.getConnectionsMaxTotal()))
                .useCookies(String.valueOf(request.isUseCookies()))
                .followRedirects(String.valueOf(request.isFollowRedirects()))
                .headers(request.getHeaders())
                .destinationFile(request.getDestinationFile() != null ? request.getDestinationFile().toAbsolutePath().toString() : null)
                .responseCharacterSet(request.getResponseCharacterSet())
                .queryParams(request.getQueryParams())
                .queryParamsAreURLEncoded(String.valueOf(request.isQueryParamsAreURLEncoded()))
                .queryParamsAreFormEncoded(String.valueOf(request.isQueryParamsAreFormEncoded()))
                .formParams(request.getFormParams())
                .formParamsAreURLEncoded(String.valueOf(request.isFormParamsAreURLEncoded()))
                .sourceFile(request.getSourceFile() != null ? request.getSourceFile().toAbsolutePath().toString() : null)
                .body(request.getBody())
                .contentType(request.getContentType())
                .requestCharacterSet(request.getRequestCharacterSet())
                .multipartBodies(request.getMultipartBodies())
                .multipartBodiesContentType(request.getMultipartBodiesContentType())
                .multipartFiles(request.getMultipartFiles())
                .multipartFilesContentType(request.getMultipartFilesContentType())
                .multipartValuesAreURLEncoded(String.valueOf(request.isMultipartValuesAreURLEncoded()))
                .cookieStoreSessionObject(request.getHttpClientCookieSession())
                .connectionPoolSessionObject(request.getHttpClientPoolingConnectionManager())
                .build();
        Map<String, String> rawResponse;
        try {
            rawResponse = HttpClientService.execute(inputs);
        } catch (IOException | URISyntaxException e) {
            throw e;
        } catch (Exception e) {
            throw new HttpClientException(e.getMessage(), e);
        }

        if (ReturnCodes.FAILURE.equals(rawResponse.get(HttpClientOutputNames.RETURN_CODE))) {
            throw new HttpClientException(rawResponse.get(HttpClientOutputNames.EXCEPTION));
        }

        String returnResult = rawResponse.get(HttpClientOutputNames.RETURN_RESULT);
        if(StringUtils.isNotEmpty(returnResult)){
            returnResult = EncodingUtils.discardBOMChar(returnResult);
        }

        return new HttpClientResponse.Builder()
                .returnResult(returnResult)
                .exception(rawResponse.get(HttpClientOutputNames.EXCEPTION))
                .statusCode(rawResponse.get(HttpClientOutputNames.STATUS_CODE))
                .responseHeaders(rawResponse.get(HttpClientOutputNames.RESPONSE_HEADERS))
                .returnCode(rawResponse.get(HttpClientOutputNames.RETURN_CODE))
                .build();
    }
}
