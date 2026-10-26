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

import io.cloudslang.content.plugin.GlobalSessionObject;
import io.cloudslang.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abbyy.constants.HttpClientOutputNames;
import io.cloudslang.content.abbyy.entities.requests.HttpClientRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedConstruction;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
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
    private static final Integer executionTimeout = 0;
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
    private org.mockito.MockedStatic<HttpClientService> httpClientServiceMock;
    private MockedConstruction<HttpClientResponse.Builder> builderConstruction;
    private Map<String, String> rawResponse;


    @BeforeEach
    public void setUp() {
        mockHttpClientRequest();
        httpClientServiceMock = mockStatic(HttpClientService.class);
        httpClientServiceMock.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                .thenAnswer(invocation -> rawResponse);
        builderConstruction = mockConstruction(HttpClientResponse.Builder.class, withSettings().defaultAnswer(invocation ->
                "build".equals(invocation.getMethod().getName()) ? null : invocation.getMock()));
    }

    @AfterEach
    public void tearDown() {
        builderConstruction.close();
        httpClientServiceMock.close();
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
        this.rawResponse = rawResponse;

        //Act
        HttpClient.execute(httpRequestMock);

        //Assert
        org.mockito.ArgumentCaptor<HttpClientInputs> inputsCaptor = org.mockito.ArgumentCaptor.forClass(HttpClientInputs.class);
        httpClientServiceMock.verify(() -> HttpClientService.execute(inputsCaptor.capture()));
        HttpClientInputs inputs = inputsCaptor.getValue();
        HttpClientResponse.Builder httpClientResponseBuilderSpy = builderConstruction.constructed().get(0);
        org.junit.jupiter.api.Assertions.assertEquals(url, inputs.getUrl());
        org.junit.jupiter.api.Assertions.assertEquals(method, inputs.getMethod());
        org.junit.jupiter.api.Assertions.assertEquals(tlsVersion, inputs.getTlsVersion());
        org.junit.jupiter.api.Assertions.assertEquals(allowedCyphers, inputs.getAllowedCiphers());
        org.junit.jupiter.api.Assertions.assertEquals(authType, inputs.getAuthType());
        org.junit.jupiter.api.Assertions.assertEquals(preemptiveAuth.toString(), inputs.getPreemptiveAuth());
        org.junit.jupiter.api.Assertions.assertEquals(username, inputs.getUsername());
        org.junit.jupiter.api.Assertions.assertEquals(password, inputs.getPassword());
        org.junit.jupiter.api.Assertions.assertEquals(proxyHost, inputs.getProxyHost());
        org.junit.jupiter.api.Assertions.assertEquals(proxyPort.toString(), inputs.getProxyPort());
        org.junit.jupiter.api.Assertions.assertEquals(proxyUsername, inputs.getProxyUsername());
        org.junit.jupiter.api.Assertions.assertEquals(proxyPassword, inputs.getProxyPassword());
        org.junit.jupiter.api.Assertions.assertEquals(trustAllRoots.toString(), inputs.getTrustAllRoots());
        org.junit.jupiter.api.Assertions.assertEquals(x509HostnameVerifier, inputs.getX509HostnameVerifier());
        org.junit.jupiter.api.Assertions.assertEquals(trustKeystore, inputs.getTrustKeystore());
        org.junit.jupiter.api.Assertions.assertEquals(trustPassword, inputs.getTrustPassword());
        org.junit.jupiter.api.Assertions.assertEquals(keystore, inputs.getKeystore());
        org.junit.jupiter.api.Assertions.assertEquals(keystorePassword, inputs.getKeystorePassword());
        org.junit.jupiter.api.Assertions.assertEquals(connectTimeout.toString(), inputs.getConnectTimeout());
        org.junit.jupiter.api.Assertions.assertEquals(socketTimeout.toString(), inputs.getResponseTimeout());
        org.junit.jupiter.api.Assertions.assertEquals(executionTimeout.toString(), inputs.getExecutionTimeout());
        org.junit.jupiter.api.Assertions.assertEquals(useCookies.toString(), inputs.getUseCookies());
        org.junit.jupiter.api.Assertions.assertEquals(keepAlive.toString(), inputs.getKeepAlive());
        org.junit.jupiter.api.Assertions.assertEquals(connectionsMaxPerRoute.toString(), inputs.getConnectionsMaxPerRoute());
        org.junit.jupiter.api.Assertions.assertEquals(connectionsMaxTotal.toString(), inputs.getConnectionsMaxTotal());
        org.junit.jupiter.api.Assertions.assertEquals(headers, inputs.getHeaders());
        org.junit.jupiter.api.Assertions.assertEquals(responseCharacterSet, inputs.getResponseCharacterSet());
        org.junit.jupiter.api.Assertions.assertEquals(destinationFile.toAbsolutePath().toString(), inputs.getDestinationFile());
        org.junit.jupiter.api.Assertions.assertEquals(followedRedirects.toString(), inputs.getFollowRedirects());
        org.junit.jupiter.api.Assertions.assertEquals(queryParams, inputs.getQueryParams());
        org.junit.jupiter.api.Assertions.assertEquals(queryParamsAreURLEncoded.toString(), inputs.getQueryParamsAreURLEncoded());
        org.junit.jupiter.api.Assertions.assertEquals(queryParamsAreFormEncoded.toString(), inputs.getQueryParamsAreFormEncoded());
        org.junit.jupiter.api.Assertions.assertEquals(formParams, inputs.getFormParams());
        org.junit.jupiter.api.Assertions.assertEquals(formParamsAreURLEncoded.toString(), inputs.getFormParamsAreURLEncoded());
        org.junit.jupiter.api.Assertions.assertEquals(sourceFile.toAbsolutePath().toString(), inputs.getSourceFile());
        org.junit.jupiter.api.Assertions.assertEquals(body, inputs.getBody());
        org.junit.jupiter.api.Assertions.assertEquals(contentType, inputs.getContentType());
        org.junit.jupiter.api.Assertions.assertEquals(requestCharacterSet, inputs.getRequestCharacterSet());
        org.junit.jupiter.api.Assertions.assertEquals(multipartBodies, inputs.getMultipartBodies());
        org.junit.jupiter.api.Assertions.assertEquals(multipartBodiesContentType, inputs.getMultipartBodiesContentType());
        org.junit.jupiter.api.Assertions.assertEquals(multipartFiles, inputs.getMultipartFiles());
        org.junit.jupiter.api.Assertions.assertEquals(multipartFilesContentType, inputs.getMultipartFilesContentType());
        org.junit.jupiter.api.Assertions.assertEquals(multipartValuesAreURLEncoded.toString(), inputs.getMultipartValuesAreURLEncoded());
        org.junit.jupiter.api.Assertions.assertSame(httpClientCookieSession, inputs.getCookieStoreSessionObject());
        org.junit.jupiter.api.Assertions.assertSame(httpClientPoolingConnectionManager, inputs.getConnectionPoolSessionObject());

        verify(httpClientResponseBuilderSpy).returnResult(returnResult);
        verify(httpClientResponseBuilderSpy).exception(exception);
        verify(httpClientResponseBuilderSpy).statusCode(statusCode);
        verify(httpClientResponseBuilderSpy).responseHeaders(responseHeaders);
        verify(httpClientResponseBuilderSpy).returnCode(returnCode);
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
        this.rawResponse = rawResponse;

        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(HttpClientException.class, () -> HttpClient.execute(httpRequestMock));
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
