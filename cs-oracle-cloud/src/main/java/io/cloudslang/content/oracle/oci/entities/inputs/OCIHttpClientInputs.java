/*
 * Copyright 2020-2025 Open Text
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

package io.cloudslang.content.oracle.oci.entities.inputs;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.httpclient.utils.Constants.*;
import static io.cloudslang.content.oracle.oci.utils.Constants.Common.ALLOWED_CYPHERS;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class OCIHttpClientInputs {
    private String url = "";
    private String authType = ANONYMOUS;
    private String method = GET;
    private String body = "";
    private String headers = "";
    private String queryParams = "";
    private String proxyHost = "";
    private String proxyPort = "";
    private String proxyUsername = "";
    private String proxyPassword = "";
    private String connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private String responseTimeout = DEFAULT_RESPONSE_TIMEOUT;
    private String keepAlive = BOOLEAN_FALSE;
    private String connectionsMaxPerRoute = DEFAULT_CONNECTIONS_MAX_PER_ROUTE;
    private String connectionsMaxTotal = DEFAULT_CONNECTIONS_MAX_TOTAL;
    private String tlsVersion = TLSv12;
    private String allowedCiphers = ALLOWED_CYPHERS;

    public void setUrl(String value) { url = defaultIfEmpty(value, ""); }
    public void setAuthType(String value) { authType = defaultIfEmpty(value, ANONYMOUS); }
    public void setMethod(String value) { method = defaultIfEmpty(value, GET); }
    public void setBody(String value) { body = defaultIfEmpty(value, ""); }
    public void setHeaders(String value) { headers = defaultIfEmpty(value, ""); }
    public void setQueryParams(String value) { queryParams = defaultIfEmpty(value, ""); }
    public void setProxyHost(String value) { proxyHost = defaultIfEmpty(value, ""); }
    public void setProxyPort(String value) { proxyPort = defaultIfEmpty(value, ""); }
    public void setProxyUsername(String value) { proxyUsername = defaultIfEmpty(value, ""); }
    public void setProxyPassword(String value) { proxyPassword = defaultIfEmpty(value, ""); }
    public void setConnectTimeout(String value) { connectTimeout = defaultIfEmpty(value, DEFAULT_CONNECT_TIMEOUT); }
    public void setSocketTimeout(String value) { responseTimeout = defaultIfEmpty(value, DEFAULT_RESPONSE_TIMEOUT); }
    public void setKeepAlive(String value) { keepAlive = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setConnectionsMaxPerRoute(String value) { connectionsMaxPerRoute = defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_PER_ROUTE); }
    public void setConnectionsMaxTotal(String value) { connectionsMaxTotal = defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_TOTAL); }
    public void setTlsVersion(String value) { tlsVersion = defaultIfEmpty(value, TLSv12); }
    public void setAllowedCiphers(String value) { allowedCiphers = defaultIfEmpty(value, ALLOWED_CYPHERS); }

    public String getUrl() { return url; }
    public String getBody() { return body; }
    public String getQueryParams() { return queryParams; }

    public HttpClientInputs build() {
        return HttpClientInputs.builder()
                .url(url)
                .authType(authType)
                .preemptiveAuth(BOOLEAN_TRUE)
                .username("")
                .password("")
                .method(method)
                .body(body)
                .contentType("")
                .headers(headers)
                .responseCharacterSet(UTF_8)
                .requestCharacterSet(UTF_8)
                .followRedirects(BOOLEAN_TRUE)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .trustAllRoots(BOOLEAN_FALSE)
                .x509HostnameVerifier(STRICT)
                .trustKeystore(DEFAULT_JAVA_KEYSTORE)
                .trustPassword("changeit")
                .keystore(DEFAULT_JAVA_KEYSTORE)
                .keystorePassword("changeit")
                .connectTimeout(connectTimeout)
                .responseTimeout(responseTimeout)
                .executionTimeout(ZERO)
                .useCookies(BOOLEAN_TRUE)
                .keepAlive(keepAlive)
                .connectionsMaxPerRoute(connectionsMaxPerRoute)
                .connectionsMaxTotal(connectionsMaxTotal)
                .queryParams(queryParams)
                .queryParamsAreURLEncoded(BOOLEAN_FALSE)
                .queryParamsAreFormEncoded(BOOLEAN_TRUE)
                .formParams("")
                .formParamsAreURLEncoded(BOOLEAN_FALSE)
                .sourceFile("")
                .destinationFile("")
                .multipartBodies("")
                .multipartFiles("")
                .multipartValuesAreURLEncoded(BOOLEAN_FALSE)
                .multipartBodiesContentType(DEFAULT_MULTIPART_BODIES_CONTENT_TYPE)
                .multipartFilesContentType(DEFAULT_MULTIPART_FILES_CONTENT_TYPE)
                .tlsVersion(tlsVersion)
                .allowedCiphers(allowedCiphers)
                .build();
    }
}
