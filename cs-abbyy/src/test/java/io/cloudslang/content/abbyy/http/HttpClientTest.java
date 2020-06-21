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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abbyy.constants.HttpClientOutputNames;
import io.cloudslang.content.abbyy.entities.requests.HttpClientRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClient.class})
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
    private static final File destinationFile = new File("destinationFile");
    private static final Boolean followedRedirects = true;
    private static final String queryParams = "queryParams";
    private static final Boolean queryParamsAreURLEncoded = false;
    private static final Boolean queryParamsAreFormEncoded = false;
    private static final String formParams = "formParams";
    private static final Boolean formParamsAreURLEncoded = false;
    private static final File sourceFile = new File("sourceFile");
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


    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private HttpClientRequest httpRequestMock;


    @Before
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
        HttpClientAction httpClientActionMock = PowerMockito.mock(HttpClientAction.class);
        PowerMockito.whenNew(HttpClientAction.class).withAnyArguments().thenReturn(httpClientActionMock);
        when(httpClientActionMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        HttpClientResponse.Builder httpClientResponseBuilderSpy = new HttpClientResponse.Builder();
        httpClientResponseBuilderSpy = PowerMockito.spy(httpClientResponseBuilderSpy);
        when(httpClientResponseBuilderSpy.build()).thenReturn(null);
        PowerMockito.whenNew(HttpClientResponse.Builder.class).withAnyArguments().thenReturn(httpClientResponseBuilderSpy);

        //Act
        HttpClient.execute(httpRequestMock);

        //Assert
        verify(httpClientActionMock).execute(eq(url), eq(tlsVersion), eq(allowedCyphers), eq(authType), eq(preemptiveAuth.toString()),
                eq(username), eq(password), eq(kerberosConfigFile), eq(kerberosLoginConfFile), eq(kerberosSkipPortForLookup),
                eq(proxyHost), eq(proxyPort.toString()), eq(proxyUsername), eq(proxyPassword), eq(trustAllRoots.toString()), eq(x509HostnameVerifier),
                eq(trustKeystore), eq(trustPassword), eq(keystore), eq(keystorePassword), eq(connectTimeout.toString()), eq(socketTimeout.toString()),
                eq(useCookies.toString()), eq(keepAlive.toString()), eq(connectionsMaxPerRoute.toString()), eq(connectionsMaxTotal.toString()), eq(headers),
                eq(responseCharacterSet), eq(destinationFile.getAbsolutePath()), eq(followedRedirects.toString()), eq(queryParams),
                eq(queryParamsAreURLEncoded.toString()), eq(queryParamsAreFormEncoded.toString()), eq(formParams), eq(formParamsAreURLEncoded.toString()),
                eq(sourceFile.getAbsolutePath()), eq(body), eq(contentType), eq(requestCharacterSet), eq(multipartBodies),
                eq(multipartBodiesContentType), eq(multipartFiles), eq(multipartFilesContentType),
                eq(multipartValuesAreURLEncoded.toString()), eq(chunkedRequestEntity.toString()), eq(method), eq(httpClientCookieSession),
                eq(httpClientPoolingConnectionManager)
        );

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
        HttpClientAction httpClientActionMock = PowerMockito.mock(HttpClientAction.class);
        PowerMockito.whenNew(HttpClientAction.class).withAnyArguments().thenReturn(httpClientActionMock);
        when(httpClientActionMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        HttpClientResponse.Builder httpClientResponseBuilderSpy = new HttpClientResponse.Builder();
        httpClientResponseBuilderSpy = PowerMockito.spy(httpClientResponseBuilderSpy);
        when(httpClientResponseBuilderSpy.build()).thenReturn(null);
        PowerMockito.whenNew(HttpClientResponse.Builder.class).withAnyArguments().thenReturn(httpClientResponseBuilderSpy);

        //Assert
        this.exception.expect(HttpClientException.class);

        //Act
        HttpClient.execute(httpRequestMock);
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
