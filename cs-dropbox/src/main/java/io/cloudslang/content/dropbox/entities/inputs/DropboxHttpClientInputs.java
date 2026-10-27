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

package io.cloudslang.content.dropbox.entities.inputs;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.httpclient.utils.Constants.*;

public class DropboxHttpClientInputs {
    private final HttpClientInputs.HttpClientInputsBuilder builder = HttpClientInputs.builder();
    private String method;

    public DropboxHttpClientInputs() {
        builder.authType(ANONYMOUS)
                .preemptiveAuth(BOOLEAN_TRUE)
                .username("")
                .password("")
                .proxyHost("")
                .proxyPort("")
                .proxyUsername("")
                .proxyPassword("")
                .tlsVersion(TLSv12)
                .allowedCiphers(DEFAULT_ALLOWED_CIPHERS_TLSv1_2)
                .trustAllRoots(BOOLEAN_FALSE)
                .x509HostnameVerifier(ALLOW_ALL)
                .trustKeystore(DEFAULT_JAVA_KEYSTORE)
                .trustPassword("changeit")
                .keystore(DEFAULT_JAVA_KEYSTORE)
                .keystorePassword("changeit")
                .connectTimeout("0")
                .responseTimeout("0")
                .executionTimeout("0")
                .useCookies(BOOLEAN_TRUE)
                .keepAlive(BOOLEAN_TRUE)
                .connectionsMaxPerRoute(DEFAULT_CONNECTIONS_MAX_PER_ROUTE)
                .connectionsMaxTotal(DEFAULT_CONNECTIONS_MAX_TOTAL)
                .headers("")
                .responseCharacterSet(UTF_8)
                .destinationFile("")
                .followRedirects(BOOLEAN_TRUE)
                .queryParams("")
                .queryParamsAreURLEncoded(BOOLEAN_FALSE)
                .queryParamsAreFormEncoded(BOOLEAN_FALSE)
                .formParams("")
                .formParamsAreURLEncoded(BOOLEAN_FALSE)
                .sourceFile("")
                .body("")
                .contentType("")
                .requestCharacterSet(UTF_8)
                .multipartBodies("")
                .multipartFiles("")
                .multipartValuesAreURLEncoded(BOOLEAN_FALSE)
                .multipartBodiesContentType("")
                .multipartFilesContentType("");
    }

    public void setUrl(String url) { builder.url(url); }
    public void setAuthType(String authType) { builder.authType(authType); }
    public void setMethod(String method) {
        this.method = method;
        builder.method(method);
    }
    public String getMethod() { return method; }
    public void setQueryParamsAreURLEncoded(String value) { builder.queryParamsAreURLEncoded(value); }
    public void setTrustAllRoots(String value) { builder.trustAllRoots(value); }
    public void setKeepAlive(String value) { builder.keepAlive(value); }
    public void setUseCookies(String value) { builder.useCookies(value); }
    public void setProxyHost(String value) { builder.proxyHost(value); }
    public void setProxyPort(String value) { builder.proxyPort(value); }
    public void setProxyUsername(String value) { builder.proxyUsername(value); }
    public void setProxyPassword(String value) { builder.proxyPassword(value); }
    public void setTrustKeystore(String value) { builder.trustKeystore(value); }
    public void setTrustPassword(String value) { builder.trustPassword(value); }
    public void setKeystore(String value) { builder.keystore(value); }
    public void setKeystorePassword(String value) { builder.keystorePassword(value); }
    public void setConnectTimeout(String value) { builder.connectTimeout(value); }
    public void setSocketTimeout(String value) { builder.responseTimeout(value); }
    public void setX509HostnameVerifier(String value) { builder.x509HostnameVerifier(value); }
    public void setHeaders(String value) { builder.headers(value); }
    public void setContentType(String value) { builder.contentType(value); }
    public void setBody(String value) { builder.body(value); }

    public HttpClientInputs build() {
        return builder.build();
    }
}
