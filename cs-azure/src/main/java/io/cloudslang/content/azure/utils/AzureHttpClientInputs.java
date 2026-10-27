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

package io.cloudslang.content.azure.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.azure.utils.Constants.Common.CONNECTIONS_MAX_PER_ROUTE_CONST;
import static io.cloudslang.content.azure.utils.Constants.Common.CONNECTIONS_MAX_TOTAL_CONST;
import static io.cloudslang.content.httpclient.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class AzureHttpClientInputs {
    private String url;
    private String authType;
    private String username;
    private String password;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String trustAllRoots;
    private String x509HostnameVerifier;
    private String trustKeystore;
    private String trustPassword;
    private String keystore;
    private String keystorePassword;
    private String connectTimeout;
    private String responseTimeout;
    private String useCookies;
    private String keepAlive;
    private String headers;
    private String responseCharacterSet;
    private String destinationFile;
    private String followRedirects;
    private String queryParams;
    private String queryParamsAreURLEncoded;
    private String queryParamsAreFormEncoded;
    private String formParams;
    private String formParamsAreURLEncoded;
    private String sourceFile;
    private String body;
    private String contentType;
    private String requestCharacterSet;
    private String multipartBodies;
    private String multipartFiles;
    private String multipartValuesAreURLEncoded;
    private String multipartBodiesContentType;
    private String multipartFilesContentType;
    private String method;
    private String tlsVersion;
    private String allowedCiphers;

    public void setUrl(String url) { this.url = url; }
    public void setAuthType(String authType) { this.authType = authType; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setProxyHost(String proxyHost) { this.proxyHost = proxyHost; }
    public void setProxyPort(String proxyPort) { this.proxyPort = proxyPort; }
    public void setProxyUsername(String proxyUsername) { this.proxyUsername = proxyUsername; }
    public void setProxyPassword(String proxyPassword) { this.proxyPassword = proxyPassword; }
    public void setTrustAllRoots(String trustAllRoots) { this.trustAllRoots = trustAllRoots; }
    public void setX509HostnameVerifier(String x509HostnameVerifier) { this.x509HostnameVerifier = x509HostnameVerifier; }
    public void setTrustKeystore(String trustKeystore) { this.trustKeystore = trustKeystore; }
    public void setTrustPassword(String trustPassword) { this.trustPassword = trustPassword; }
    public void setKeystore(String keystore) { this.keystore = keystore; }
    public void setKeystorePassword(String keystorePassword) { this.keystorePassword = keystorePassword; }
    public void setConnectTimeout(String connectTimeout) { this.connectTimeout = connectTimeout; }
    public void setSocketTimeout(String responseTimeout) { this.responseTimeout = responseTimeout; }
    public void setUseCookies(String useCookies) { this.useCookies = useCookies; }
    public void setKeepAlive(String keepAlive) { this.keepAlive = keepAlive; }
    public void setHeaders(String headers) { this.headers = headers; }
    public void setResponseCharacterSet(String responseCharacterSet) { this.responseCharacterSet = responseCharacterSet; }
    public void setDestinationFile(String destinationFile) { this.destinationFile = destinationFile; }
    public void setFollowRedirects(String followRedirects) { this.followRedirects = followRedirects; }
    public void setQueryParams(String queryParams) { this.queryParams = queryParams; }
    public void setQueryParamsAreURLEncoded(String queryParamsAreURLEncoded) { this.queryParamsAreURLEncoded = queryParamsAreURLEncoded; }
    public void setFormParams(String formParams) { this.formParams = formParams; }
    public void setFormParamsAreURLEncoded(String formParamsAreURLEncoded) { this.formParamsAreURLEncoded = formParamsAreURLEncoded; }
    public void setSourceFile(String sourceFile) { this.sourceFile = sourceFile; }
    public void setBody(String body) { this.body = body; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setRequestCharacterSet(String requestCharacterSet) { this.requestCharacterSet = requestCharacterSet; }
    public void setMultipartBodies(String multipartBodies) { this.multipartBodies = multipartBodies; }
    public void setMultipartFiles(String multipartFiles) { this.multipartFiles = multipartFiles; }
    public void setMultipartValuesAreURLEncoded(String multipartValuesAreURLEncoded) { this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded; }
    public void setMultipartBodiesContentType(String multipartBodiesContentType) { this.multipartBodiesContentType = multipartBodiesContentType; }
    public void setMultipartFilesContentType(String multipartFilesContentType) { this.multipartFilesContentType = multipartFilesContentType; }
    public void setMethod(String method) { this.method = method; }
    public void setTlsVersion(String tlsVersion) { this.tlsVersion = tlsVersion; }
    public void setAllowedCyphers(String allowedCiphers) { this.allowedCiphers = allowedCiphers; }

    public HttpClientInputs build() {
        String defaultKeyStore = System.getProperty("javax.net.ssl.keyStore", DEFAULT_JAVA_KEYSTORE);
        String defaultKeyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword", "changeit");
        String defaultTrustStore = System.getProperty("javax.net.ssl.trustStore", defaultKeyStore);
        String defaultTrustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword", defaultKeyStorePassword);

        return HttpClientInputs.builder()
                .url(defaultIfEmpty(url, ""))
                .authType(defaultIfEmpty(authType, ANONYMOUS))
                .preemptiveAuth(BOOLEAN_TRUE)
                .method(defaultIfEmpty(method, ""))
                .username(defaultIfEmpty(username, ""))
                .password(defaultIfEmpty(password, ""))
                .proxyHost(defaultIfEmpty(proxyHost, ""))
                .proxyPort(defaultIfEmpty(proxyPort, ""))
                .proxyUsername(defaultIfEmpty(proxyUsername, ""))
                .proxyPassword(defaultIfEmpty(proxyPassword, ""))
                .tlsVersion(defaultIfEmpty(tlsVersion, TLSv12))
                .allowedCiphers(defaultIfEmpty(allowedCiphers, DEFAULT_ALLOWED_CIPHERS_TLSv1_2))
                .trustAllRoots(defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE))
                .x509HostnameVerifier(defaultIfEmpty(x509HostnameVerifier, STRICT))
                .trustKeystore(defaultIfEmpty(trustKeystore, defaultTrustStore))
                .trustPassword(defaultIfEmpty(trustPassword, defaultTrustStorePassword))
                .keystore(defaultIfEmpty(keystore, defaultKeyStore))
                .keystorePassword(defaultIfEmpty(keystorePassword, defaultKeyStorePassword))
                .connectTimeout(defaultIfEmpty(connectTimeout, DEFAULT_CONNECT_TIMEOUT))
                .responseTimeout(defaultIfEmpty(responseTimeout, DEFAULT_RESPONSE_TIMEOUT))
                .executionTimeout(DEFAULT_EXECUTION_TIMEOUT)
                .useCookies(defaultIfEmpty(useCookies, BOOLEAN_TRUE))
                .keepAlive(defaultIfEmpty(keepAlive, BOOLEAN_FALSE))
                .connectionsMaxPerRoute(CONNECTIONS_MAX_PER_ROUTE_CONST)
                .connectionsMaxTotal(CONNECTIONS_MAX_TOTAL_CONST)
                .headers(defaultIfEmpty(headers, ""))
                .responseCharacterSet(defaultIfEmpty(responseCharacterSet, UTF_8))
                .destinationFile(defaultIfEmpty(destinationFile, ""))
                .followRedirects(defaultIfEmpty(followRedirects, BOOLEAN_TRUE))
                .queryParams(defaultIfEmpty(queryParams, ""))
                .queryParamsAreURLEncoded(defaultIfEmpty(queryParamsAreURLEncoded, BOOLEAN_FALSE))
                .queryParamsAreFormEncoded(defaultIfEmpty(queryParamsAreFormEncoded, BOOLEAN_FALSE))
                .formParams(defaultIfEmpty(formParams, ""))
                .formParamsAreURLEncoded(defaultIfEmpty(formParamsAreURLEncoded, BOOLEAN_FALSE))
                .sourceFile(defaultIfEmpty(sourceFile, ""))
                .body(defaultIfEmpty(body, ""))
                .contentType(defaultIfEmpty(contentType, ""))
                .requestCharacterSet(defaultIfEmpty(requestCharacterSet, UTF_8))
                .multipartBodies(defaultIfEmpty(multipartBodies, ""))
                .multipartFiles(defaultIfEmpty(multipartFiles, ""))
                .multipartValuesAreURLEncoded(defaultIfEmpty(multipartValuesAreURLEncoded, BOOLEAN_FALSE))
                .multipartBodiesContentType(defaultIfEmpty(multipartBodiesContentType, ""))
                .multipartFilesContentType(defaultIfEmpty(multipartFilesContentType, ""))
                .build();
    }
}
