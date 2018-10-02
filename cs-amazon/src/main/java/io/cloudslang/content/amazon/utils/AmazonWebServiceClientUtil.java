/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.amazon.utils;

import com.amazonaws.ClientConfiguration;
import org.apache.commons.lang3.StringUtils;

public class AmazonWebServiceClientUtil {
    public static ClientConfiguration getClientConfiguration(String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String connectTimeoutMs, String executionTimeoutMs) {
        ClientConfiguration clientConf = new ClientConfiguration()
                .withConnectionTimeout(Integer.parseInt(connectTimeoutMs))
                .withClientExecutionTimeout(Integer.parseInt(executionTimeoutMs));

        if (!StringUtils.isEmpty(proxyHost)) {
            clientConf.setProxyHost(proxyHost);
        }

        if (!StringUtils.isEmpty(proxyPort)) {
            clientConf.setProxyPort(Integer.parseInt(proxyPort));
        }

        if (!StringUtils.isEmpty(proxyUsername)) {
            clientConf.setProxyUsername(proxyUsername);
        }

        if (!StringUtils.isEmpty(proxyPassword)) {
            clientConf.setProxyPassword(proxyPassword);
        }

        return clientConf;
    }
}
