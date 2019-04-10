/*
 * (c) Copyright 2019 Micro Focus, L.P.
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


package io.cloudslang.content.hcm.utils;


import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.jetbrains.annotations.NotNull;

public class Utilities {

    public static void setInput(@NotNull final HttpClientInputs httpClientInputs,
                                @NotNull final String url,
                                @NotNull final String authType,
                                @NotNull final String username,
                                @NotNull final String password,
                                @NotNull final String connectTimeout,
                                @NotNull final String socketTimeout,
                                @NotNull final String useCookies,
                                @NotNull final String keepAlive,
                                @NotNull final String queryParams,
                                @NotNull final String contentType,
                                @NotNull final String followRedirects,
                                @NotNull final String method,
                                @NotNull final String proxyHost,
                                @NotNull final String proxyPort,
                                @NotNull final String proxyUsername,
                                @NotNull final String proxyPassword,
                                @NotNull final String trustAllRoots,
                                @NotNull final String x509HostnameVerifier,
                                @NotNull final String trustKeystore,
                                @NotNull final String trustPassword,
                                @NotNull final String keystore,
                                @NotNull final String keystorePassword) {
        httpClientInputs.setUrl(url);
        httpClientInputs.setAuthType(authType);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setQueryParams(queryParams);
        httpClientInputs.setContentType(contentType);
        httpClientInputs.setFollowRedirects(followRedirects);
        httpClientInputs.setMethod(method);
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
    }

}
