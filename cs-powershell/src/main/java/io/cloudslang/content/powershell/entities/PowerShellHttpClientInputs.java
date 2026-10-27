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

package io.cloudslang.content.powershell.entities;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.Constants;

import static io.cloudslang.content.httpclient.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class PowerShellHttpClientInputs {
    private String url = "";
    private String username = "";
    private String password = "";
    private String authType = ANONYMOUS;
    private String kerberosConfFile = "";
    private String kerberosLoginConfFile = "";
    private String kerberosSkipPortForLookup = BOOLEAN_TRUE;
    private String proxyHost = "";
    private String proxyPort = "";
    private String proxyUsername = "";
    private String proxyPassword = "";
    private String trustAllRoots = BOOLEAN_FALSE;
    private String x509HostnameVerifier = STRICT;
    private String trustKeystore = DEFAULT_JAVA_KEYSTORE;
    private String trustPassword = Constants.Others.CHANGEIT;
    private String keystore = DEFAULT_JAVA_KEYSTORE;
    private String keystorePassword = Constants.Others.CHANGEIT;
    private String headers = "";
    private String method = "POST";
    private String body = "";

    public void setUrl(String value) { url = defaultIfEmpty(value, ""); }
    public void setUsername(String value) { username = defaultIfEmpty(value, ""); }
    public void setPassword(String value) { password = defaultIfEmpty(value, ""); }
    public void setAuthType(String value) { authType = defaultIfEmpty(value, ANONYMOUS); }
    public void setKerberosConfFile(String value) { kerberosConfFile = defaultIfEmpty(value, ""); }
    public void setKerberosLoginConfFile(String value) { kerberosLoginConfFile = defaultIfEmpty(value, ""); }
    public void setKerberosSkipPortCheck(String value) { kerberosSkipPortForLookup = defaultIfEmpty(value, BOOLEAN_TRUE); }
    public void setTrustAllRoots(String value) { trustAllRoots = defaultIfEmpty(value, BOOLEAN_FALSE); }
    public void setX509HostnameVerifier(String value) { x509HostnameVerifier = defaultIfEmpty(value, STRICT); }
    public void setProxyHost(String value) { proxyHost = defaultIfEmpty(value, ""); }
    public void setProxyPort(String value) { proxyPort = defaultIfEmpty(value, ""); }
    public void setProxyUsername(String value) { proxyUsername = defaultIfEmpty(value, ""); }
    public void setProxyPassword(String value) { proxyPassword = defaultIfEmpty(value, ""); }
    public void setKeystore(String value) { keystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setKeystorePassword(String value) { keystorePassword = defaultIfEmpty(value, Constants.Others.CHANGEIT); }
    public void setTrustKeystore(String value) { trustKeystore = defaultIfEmpty(value, DEFAULT_JAVA_KEYSTORE); }
    public void setTrustPassword(String value) { trustPassword = defaultIfEmpty(value, Constants.Others.CHANGEIT); }
    public void setHeaders(String value) { headers = defaultIfEmpty(value, ""); }
    public void setMethod(String value) { method = defaultIfEmpty(value, "POST"); }
    public void setBody(String value) { body = defaultIfEmpty(value, ""); }

    public String getUrl() { return url; }
    public String getHeaders() { return headers; }
    public String getBody() { return body; }

    public HttpClientInputs build() {
        if ("KERBEROS".equalsIgnoreCase(authType)) {
            if (!kerberosConfFile.isEmpty()) {
                System.setProperty("java.security.krb5.conf", kerberosConfFile);
            }
            if (!kerberosLoginConfFile.isEmpty()) {
                System.setProperty("java.security.auth.login.config", kerberosLoginConfFile);
            }
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        }
        return HttpClientInputs.builder()
                .url(url).authType(authType).preemptiveAuth(BOOLEAN_FALSE)
                .username(username).password(password).method(method).body(body)
                .contentType("").headers(headers).requestCharacterSet(UTF_8).responseCharacterSet(UTF_8)
                .followRedirects(BOOLEAN_TRUE).proxyHost(proxyHost).proxyPort(proxyPort)
                .proxyUsername(proxyUsername).proxyPassword(proxyPassword)
                .trustAllRoots(trustAllRoots).x509HostnameVerifier(x509HostnameVerifier)
                .trustKeystore(trustKeystore).trustPassword(trustPassword)
                .keystore(keystore).keystorePassword(keystorePassword)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT).responseTimeout(DEFAULT_RESPONSE_TIMEOUT)
                .executionTimeout(ZERO).useCookies(BOOLEAN_TRUE).keepAlive(BOOLEAN_FALSE)
                .connectionsMaxPerRoute(DEFAULT_CONNECTIONS_MAX_PER_ROUTE)
                .connectionsMaxTotal(DEFAULT_CONNECTIONS_MAX_TOTAL)
                .queryParams("").queryParamsAreURLEncoded(BOOLEAN_FALSE)
                .queryParamsAreFormEncoded(BOOLEAN_TRUE).formParams("")
                .formParamsAreURLEncoded(BOOLEAN_FALSE).sourceFile("").destinationFile("")
                .multipartBodies("").multipartFiles("").multipartValuesAreURLEncoded(BOOLEAN_FALSE)
                .multipartBodiesContentType(DEFAULT_MULTIPART_BODIES_CONTENT_TYPE)
                .multipartFilesContentType(DEFAULT_MULTIPART_FILES_CONTENT_TYPE)
                .tlsVersion(TLSv12).allowedCiphers(DEFAULT_ALLOWED_CIPHERS_TLSv1_2).build();
    }
}
