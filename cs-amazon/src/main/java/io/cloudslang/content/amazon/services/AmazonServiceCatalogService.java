/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.cloudslang.content.amazon.entities.constants.Constants.ServiceCatalogActions.CLOUD_FORMATION_STACK_NAME_REGEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.ServiceCatalogActions.FAILED;

public class AmazonServiceCatalogService {

    public static ProvisionProductResult provisionProduct(final String provisionedProductName,
                                                          final List<ProvisioningParameter> provisioningParameters,
                                                          final String productId,
                                                          String provisionToken,
                                                          final String provisioningArtifactId,
                                                          final List<Tag> tags,
                                                          final String acceptLanguage,
                                                          final String notificationArns,
                                                          final String pathId,
                                                          final AWSServiceCatalog serviceCatalogClient) {

        if (StringUtils.isEmpty(provisionToken)) {
            provisionToken = generateRandomToken();
        }

        ProvisionProductRequest provisionProductRequest = new ProvisionProductRequest()
                .withProvisionedProductName(provisionedProductName)
                .withProvisioningParameters(provisioningParameters)
                .withProductId(productId)
                .withProvisioningArtifactId(provisioningArtifactId)
                .withTags(tags)
                .withAcceptLanguage(acceptLanguage)
                .withProvisionToken(provisionToken);

        if (!StringUtils.isEmpty(notificationArns)) {
            provisionProductRequest.withNotificationArns(notificationArns);
        }

        if (!StringUtils.isEmpty(pathId)) {
            provisionProductRequest.withPathId(pathId);
        }

        return serviceCatalogClient.provisionProduct(provisionProductRequest);
    }

    public static DescribeProvisionedProductResult describeProvisionProduct(final String acceptedLanguage,
                                                                            final String productId,
                                                                            final AWSServiceCatalog serviceCatalogClient) {
        DescribeProvisionedProductRequest describeProvisionProductRequest = new DescribeProvisionedProductRequest()
                .withAcceptLanguage(acceptedLanguage)
                .withId(productId);
        return serviceCatalogClient.describeProvisionedProduct(describeProvisionProductRequest);
    }

    public static String getUpdatedProductStatus(final String recordId,
                                                 final AWSServiceCatalog serviceCatalogClient){
        final DescribeRecordResult result = describeRecord(recordId, serviceCatalogClient);
        return result.getRecordDetail().getStatus();
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

    public static DescribeStackResourcesResult describeStackResourcesResult(final String stackName, final AmazonCloudFormation cloudFormationClient) {
        DescribeStackResourcesRequest stackResourceRequest = new DescribeStackResourcesRequest()
                .withStackName(stackName);
        return cloudFormationClient.describeStackResources(stackResourceRequest);
    }

    public static String getCloudFormationStackName(String recordId, AWSServiceCatalog serviceCatalogClient, Long pollingInterval) throws InterruptedException {
        DescribeRecordResult recordResult = AmazonServiceCatalogService.describeRecord(recordId, serviceCatalogClient);
        Pattern stackNamePattern = Pattern.compile(CLOUD_FORMATION_STACK_NAME_REGEX);
        Matcher stackNameMatcher = stackNamePattern.matcher(recordResult.getRecordOutputs().toString());
        while (!stackNameMatcher.find()) {
            if ((recordResult.getRecordDetail().getStatus().equals(FAILED))) {
                throw new RuntimeException(recordResult.getRecordDetail().getRecordErrors().toString());
            }
            Thread.sleep(pollingInterval);
            recordResult = AmazonServiceCatalogService.describeRecord(recordId, serviceCatalogClient);
            stackNameMatcher = stackNamePattern.matcher(recordResult.getRecordOutputs().toString());
        }
        return recordResult.getRecordOutputs().toString().split("/")[1];
    }

    public static TerminateProvisionedProductResult terminateProvisionedProduct(String acceptLanguage,
                                                                                Boolean ignoreErrors,
                                                                                String provisionedProductId,
                                                                                String provisionedProductName,
                                                                                String terminateToken,
                                                                                final AWSServiceCatalog serviceCatalogClient) {
        if (StringUtils.isEmpty(terminateToken)) {
            terminateToken = generateRandomToken();
        }

        TerminateProvisionedProductRequest terminateProvisionedProductRequest = new TerminateProvisionedProductRequest()
                .withIgnoreErrors(ignoreErrors)
                .withAcceptLanguage(acceptLanguage)
                .withTerminateToken(terminateToken);
        if (!StringUtils.isEmpty(provisionedProductId)) {
            terminateProvisionedProductRequest.withProvisionedProductId(provisionedProductId);
        }
        if (!StringUtils.isEmpty(provisionedProductName)) {
            terminateProvisionedProductRequest.withProvisionedProductName(provisionedProductName);
        }

        return serviceCatalogClient.terminateProvisionedProduct(terminateProvisionedProductRequest);
    }

    public static UpdateProvisionedProductResult updateProvisionedProduct(final String acceptedLanguage,
                                                                          final String pathId,
                                                                          final String productId,
                                                                          final String provisionedProductId,
                                                                          final List<UpdateProvisioningParameter> provisioningParameters,
                                                                          final String provisionedProductName,
                                                                          final String provisioningArtifactId,
                                                                          String updateToken,
                                                                          final AWSServiceCatalog serviceCatalogclient) {

        if (StringUtils.isEmpty(updateToken)) {
            updateToken = generateRandomToken();
        }

        UpdateProvisionedProductRequest updateProvisionedProductRequest = new UpdateProvisionedProductRequest()
                .withAcceptLanguage(acceptedLanguage)
                .withProductId(productId)
                .withProvisioningParameters(provisioningParameters)
                .withProvisioningArtifactId(provisioningArtifactId)
                .withUpdateToken(updateToken);

        if (!StringUtils.isEmpty(pathId)) {
            updateProvisionedProductRequest.withPathId(pathId);
        }
        if (!StringUtils.isEmpty(provisionedProductId)) {
            updateProvisionedProductRequest.withProvisionedProductId(provisionedProductId);
        }
        if (!StringUtils.isEmpty(provisionedProductName)) {
            updateProvisionedProductRequest.withProvisionedProductName(provisionedProductName);
        }


        return serviceCatalogclient.updateProvisionedProduct(updateProvisionedProductRequest);
    }

    private static String generateRandomToken(){
        return UUID.randomUUID().toString();
    }
}
