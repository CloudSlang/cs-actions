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
package io.cloudslang.content.amazon.factory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import org.apache.commons.lang3.StringUtils;

public class CloudFormationClientBuilder {
    private CloudFormationClientBuilder() {
    }

    public static AmazonCloudFormation getCloudFormationClient(
            String accessKeyId,
            String secretAccessKey,
            String proxyHost,
            String proxyPort,
            String proxyUsername,
            String proxyPassword,
            String connectTimeoutMs,
            String executionTimeoutMs,
            String region) {

        connectTimeoutMs = StringUtils.defaultIfEmpty(connectTimeoutMs,"0");
        executionTimeoutMs = StringUtils.defaultIfEmpty(executionTimeoutMs,"0");

        ClientConfiguration clientConf = new ClientConfiguration().
                withConnectionTimeout(Integer.parseInt(connectTimeoutMs))
                .withClientExecutionTimeout(Integer.parseInt(executionTimeoutMs));

        if (!StringUtils.isEmpty(proxyHost)) {
            clientConf.setProxyHost(proxyHost);
            clientConf.setProxyPort(Integer.parseInt(proxyPort));
            clientConf.setProxyUsername(proxyUsername);
            clientConf.setProxyPassword(proxyPassword);
        }

        return AmazonCloudFormationClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .withClientConfiguration(clientConf)
                .build();
    }
}
