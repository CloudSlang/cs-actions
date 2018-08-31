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
package io.cloudslang.content.amazon.services;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.DescribeStackResourcesRequest;
import com.amazonaws.services.cloudformation.model.DescribeStackResourcesResult;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.Stack;
import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmazonServiceCatalogService {

    public static final String CLOUD_FORMATION_STACK_NAME_REGEX = "(SC)-[0-9]{0,63}-[a-z]{0,63}-[a-z0-9]{0,63}";

    public static ProvisionProductResult provisionProduct(final String provisionedProductName,
                                                          final List<ProvisioningParameter> provisioningParameters,
                                                          final String productId,
                                                          final String provisionToken,
                                                          final String provisioningArtifactId,
                                                          final List<Tag> tags,
                                                          final String acceptLanguage,
                                                          final String notificationArns,
                                                          final String pathId,
                                                          final AWSServiceCatalog serviceCatalogClient) {
        ProvisionProductRequest provisionProductRequest = new ProvisionProductRequest()
                .withProvisionedProductName(provisionedProductName)
                .withProvisioningParameters(provisioningParameters)
                .withProductId(productId)
                .withProvisioningArtifactId(provisioningArtifactId)
                .withTags(tags)
                .withAcceptLanguage(acceptLanguage);

        if (!StringUtils.isEmpty(notificationArns)) {
            provisionProductRequest.withNotificationArns(notificationArns);
        }

        if (!StringUtils.isEmpty(pathId)) {
            provisionProductRequest.withPathId(pathId);
        }

        if (!StringUtils.isEmpty(provisionToken)) {
            provisionProductRequest.withProvisionToken(provisionToken);
        }

        return serviceCatalogClient.provisionProduct(provisionProductRequest);
    }

    public static DescribeRecordResult describeRecord(final String recordId, final AWSServiceCatalog serviceCatalogClient) {
        DescribeRecordRequest describeRecordRequest = new DescribeRecordRequest()
                .withId(recordId);
        return serviceCatalogClient.describeRecord(describeRecordRequest);
    }

    public static List<Stack> describeCloudFormationStack(final String stackName, final AmazonCloudFormation cloudFormationClient) {
        DescribeStacksRequest describeStacksRequest = new DescribeStacksRequest()
                .withStackName(stackName);
        return cloudFormationClient.describeStacks(describeStacksRequest).getStacks();
    }

    public static String describeStackResources(final String stackName, final AmazonCloudFormation cloudFormationClient) {
        return describeStackResourcesResult(stackName, cloudFormationClient).getStackResources().toString();
    }

    private static DescribeStackResourcesResult describeStackResourcesResult(final String stackName, final AmazonCloudFormation cloudFormationClient) {
        DescribeStackResourcesRequest stackResourceRequest = new DescribeStackResourcesRequest()
                .withStackName(stackName);
        return cloudFormationClient.describeStackResources(stackResourceRequest);
    }

    public static String getCloudFormationStackName(String recordId, AWSServiceCatalog serviceCatalogClient, Long poolingInterval) throws InterruptedException {
        DescribeRecordResult recordResult = AmazonServiceCatalogService.describeRecord(recordId, serviceCatalogClient);
        Pattern stackNamePattern = Pattern.compile(CLOUD_FORMATION_STACK_NAME_REGEX);
        Matcher stackNameMatcher = stackNamePattern.matcher(recordResult.getRecordOutputs().toString());
        while (!stackNameMatcher.find()) {
            Thread.sleep(poolingInterval);
            recordResult = AmazonServiceCatalogService.describeRecord(recordId, serviceCatalogClient);
            stackNameMatcher = stackNamePattern.matcher(recordResult.getRecordOutputs().toString());
        }
        String recordOutputs = recordResult.getRecordOutputs().toString();
        return recordOutputs.split("/")[1];
    }
}
