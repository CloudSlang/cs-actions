/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
package io.cloudslang.content.amazon.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rds.AmazonRDS;
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder;
import com.amazonaws.services.rds.AmazonRDSClientBuilder;
import org.apache.commons.lang3.StringUtils;

public class RDSClientBuilder {

    public static AmazonRDS getRDSClientBuilder(
            String accessKeyId,
            String secretAccessKey,
            String proxyHost,
            Integer proxyPort,
            String proxyUsername,
            String proxyPassword,
            Integer connectTimeoutMs,
            Integer executionTimeoutMs,
            String region,
            boolean async) {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(connectTimeoutMs);
        clientConfiguration.setClientExecutionTimeout(executionTimeoutMs);

        if (!StringUtils.isEmpty(proxyHost)) {
            clientConfiguration.setProxyHost(proxyHost);
            clientConfiguration.setProxyPort(proxyPort);
            if (!StringUtils.isEmpty(proxyUsername)) {
                clientConfiguration.setProxyUsername(proxyUsername);
                clientConfiguration.setProxyPassword(proxyPassword);
            }
        }
        if (!async) {
            return AmazonRDSClientBuilder.standard().withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                    .withClientConfiguration(clientConfiguration)
                    .build();
        }
        return AmazonRDSAsyncClientBuilder.standard().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .withClientConfiguration(clientConfiguration)
                .build();
    }
}
