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
import io.cloudslang.content.httpclient.actions.HttpClientAction;

import java.io.IOException;
import java.util.Map;

class HttpSDK {
    public static HttpClientResponse execute(HttpClientRequest request) throws IOException {
        Map<String, String> rawResponse = new HttpClientAction().execute(
                request.getUrl(),
                request.getTlsVersion(),
                request.getAllowedCyphers(),
                request.getAuthType(),
                request.getPreemptiveAuth(),
                request.getUsername(),
                request.getPassword(),
                request.getKerberosConfigFile(),
                request.getKerberosLoginConfFile(),
                request.getKerberosSkipPortForLookup(),
                request.getProxyHost(),
                request.getProxyPort(),
                request.getProxyUsername(),
                request.getProxyPassword(),
                request.getTrustAllRoots(),
                request.getX509HostnameVerifier(),
                request.getTrustKeystore(),
                request.getTrustPassword(),
                request.getKeystore(),
                request.getKeystorePassword(),
                request.getConnectTimeout(),
                request.getSocketTimeout(),
                request.getUseCookies(),
                request.getKeepAlive(),
                request.getConnectionsMaxPerRoute(),
                request.getConnectionsMaxTotal(),
                request.getHeaders(),
                request.getResponseCharacterSet(),
                request.getDestinationFile(),
                request.getFollowRedirects(),
                request.getQueryParams(),
                request.getQueryParamsAreURLEncoded(),
                request.getQueryParamsAreFormEncoded(),
                request.getFormParams(),
                request.getFormParamsAreURLEncoded(),
                request.getSourceFile(),
                request.getBody(),
                request.getContentType(),
                request.getRequestCharacterSet(),
                request.getMultipartBodies(),
                request.getMultipartBodiesContentType(),
                request.getMultipartFiles(),
                request.getMultipartFilesContentType(),
                request.getMultipartValuesAreURLEncoded(),
                request.getChunkedRequestEntity(),
                request.getMethod(),
                request.getHttpClientCookieSession(),
                request.getHttpClientPoolingConnectionManager()
        );

        return new HttpClientResponse.Builder()
                .returnResult(rawResponse.get(HttpClientOutputNames.RETURN_RESULT))
                .exception(rawResponse.get(HttpClientOutputNames.EXCEPTION))
                .statusCode(rawResponse.get(HttpClientOutputNames.STATUS_CODE))
                .responseHeaders(rawResponse.get(HttpClientOutputNames.RESPONSE_HEADERS))
                .returnCode(rawResponse.get(HttpClientOutputNames.RETURN_CODE))
                .build();
    }
}
