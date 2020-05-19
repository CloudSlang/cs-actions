/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.utils;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.oracle.utils.Constants.Common.*;
import static java.net.Proxy.Type.HTTP;

public class HttpUtils {
    @org.jetbrains.annotations.NotNull
    public static Proxy getProxy(@org.jetbrains.annotations.NotNull final String proxyHost, final int proxyPort, @org.jetbrains.annotations.NotNull final String proxyUser, @org.jetbrains.annotations.NotNull final String proxyPassword) {
        if (StringUtilities.isBlank(proxyHost)) {
            return Proxy.NO_PROXY;
        }
        if (StringUtilities.isNotEmpty(proxyUser)) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(proxyUser, proxyPassword.toCharArray());
                }
            });
        }
        return new Proxy(HTTP, InetSocketAddress.createUnresolved(proxyHost, proxyPort));
    }

    public static void setProxy(@org.jetbrains.annotations.NotNull final HttpClientInputs httpClientInputs,
                                @org.jetbrains.annotations.NotNull final String proxyHost,
                                @org.jetbrains.annotations.NotNull final String proxyPort,
                                @org.jetbrains.annotations.NotNull final String proxyUsername,
                                @org.jetbrains.annotations.NotNull final String proxyPassword) {
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
    }

    @NotNull
    public static URIBuilder getUriBuilder() {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(OCI_HOST);
        uriBuilder.setScheme(HTTPS);
        return uriBuilder;
    }


    @NotNull
    public static String getQueryParams(@NotNull final String compartmentId) {
        final StringBuilder queryParams = new StringBuilder()
                .append(QUERY)
                .append(COMPARTMENT_ID)
                .append(compartmentId);
        return queryParams.toString();
    }

    @NotNull
    public static Map<String, String> getFailureResults(@NotNull String inputName, @NotNull Integer statusCode, @NotNull String throwable) {
        Map<String, String> results = new HashMap();
        results.put("returnCode", "-1");
        results.put("statusCode",statusCode.toString());
        if (statusCode.equals(404)) {
            results.put("returnResult", inputName + " not found, or user unauthorized to perform action");
            results.put("exception ", "status : " + statusCode + ", Title :  " + inputName + " not found, or user unauthorized to perform action");
        } else {
            results.put("returnResult", throwable);
            results.put("exception", throwable);
        }
        return results;
    }

}
