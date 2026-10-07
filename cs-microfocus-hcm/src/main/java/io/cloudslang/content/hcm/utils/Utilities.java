/*
 * Copyright 2019-2024 Open Text
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
import io.cloudslang.content.httpclient.entities.HttpClientInputs.HttpClientInputsBuilder;
import org.jetbrains.annotations.NotNull;

public class Utilities {

    public static void setInput(@NotNull final HttpClientInputsBuilder httpClientInputs,
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
        httpClientInputs.url(url);
        httpClientInputs.authType(authType);
        httpClientInputs.username(username);
        httpClientInputs.password(password);
        httpClientInputs.connectTimeout(connectTimeout);
        httpClientInputs.responseTimeout(socketTimeout);
        httpClientInputs.useCookies(useCookies);
        httpClientInputs.keepAlive(keepAlive);
        httpClientInputs.queryParams(queryParams);
        httpClientInputs.contentType(contentType);
        httpClientInputs.followRedirects(followRedirects);
        httpClientInputs.method(method);
        httpClientInputs.proxyHost(proxyHost);
        httpClientInputs.proxyPort(proxyPort);
        httpClientInputs.proxyUsername(proxyUsername);
        httpClientInputs.proxyPassword(proxyPassword);
        httpClientInputs.trustAllRoots(trustAllRoots);
        httpClientInputs.x509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.trustKeystore(trustKeystore);
        httpClientInputs.trustPassword(trustPassword);
        httpClientInputs.keystore(keystore);
        httpClientInputs.keystorePassword(keystorePassword);
    }

}
