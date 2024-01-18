/*
 * Copyright 2020-2024 Open Text
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



package io.cloudslang.content.sitescope.services;

import org.jetbrains.annotations.NotNull;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.sitescope.entities.SiteScopeCommonInputs;

import static io.cloudslang.content.sitescope.utils.HttpUtils.*;

public class HttpCommons {

    @NotNull
    static void setCommonHttpInputs(@NotNull final HttpClientInputs httpClientInputs,
                                    @NotNull final SiteScopeCommonInputs commonInputs) {
        setProxy(httpClientInputs,
                commonInputs.getProxyHost(),
                commonInputs.getProxyPort(),
                commonInputs.getProxyUsername(),
                commonInputs.getProxyPassword());

        setSecurityInputs(httpClientInputs,
                commonInputs.getTrustAllRoots(),
                commonInputs.getX509HostnameVerifier(),
                commonInputs.getKeystore(),
                commonInputs.getKeystorePassword(),
                commonInputs.getTrustKeystore(),
                commonInputs.getTrustPassword());

        setConnectionParameters(httpClientInputs,
                commonInputs.getConnectTimeout(),
                commonInputs.getSocketTimeout(),
                commonInputs.getKeepAlive(),
                commonInputs.getConnectionsMaxPerRoute(),
                commonInputs.getConnectionsMaxTotal(),
                commonInputs.getResponseCharacterSet());
    }
}
