/*
 * Copyright 2022-2025 Open Text
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


package io.cloudslang.content.httpclient.entities;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.cloudslang.content.httpclient.utils.Constants.ANONYMOUS;
import static org.apache.hc.client5.http.auth.StandardAuthScheme.*;

public class CustomRequestConfig {

    public static RequestConfig getDefaultRequestConfig(HttpClientInputs httpClientInputs) {
        RequestConfig.Builder requestConfigBuilder;
        String authType = httpClientInputs.getAuthType().toUpperCase();

        requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getConnectTimeout()))))
                .setResponseTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getResponseTimeout()))))
                .setRedirectsEnabled(Boolean.parseBoolean(httpClientInputs.getFollowRedirects()));

        if (Boolean.parseBoolean(httpClientInputs.getKeepAlive()))
            requestConfigBuilder.setConnectionKeepAlive((TimeValue.ofSeconds(-1)));

        if (!authType.equalsIgnoreCase("ANONYMOUS")) {
            List<String> authPrefs = new ArrayList<>();
            // Order matters - add the preferred scheme first
            authPrefs.add(authType);
            // For NTLM, add fallback to Basic if needed
            if (authType.equals("NTLM")) {
                authPrefs.add("BASIC");
            }
            requestConfigBuilder.setTargetPreferredAuthSchemes(authPrefs);
        }

        if (!StringUtils.isEmpty(httpClientInputs.getProxyHost())) {
            requestConfigBuilder.setProxyPreferredAuthSchemes(Collections.singletonList(BASIC));
            requestConfigBuilder.setProxy(new HttpHost(httpClientInputs.getProxyHost(), Integer.parseInt(httpClientInputs.getProxyPort())));
        }
        return requestConfigBuilder.build();
    }
}
