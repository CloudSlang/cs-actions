/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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



package io.cloudslang.content.azure.utils;

import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

import static io.cloudslang.content.azure.utils.Constants.PROXY_HTTP_PASSWORD;
import static io.cloudslang.content.azure.utils.Constants.PROXY_HTTP_USER;
import static java.net.Proxy.Type.HTTP;

/**
 * Created by victor on 31.10.2016.
 */
public class HttpUtils {
    @NotNull
    public static Proxy getProxy(@NotNull final String proxyHost, final int proxyPort, @NotNull final String proxyUser, @NotNull final String proxyPassword) {
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
}
