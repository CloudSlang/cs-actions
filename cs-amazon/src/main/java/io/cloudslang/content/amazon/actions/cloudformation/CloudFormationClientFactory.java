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
package io.cloudslang.content.amazon.actions.cloudformation;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;

public class CloudFormationClientFactory {
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

        ClientConfiguration clientConf = new ClientConfiguration()
                .withConnectionTimeout(Integer.parseInt(connectTimeoutMs))
                .withProxyHost(proxyHost)
                .withProxyPort(Integer.parseInt(proxyPort))
                .withProxyUsername(proxyUsername)
                .withProxyPassword(proxyPassword)
                .withClientExecutionTimeout(Integer.parseInt(executionTimeoutMs));

        return AmazonCloudFormationClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .withClientConfiguration(clientConf)
                .build();
    }
}
