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
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder;
import io.cloudslang.content.amazon.utils.AmazonWebServiceClientUtil;
import io.cloudslang.content.amazon.utils.DefaultValues;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

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

        connectTimeoutMs = defaultIfEmpty(connectTimeoutMs, DefaultValues.CONNECT_TIMEOUT);
        executionTimeoutMs = defaultIfEmpty(executionTimeoutMs, DefaultValues.EXEC_TIMEOUT);

        ClientConfiguration clientConf = AmazonWebServiceClientUtil.getClientConfiguration(proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeoutMs, executionTimeoutMs);

        return AmazonCloudFormationClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .withClientConfiguration(clientConf)
                .build();
    }

}
