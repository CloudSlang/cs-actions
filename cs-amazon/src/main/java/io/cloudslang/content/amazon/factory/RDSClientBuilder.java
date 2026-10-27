/*
 * Copyright 2026-2027 Open Text
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
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rds.RdsClient;

public class RDSClientBuilder {
    public static RdsClient getRDSClientBuilder(
            String accessKeyId, String secretAccessKey, String proxyHost, Integer proxyPort,
            String proxyUsername, String proxyPassword, Integer connectTimeoutMs,
            Integer executionTimeoutMs, String region, boolean async) {
        return RdsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .httpClientBuilder(AmazonWebServiceClientUtil.getHttpClientBuilder(
                        proxyHost, proxyPort == null ? null : proxyPort.toString(), proxyUsername, proxyPassword,
                        connectTimeoutMs.toString()))
                .overrideConfiguration(AmazonWebServiceClientUtil.getClientOverrideConfiguration(executionTimeoutMs.toString()))
                .build();
    }
}
