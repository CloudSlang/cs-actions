/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.office365.entities;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.httpclient.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class Office365HttpClientInputs {
    private String url = "";
    private String authType = ANONYMOUS;
    private String username = "";
    private String password = "";
    private String method = GET;
    private String body = "";
    private String contentType = "";
    private String headers = "";
    private String responseCharacterSet = UTF_8;
    private String queryParams = "";
    private String proxyHost = "";
    private String proxyPort = "";
    private String proxyUsername = "";
    private String proxyPassword = "";
    private String trustAllRoots = BOOLEAN_FALSE;
    private String x509HostnameVerifier = STRICT;
    private String trustKeystore = DEFAULT_JAVA_KEYSTORE;
    private String trustPassword = "changeit";
    private String keystore = DEFAULT_JAVA_KEYSTORE;
    private String keystorePassword = "changeit";
    private String connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private String responseTimeout = DEFAULT_RESPONSE_TIMEOUT;
    private String keepAlive = BOOLEAN_FALSE;
    private String connectionsMaxPerRoute = DEFAULT_CONNECTIONS_MAX_PER_ROUTE;
    private String connectionsMaxTotal = DEFAULT_CONNECTIONS_MAX_TOTAL;

    public void setUrl(String value) { url = defaultIfEmpty(value, ""); }
    public void setAuthType(String value) { authType = defaultIfEmpty(value, ANONYMOUS); }
    public void setUsername(String value) { username = defaultIfEmpty(value, ""); }
    public void setPassword(String value) { password = defaultIfEmpty(value, ""); }
    public void setMethod(String value) { method = defaultIfEmpty(value, GET); }
    public void setBody(String value) { body = defaultIfEmpty(value, ""); }
    public void setContentType(String value) { contentType = defaultIfEmpty(value, ""); }
    public void setHeaders(String value) { headers = defaultIfEmpty(value, ""); }
    public void setResponseCharacterSet(String value) { responseCharacterSet = defaultIfEmpty(value, UTF_8); }
    public void setQueryParams(String value) { queryParams = defaultIfEmpty(value, ""); }
    public void setProxyHost(String value) { proxyHost = defaultIfEmpty(value, ""); }
    public void setProxyPort(String value) { proxyPort = defaultIfEmpty(value, ""); }
    public void setProxyUsername(String value) { proxyUsername = defaultIfEmpty(value, ""); }
    public void setProxyPassword(String value) { proxyPassword = defaultIfEmpty(value, ""); }
    public void setTrustAllRoots(String value) { trustAllRoots = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setX509HostnameVerifier(String value) { x509HostnameVerifier = defaultIfEmpty(value, STRICT); }
    public void setTrustKeystore(String value) { trustKeystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setTrustPassword(String value) { trustPassword = defaultIfEmpty(value, "changeit"); }
    public void setKeystore(String value) { keystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setKeystorePassword(String value) { keystorePassword = defaultIfEmpty(value, "changeit"); }
    public void setConnectTimeout(String value) { connectTimeout = defaultIfEmpty(value, DEFAULT_CONNECT_TIMEOUT); }
    public void setSocketTimeout(String value) { responseTimeout = defaultIfEmpty(value, DEFAULT_RESPONSE_TIMEOUT); }
    public void setKeepAlive(String value) { keepAlive = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setConnectionsMaxPerRoute(String value) { connectionsMaxPerRoute = defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_PER_ROUTE); }
    public void setConnectionsMaxTotal(String value) { connectionsMaxTotal = defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_TOTAL); }

    public HttpClientInputs build() {
        return HttpClientInputs.builder()
                .url(url)
                .authType(authType)
                .preemptiveAuth(BOOLEAN_TRUE)
                .username(username)
                .password(password)
                .method(method)
                .body(body)
                .contentType(contentType)
                .headers(headers)
                .responseCharacterSet(responseCharacterSet)
                .requestCharacterSet(UTF_8)
                .followRedirects(BOOLEAN_TRUE)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .trustAllRoots(trustAllRoots)
                .x509HostnameVerifier(x509HostnameVerifier)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .keystore(keystore)
                .keystorePassword(keystorePassword)
                .connectTimeout(connectTimeout)
                .responseTimeout(responseTimeout)
                .executionTimeout(ZERO)
                .useCookies(BOOLEAN_TRUE)
                .keepAlive(keepAlive)
                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                .connectionsMaxTotal(connectionsMaxTotal)
                .queryParams(queryParams)
                .queryParamsAreURLEncoded(BOOLEAN_FALSE)
                .queryParamsAreFormEncoded(BOOLEAN_FALSE)
                .formParams("")
                .formParamsAreURLEncoded(BOOLEAN_FALSE)
                .sourceFile("")
                .destinationFile("")
                .multipartBodies("")
                .multipartFiles("")
                .multipartValuesAreURLEncoded(BOOLEAN_FALSE)
                .multipartBodiesContentType(DEFAULT_MULTIPART_BODIES_CONTENT_TYPE)
                .multipartFilesContentType(DEFAULT_MULTIPART_FILES_CONTENT_TYPE)
                .tlsVersion(TLSv12)
                .allowedCiphers(DEFAULT_ALLOWED_CIPHERS_TLSv1_2)
                .build();
    }
}
