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

import io.cloudslang.content.abbyy.constants.HttpClientOutputNames;
import io.cloudslang.content.abbyy.entities.requests.HttpRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.HttpClientException;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

class HttpClient {
    public static HttpClientResponse execute(@NotNull HttpRequest request) throws IOException, HttpClientException, URISyntaxException {
        Map<String, String> rawResponse = new HttpClientAction().execute(
                request.getUrl(),
                request.getTlsVersion(),
                request.getAllowedCyphers(),
                request.getAuthType(),
                String.valueOf(request.isPreemptiveAuth()),
                request.getUsername(),
                request.getPassword(),
                request.getKerberosConfigFile(),
                request.getKerberosLoginConfFile(),
                request.getKerberosSkipPortForLookup(),
                request.getProxyHost(),
                String.valueOf(request.getProxyPort()),
                request.getProxyUsername(),
                request.getProxyPassword(),
                String.valueOf(request.isTrustAllRoots()),
                request.getX509HostnameVerifier(),
                request.getTrustKeystore(),
                request.getTrustPassword(),
                request.getKeystore(),
                request.getKeystorePassword(),
                String.valueOf(request.getConnectTimeout()),
                String.valueOf(request.getSocketTimeout()),
                String.valueOf(request.isUseCookies()),
                String.valueOf(request.isKeepAlive()),
                String.valueOf(request.getConnectionsMaxPerRoute()),
                String.valueOf(request.getConnectionsMaxTotal()),
                request.getHeaders(),
                request.getResponseCharacterSet(),
                request.getDestinationFile() != null ? request.getDestinationFile().getAbsolutePath() : null,
                String.valueOf(request.isFollowRedirects()),
                request.getQueryParams(),
                String.valueOf(request.isQueryParamsAreURLEncoded()),
                String.valueOf(request.isQueryParamsAreFormEncoded()),
                request.getFormParams(),
                String.valueOf(request.isFormParamsAreURLEncoded()),
                request.getSourceFile() != null ? request.getSourceFile().getAbsolutePath() : null,
                request.getBody(),
                request.getContentType(),
                request.getRequestCharacterSet(),
                request.getMultipartBodies(),
                request.getMultipartBodiesContentType(),
                request.getMultipartFiles(),
                request.getMultipartFilesContentType(),
                String.valueOf(request.isMultipartValuesAreURLEncoded()),
                String.valueOf(request.isChunkedRequestEntity()),
                request.getMethod(),
                request.getHttpClientCookieSession(),
                request.getHttpClientPoolingConnectionManager()
        );

        if (ReturnCodes.FAILURE.equals(rawResponse.get(HttpClientOutputNames.RETURN_CODE))) {
            throw new HttpClientException(rawResponse.get(HttpClientOutputNames.EXCEPTION));
        }

        return new HttpClientResponse.Builder()
                .returnResult(rawResponse.get(HttpClientOutputNames.RETURN_RESULT))
                .exception(rawResponse.get(HttpClientOutputNames.EXCEPTION))
                .statusCode(rawResponse.get(HttpClientOutputNames.STATUS_CODE))
                .responseHeaders(rawResponse.get(HttpClientOutputNames.RESPONSE_HEADERS))
                .returnCode(rawResponse.get(HttpClientOutputNames.RETURN_CODE))
                .build();
    }
}
