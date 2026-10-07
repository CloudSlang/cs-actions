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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abbyy.constants.HttpClientOutputNames;
import io.cloudslang.content.abbyy.entities.requests.HttpClientRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HttpClientTest {

    private static final String url = "url";
    private static final String tlsVersion = "tlsVersion";
    private static final String allowedCyphers = "allowedCyphers";
    private static final String authType = "authType";
    private static final Boolean preemptiveAuth = true;
    private static final String username = "username";
    private static final String password = "password";
    private static final String kerberosConfigFile = "kerberosConfigFile";
    private static final String kerberosLoginConfFile = "kerberosLoginConfFile";
    private static final String kerberosSkipPortForLookup = "kerberosSkipPortForLookup";
    private static final String proxyHost = "proxyHost";
    private static final Short proxyPort = 0;
    private static final String proxyUsername = "proxyUsername";
    private static final String proxyPassword = "proxyPassword";
    private static final Boolean trustAllRoots = true;
    private static final String x509HostnameVerifier = "x509HostnameVerifier";
    private static final String trustKeystore = "trustKeystore";
    private static final String trustPassword = "trustPassword";
    private static final String keystore = "keystore";
    private static final String keystorePassword = "keystorePassword";
    private static final Integer connectTimeout = 1;
    private static final Integer socketTimeout = 1;
    private static final Boolean useCookies = true;
    private static final Boolean keepAlive = false;
    private static final Integer connectionsMaxPerRoute = 2;
    private static final Integer connectionsMaxTotal = 2;
    private static final String headers = "headers";
    private static final String responseCharacterSet = "responseCharacterSet";
    private static final Path destinationFile = Paths.get("destinationFile");
    private static final Boolean followedRedirects = true;
    private static final String queryParams = "queryParams";
    private static final Boolean queryParamsAreURLEncoded = false;
    private static final Boolean queryParamsAreFormEncoded = false;
    private static final String formParams = "formParams";
    private static final Boolean formParamsAreURLEncoded = false;
    private static final Path sourceFile = Paths.get("sourceFile");
    private static final String body = "body";
    private static final String contentType = "contentType";
    private static final String requestCharacterSet = "requestCharacterSet";
    private static final String multipartBodies = "multipartBodies";
    private static final String multipartBodiesContentType = "multipartBodiesContentType";
    private static final String multipartFiles = "multipartFiles";
    private static final String multipartFilesContentType = "multipartFilesContentType";
    private static final Boolean multipartValuesAreURLEncoded = false;
    private static final Boolean chunkedRequestEntity = false;
    private static final String method = "method";
    private static final SerializableSessionObject httpClientCookieSession = null;
    private static final GlobalSessionObject httpClientPoolingConnectionManager = null;


    @Mock
    private HttpClientRequest httpRequestMock;


    @BeforeEach
    public void setUp() {
        mockHttpClientRequest();
    }


    @Test
    public void execute_requestIsValid_success() throws Exception {
        //Arrange
        final String returnResult = "return result";
        final String exception = StringUtils.EMPTY;
        final String statusCode = "status code";
        final String responseHeaders = "response headers";
        final String returnCode = "return code";

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(HttpClientOutputNames.RETURN_RESULT, returnResult);
        rawResponse.put(HttpClientOutputNames.EXCEPTION, exception);
        rawResponse.put(HttpClientOutputNames.STATUS_CODE, statusCode);
        rawResponse.put(HttpClientOutputNames.RESPONSE_HEADERS, responseHeaders);
        rawResponse.put(HttpClientOutputNames.RETURN_CODE, returnCode);
        try (MockedStatic<HttpClientService> httpClientService = mockStatic(HttpClientService.class);
             MockedConstruction<HttpClientResponse.Builder> responseBuilder =
                     mockConstruction(HttpClientResponse.Builder.class,
                             withSettings().defaultAnswer(CALLS_REAL_METHODS),
                             (builder, context) -> when(builder.build()).thenReturn(null))) {
            httpClientService.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(rawResponse);

            //Act
            HttpClient.execute(httpRequestMock);

            //Assert
            httpClientService.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
            HttpClientResponse.Builder responseBuilderMock = responseBuilder.constructed().get(0);
            verify(responseBuilderMock).returnResult(returnResult);
            verify(responseBuilderMock).exception(exception);
            verify(responseBuilderMock).statusCode(statusCode);
            verify(responseBuilderMock).responseHeaders(responseHeaders);
            verify(responseBuilderMock).returnCode(returnCode);
        }
    }


    @Test
    public void execute_httpRequestFails_HttpException() throws Exception {
        //Arrange
        final String returnResult = "return result";
        final String exception = "exception";
        final String statusCode = "status code";
        final String responseHeaders = "response headers";
        final String returnCode = ReturnCodes.FAILURE;

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(HttpClientOutputNames.RETURN_RESULT, returnResult);
        rawResponse.put(HttpClientOutputNames.EXCEPTION, exception);
        rawResponse.put(HttpClientOutputNames.STATUS_CODE, statusCode);
        rawResponse.put(HttpClientOutputNames.RESPONSE_HEADERS, responseHeaders);
        rawResponse.put(HttpClientOutputNames.RETURN_CODE, returnCode);
        try (MockedStatic<HttpClientService> httpClientService = mockStatic(HttpClientService.class)) {
            httpClientService.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(rawResponse);

            //Assert and Act
            assertThrows(HttpClientException.class, () -> HttpClient.execute(httpRequestMock));
        }
    }


    private HttpClientRequest mockHttpClientRequest() {
        when(httpRequestMock.getUrl()).thenReturn(url);
        when(httpRequestMock.getTlsVersion()).thenReturn(tlsVersion);
        when(httpRequestMock.getAllowedCyphers()).thenReturn(allowedCyphers);
        when(httpRequestMock.getAuthType()).thenReturn(authType);
        when(httpRequestMock.isPreemptiveAuth()).thenReturn(preemptiveAuth);
        when(httpRequestMock.getUsername()).thenReturn(username);
        when(httpRequestMock.getPassword()).thenReturn(password);
        when(httpRequestMock.getKerberosConfigFile()).thenReturn(kerberosConfigFile);
        when(httpRequestMock.getKerberosLoginConfFile()).thenReturn(kerberosLoginConfFile);
        when(httpRequestMock.getKerberosSkipPortForLookup()).thenReturn(kerberosSkipPortForLookup);
        when(httpRequestMock.getProxyHost()).thenReturn(proxyHost);
        when(httpRequestMock.getProxyPort()).thenReturn(proxyPort);
        when(httpRequestMock.getProxyUsername()).thenReturn(proxyUsername);
        when(httpRequestMock.getProxyPassword()).thenReturn(proxyPassword);
        when(httpRequestMock.isTrustAllRoots()).thenReturn(trustAllRoots);
        when(httpRequestMock.getX509HostnameVerifier()).thenReturn(x509HostnameVerifier);
        when(httpRequestMock.getTrustKeystore()).thenReturn(trustKeystore);
        when(httpRequestMock.getTrustPassword()).thenReturn(trustPassword);
        when(httpRequestMock.getKeystore()).thenReturn(keystore);
        when(httpRequestMock.getKeystorePassword()).thenReturn(keystorePassword);
        when(httpRequestMock.getConnectTimeout()).thenReturn(connectTimeout);
        when(httpRequestMock.getSocketTimeout()).thenReturn(socketTimeout);
        when(httpRequestMock.isUseCookies()).thenReturn(useCookies);
        when(httpRequestMock.isKeepAlive()).thenReturn(keepAlive);
        when(httpRequestMock.getConnectionsMaxPerRoute()).thenReturn(connectionsMaxPerRoute);
        when(httpRequestMock.getConnectionsMaxTotal()).thenReturn(connectionsMaxTotal);
        when(httpRequestMock.getHeaders()).thenReturn(headers);
        when(httpRequestMock.getResponseCharacterSet()).thenReturn(responseCharacterSet);
        when(httpRequestMock.getDestinationFile()).thenReturn(destinationFile);
        when(httpRequestMock.isFollowRedirects()).thenReturn(followedRedirects);
        when(httpRequestMock.getQueryParams()).thenReturn(queryParams);
        when(httpRequestMock.isQueryParamsAreURLEncoded()).thenReturn(queryParamsAreURLEncoded);
        when(httpRequestMock.isQueryParamsAreFormEncoded()).thenReturn(queryParamsAreFormEncoded);
        when(httpRequestMock.getFormParams()).thenReturn(formParams);
        when(httpRequestMock.isFormParamsAreURLEncoded()).thenReturn(formParamsAreURLEncoded);
        when(httpRequestMock.getSourceFile()).thenReturn(sourceFile);
        when(httpRequestMock.getBody()).thenReturn(body);
        when(httpRequestMock.getContentType()).thenReturn(contentType);
        when(httpRequestMock.getRequestCharacterSet()).thenReturn(requestCharacterSet);
        when(httpRequestMock.getMultipartBodies()).thenReturn(multipartBodies);
        when(httpRequestMock.getMultipartBodiesContentType()).thenReturn(multipartBodiesContentType);
        when(httpRequestMock.getMultipartFiles()).thenReturn(multipartFiles);
        when(httpRequestMock.getMultipartFilesContentType()).thenReturn(multipartFilesContentType);
        when(httpRequestMock.isMultipartValuesAreURLEncoded()).thenReturn(multipartValuesAreURLEncoded);
        when(httpRequestMock.isChunkedRequestEntity()).thenReturn(chunkedRequestEntity);
        when(httpRequestMock.getMethod()).thenReturn(method);
        when(httpRequestMock.getHttpClientCookieSession()).thenReturn(httpClientCookieSession);
        when(httpRequestMock.getHttpClientPoolingConnectionManager()).thenReturn(httpClientPoolingConnectionManager);

        return httpRequestMock;
    }
}
