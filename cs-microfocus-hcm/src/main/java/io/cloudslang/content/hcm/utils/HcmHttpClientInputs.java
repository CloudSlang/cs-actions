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

package io.cloudslang.content.hcm.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;

import static io.cloudslang.content.hcm.utils.DefaultValues.DEFAULT_JAVA_KEYSTORE;
import static io.cloudslang.content.hcm.utils.DefaultValues.DEFAULT_JAVA_KEYSTORE_PASSWORD;
import static io.cloudslang.content.httpclient.utils.Constants.ANONYMOUS;
import static io.cloudslang.content.httpclient.utils.Constants.BOOLEAN_FALSE;
import static io.cloudslang.content.httpclient.utils.Constants.BOOLEAN_TRUE;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_ALLOWED_CIPHERS_TLSv1_2;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_CONNECT_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Constants.DEFAULT_RESPONSE_TIMEOUT;
import static io.cloudslang.content.httpclient.utils.Constants.GET;
import static io.cloudslang.content.httpclient.utils.Constants.STRICT;
import static io.cloudslang.content.httpclient.utils.Constants.TLSv12;
import static io.cloudslang.content.httpclient.utils.Constants.UTF_8;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class HcmHttpClientInputs {
    private String url = "";
    private String authType = ANONYMOUS;
    private String preemptiveAuth = BOOLEAN_TRUE;
    private String username = "";
    private String password = "";
    private String connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    private String responseTimeout = DEFAULT_RESPONSE_TIMEOUT;
    private String useCookies = BOOLEAN_TRUE;
    private String keepAlive = BOOLEAN_FALSE;
    private String queryParams = "";
    private String contentType = "";
    private String followRedirects = BOOLEAN_TRUE;
    private String method = GET;
    private String proxyHost = "";
    private String proxyPort = "";
    private String proxyUsername = "";
    private String proxyPassword = "";
    private String trustAllRoots = BOOLEAN_FALSE;
    private String x509HostnameVerifier = STRICT;
    private String trustKeystore = DEFAULT_JAVA_KEYSTORE;
    private String trustPassword = DEFAULT_JAVA_KEYSTORE_PASSWORD;
    private String keystore = DEFAULT_JAVA_KEYSTORE;
    private String keystorePassword = DEFAULT_JAVA_KEYSTORE_PASSWORD;

    public void setUrl(String value) { url = value; }
    public void setAuthType(String value) { authType = defaultIfEmpty(value, ANONYMOUS); }
    public void setUsername(String value) { username = defaultIfEmpty(value, ""); }
    public void setPassword(String value) { password = defaultIfEmpty(value, ""); }
    public void setConnectTimeout(String value) { connectTimeout = defaultIfEmpty(value, DEFAULT_CONNECT_TIMEOUT); }
    public void setSocketTimeout(String value) { responseTimeout = defaultIfEmpty(value, DEFAULT_RESPONSE_TIMEOUT); }
    public void setUseCookies(String value) { useCookies = defaultIfEmpty(value, BOOLEAN_TRUE); }
    public void setKeepAlive(String value) { keepAlive = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setQueryParams(String value) { queryParams = defaultIfEmpty(value, ""); }
    public void setContentType(String value) { contentType = defaultIfEmpty(value, ""); }
    public void setFollowRedirects(String value) { followRedirects = defaultIfEmpty(value, BOOLEAN_TRUE); }
    public void setMethod(String value) { method = defaultIfEmpty(value, GET); }
    public void setProxyHost(String value) { proxyHost = defaultIfEmpty(value, ""); }
    public void setProxyPort(String value) { proxyPort = defaultIfEmpty(value, ""); }
    public void setProxyUsername(String value) { proxyUsername = defaultIfEmpty(value, ""); }
    public void setProxyPassword(String value) { proxyPassword = defaultIfEmpty(value, ""); }
    public void setTrustAllRoots(String value) { trustAllRoots = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setX509HostnameVerifier(String value) { x509HostnameVerifier = defaultIfEmpty(value, STRICT); }
    public void setTrustKeystore(String value) { trustKeystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setTrustPassword(String value) { trustPassword = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE_PASSWORD); }
    public void setKeystore(String value) { keystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setKeystorePassword(String value) { keystorePassword = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE_PASSWORD); }

    public HttpClientInputs build() {
        return HttpClientInputs.builder()
                .url(url)
                .authType(authType)
                .preemptiveAuth(preemptiveAuth)
                .username(username)
                .password(password)
                .connectTimeout(connectTimeout)
                .responseTimeout(responseTimeout)
                .executionTimeout("0")
                .useCookies(useCookies)
                .keepAlive(keepAlive)
                .queryParams(queryParams)
                .contentType(contentType)
                .followRedirects(followRedirects)
                .method(method)
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
                .tlsVersion(TLSv12)
                .allowedCiphers(DEFAULT_ALLOWED_CIPHERS_TLSv1_2)
                .headers("")
                .responseCharacterSet(UTF_8)
                .requestCharacterSet(UTF_8)
                .destinationFile("")
                .queryParamsAreURLEncoded(BOOLEAN_FALSE)
                .queryParamsAreFormEncoded(BOOLEAN_FALSE)
                .formParams("")
                .formParamsAreURLEncoded(BOOLEAN_FALSE)
                .sourceFile("")
                .multipartBodies("")
                .multipartFiles("")
                .multipartValuesAreURLEncoded(BOOLEAN_FALSE)
                .multipartBodiesContentType("")
                .multipartFilesContentType("")
                .build();
    }
}
