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
package io.cloudslang.content.amazon.utils;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.http.apache.ProxyConfiguration;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;

import java.net.URI;
import java.time.Duration;

public class AmazonWebServiceClientUtil {
    public static ApacheHttpClient.Builder getHttpClientBuilder(
            String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String connectTimeoutMs) {
        ApacheHttpClient.Builder builder = ApacheHttpClient.builder()
                .connectionTimeout(Duration.ofMillis(Long.parseLong(connectTimeoutMs)));
        if (StringUtils.isNotEmpty(proxyHost)) {
            String port = StringUtils.defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
            ProxyConfiguration.Builder proxy = ProxyConfiguration.builder()
                    .endpoint(URI.create("http://" + proxyHost + ":" + port));
            if (StringUtils.isNotEmpty(proxyUsername)) {
                proxy.username(proxyUsername);
                proxy.password(proxyPassword);
            }
            builder.proxyConfiguration(proxy.build());
        }
        return builder;
    }

    public static ClientOverrideConfiguration getClientOverrideConfiguration(String executionTimeoutMs) {
        return ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMillis(Long.parseLong(executionTimeoutMs)))
                .build();
    }
}
