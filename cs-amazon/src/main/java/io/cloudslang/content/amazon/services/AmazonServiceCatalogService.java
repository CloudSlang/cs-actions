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
package io.cloudslang.content.amazon.services;

import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.DescribeStackResourcesRequest;
import software.amazon.awssdk.services.cloudformation.model.DescribeStackResourcesResponse;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksRequest;
import software.amazon.awssdk.services.cloudformation.model.Stack;
import software.amazon.awssdk.services.servicecatalog.ServiceCatalogClient;
import software.amazon.awssdk.services.servicecatalog.model.*;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.cloudslang.content.amazon.entities.constants.Constants.ServiceCatalogActions.CLOUD_FORMATION_STACK_NAME_REGEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.ServiceCatalogActions.FAILED;

public class AmazonServiceCatalogService {
    public static ProvisionProductResponse provisionProduct(
            String name, List<ProvisioningParameter> parameters, String productId, String token,
            String artifactId, List<Tag> tags, String acceptLanguage, String notificationArns,
            String pathId, ServiceCatalogClient client) {
        String provisionToken = StringUtils.isEmpty(token) ? generateRandomToken() : token;
        ProvisionProductRequest.Builder request = ProvisionProductRequest.builder()
                .provisionedProductName(name)
                .provisioningParameters(parameters)
                .productId(productId)
                .provisioningArtifactId(artifactId)
                .tags(tags)
                .acceptLanguage(acceptLanguage)
                .provisionToken(provisionToken);
        if (!StringUtils.isEmpty(notificationArns)) request.notificationArns(notificationArns);
        if (!StringUtils.isEmpty(pathId)) request.pathId(pathId);
        return client.provisionProduct(request.build());
    }

    public static DescribeProvisionedProductResponse describeProvisionProduct(
            String acceptedLanguage, String productId, ServiceCatalogClient client) {
        return client.describeProvisionedProduct(DescribeProvisionedProductRequest.builder()
                .acceptLanguage(acceptedLanguage).id(productId).build());
    }

    public static String getUpdatedProductStatus(String recordId, ServiceCatalogClient client) {
        return describeRecord(recordId, client).recordDetail().statusAsString();
    }

    public static DescribeRecordResponse describeRecord(String recordId, ServiceCatalogClient client) {
        return client.describeRecord(DescribeRecordRequest.builder().id(recordId).build());
    }

    public static List<Stack> describeCloudFormationStack(String stackName, CloudFormationClient client) {
        return client.describeStacks(DescribeStacksRequest.builder().stackName(stackName).build()).stacks();
    }

    public static DescribeStackResourcesResponse describeStackResourcesResult(String stackName, CloudFormationClient client) {
        return client.describeStackResources(DescribeStackResourcesRequest.builder().stackName(stackName).build());
    }

    public static String getCloudFormationStackName(String recordId, ServiceCatalogClient client, Long pollingInterval)
            throws InterruptedException {
        DescribeRecordResponse recordResult = describeRecord(recordId, client);
        Pattern pattern = Pattern.compile(CLOUD_FORMATION_STACK_NAME_REGEX);
        Matcher matcher = pattern.matcher(recordResult.recordOutputs().toString());
        while (!matcher.find()) {
            if (recordResult.recordDetail().statusAsString().equals(FAILED)) {
                throw new RuntimeException(recordResult.recordDetail().recordErrors().toString());
            }
            Thread.sleep(pollingInterval);
            recordResult = describeRecord(recordId, client);
            matcher = pattern.matcher(recordResult.recordOutputs().toString());
        }
        return recordResult.recordOutputs().toString().split("/")[1];
    }

    public static TerminateProvisionedProductResponse terminateProvisionedProduct(
            String acceptLanguage, Boolean ignoreErrors, String productId, String productName, String token,
            ServiceCatalogClient client) {
        String terminateToken = StringUtils.isEmpty(token) ? generateRandomToken() : token;
        TerminateProvisionedProductRequest.Builder request = TerminateProvisionedProductRequest.builder()
                .ignoreErrors(ignoreErrors)
                .acceptLanguage(acceptLanguage)
                .terminateToken(terminateToken);
        if (!StringUtils.isEmpty(productId)) request.provisionedProductId(productId);
        if (!StringUtils.isEmpty(productName)) request.provisionedProductName(productName);
        return client.terminateProvisionedProduct(request.build());
    }

    public static UpdateProvisionedProductResponse updateProvisionedProduct(
            String acceptedLanguage, String pathId, String productId, String provisionedProductId,
            List<UpdateProvisioningParameter> parameters, String provisionedProductName,
            String artifactId, String token, ServiceCatalogClient client) {
        String updateToken = StringUtils.isEmpty(token) ? generateRandomToken() : token;
        UpdateProvisionedProductRequest.Builder request = UpdateProvisionedProductRequest.builder()
                .acceptLanguage(acceptedLanguage)
                .productId(productId)
                .provisioningParameters(parameters)
                .provisioningArtifactId(artifactId)
                .updateToken(updateToken);
        if (!StringUtils.isEmpty(pathId)) request.pathId(pathId);
        if (!StringUtils.isEmpty(provisionedProductId)) request.provisionedProductId(provisionedProductId);
        if (!StringUtils.isEmpty(provisionedProductName)) request.provisionedProductName(provisionedProductName);
        return client.updateProvisionedProduct(request.build());
    }

    private static String generateRandomToken() {
        return UUID.randomUUID().toString();
    }
}
