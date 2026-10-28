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

package io.cloudslang.content.hashicorp.terraform.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.CHANGEIT;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.STRICT;
import static io.cloudslang.content.httpclient.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class TerraformHttpClientInputs {
    private final HttpClientInputs.HttpClientInputsBuilder builder = HttpClientInputs.builder();
    private final SerializableSessionObject cookieStoreSessionObject = new SerializableSessionObject();
    private final GlobalSessionObject<?> connectionPoolSessionObject = new GlobalSessionObject<>();

    public TerraformHttpClientInputs() {
        builder.url("")
                .cookieStoreSessionObject(cookieStoreSessionObject)
                .connectionPoolSessionObject(connectionPoolSessionObject)
                .authType(ANONYMOUS)
                .preemptiveAuth(BOOLEAN_TRUE)
                .username("")
                .password("")
                .proxyHost("")
                .proxyPort("")
                .proxyUsername("")
                .proxyPassword("")
                .trustAllRoots(BOOLEAN_FALSE)
                .x509HostnameVerifier(STRICT)
                .trustKeystore(DEFAULT_JAVA_KEYSTORE)
                .trustPassword(CHANGEIT)
                .keystore(DEFAULT_JAVA_KEYSTORE)
                .keystorePassword(CHANGEIT)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT)
                .responseTimeout(DEFAULT_RESPONSE_TIMEOUT)
                .executionTimeout("0")
                .useCookies(BOOLEAN_TRUE)
                .keepAlive(BOOLEAN_FALSE)
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

    public void setUrl(String value) { builder.url(value); }
    public void setAuthType(String value) { builder.authType(defaultIfEmpty(value, ANONYMOUS)); }
    public void setMethod(String value) { builder.method(defaultIfEmpty(value, GET)); }
    public void setHeaders(String value) { builder.headers(defaultIfEmpty(value, "")); }
    public void setBody(String value) { builder.body(defaultIfEmpty(value, "")); }
    public void setContentType(String value) { builder.contentType(defaultIfEmpty(value, "")); }
    public void setQueryParams(String value) { builder.queryParams(defaultIfEmpty(value, "")); }
    public void setResponseCharacterSet(String value) { builder.responseCharacterSet(defaultIfEmpty(value, UTF_8)); }
    public void setProxyHost(String value) { builder.proxyHost(defaultIfEmpty(value, "")); }
    public void setProxyPort(String value) { builder.proxyPort(defaultIfEmpty(value, "")); }
    public void setProxyUsername(String value) { builder.proxyUsername(defaultIfEmpty(value, "")); }
    public void setProxyPassword(String value) { builder.proxyPassword(defaultIfEmpty(value, "")); }
    public void setTrustAllRoots(String value) { builder.trustAllRoots(defaultIfEmpty(value, BOOLEAN_FALSE)); }
    public void setX509HostnameVerifier(String value) { builder.x509HostnameVerifier(defaultIfEmpty(value, STRICT)); }
    public void setTrustKeystore(String value) { builder.trustKeystore(defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE)); }
    public void setTrustPassword(String value) { builder.trustPassword(defaultIfEmpty(value, CHANGEIT)); }
    public void setKeystore(String value) { builder.keystore(defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE)); }
    public void setKeystorePassword(String value) { builder.keystorePassword(defaultIfEmpty(value, CHANGEIT)); }
    public void setConnectTimeout(String value) { builder.connectTimeout(defaultIfEmpty(value, DEFAULT_CONNECT_TIMEOUT)); }
    public void setSocketTimeout(String value) { builder.responseTimeout(defaultIfEmpty(value, DEFAULT_RESPONSE_TIMEOUT)); }
    public void setKeepAlive(String value) { builder.keepAlive(defaultIfEmpty(value, BOOLEAN_FALSE)); }
    public void setConnectionsMaxPerRoute(String value) { builder.connectionsMaxPerRoute(defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_PER_ROUTE)); }
    public void setConnectionsMaxTotal(String value) { builder.connectionsMaxTotal(defaultIfEmpty(value, DEFAULT_CONNECTIONS_MAX_TOTAL)); }
    public void setTlsVersion(String value) { builder.tlsVersion(defaultIfEmpty(value, TLSv12)); }
    public void setAllowedCyphers(String value) { builder.allowedCiphers(defaultIfEmpty(value, DEFAULT_ALLOWED_CIPHERS_TLSv1_2)); }

    public HttpClientInputs build() {
        return builder.build();
    }
}
