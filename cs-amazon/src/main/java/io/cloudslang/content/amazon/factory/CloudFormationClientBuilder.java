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



package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.utils.AmazonWebServiceClientUtil;
import io.cloudslang.content.amazon.utils.DefaultValues;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CloudFormationClientBuilder {
    private CloudFormationClientBuilder() {
    }

    public static CloudFormationClient getCloudFormationClient(
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

        var credentials = StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey));

        return CloudFormationClient.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .credentialsProvider(credentials)
                .httpClientBuilder(AmazonWebServiceClientUtil.getHttpClientBuilder(proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeoutMs))
                .overrideConfiguration(AmazonWebServiceClientUtil.getClientOverrideConfiguration(executionTimeoutMs))
                .build();
    }

}
