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


package io.cloudslang.content.amazon.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.AWSServiceCatalogAsyncClientBuilder;
import com.amazonaws.services.servicecatalog.AWSServiceCatalogClientBuilder;
import org.apache.commons.lang3.StringUtils;

public class ServiceCatalogClientBuilder {
    public static AWSServiceCatalog getServiceCatalogClientBuilder(
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
            return AWSServiceCatalogClientBuilder.standard()
                    .withRegion(region)
                    .withClientConfiguration(clientConfiguration)
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                    .build();
        }
        return AWSServiceCatalogAsyncClientBuilder.standard()
                .withRegion(region)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();


    }
}
