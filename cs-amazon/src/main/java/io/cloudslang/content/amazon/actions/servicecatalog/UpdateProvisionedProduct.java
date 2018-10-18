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
package io.cloudslang.content.amazon.actions.servicecatalog;

import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.model.DescribeRecordResult;
import com.amazonaws.services.servicecatalog.model.UpdateProvisionedProductResult;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Descriptions;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.validators.Validator;
import io.cloudslang.content.amazon.factory.ServiceCatalogClientBuilder;
import io.cloudslang.content.amazon.services.AmazonServiceCatalogService;
import io.cloudslang.content.amazon.utils.DefaultValues;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.ServiceCatalogActions.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DescribeProvisionedProductAction.PROVISIONED_PRODUCT_ACCEPTED_LANGUAGE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UpdateProvisionedProductDescriptions.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UpdateProvisionedProductDescriptions.PATH_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UpdateProvisionedProductDescriptions.PRODUCT_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UpdateProvisionedProductDescriptions.PROVISIONING_ARTIFACT_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UpdateProvisionedProductDescriptions.PROVISIONING_PARAMETERS_DESC;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ServiceCatalogInputs.*;
import static io.cloudslang.content.amazon.services.AmazonServiceCatalogService.getUpdatedProductStatus;
import static io.cloudslang.content.amazon.utils.OutputsUtil.getSuccessResultMapUpdateProvisioningProduct;
import static io.cloudslang.content.amazon.utils.ServiceCatalogUtil.toArrayOfUpdateParameters;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UpdateProvisionedProduct {
    @Action(name = "Update Provisioned Product", description = UPDATE_PROVISIONED_PRODUCT_DESCRIPTION,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = Outputs.CREATED_TIME, description = CREATED_TIME_DESC),
                    @Output(value = Outputs.PATH_ID, description = PATH_ID_DESC),
                    @Output(value = Outputs.PRODUCT_ID, description = PRODUCT_ID_DESC),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_ID, description = PROVISIONED_PRODUCT_ID_DESC),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_NAME, description = PROVISIONED_PRODUCT_NAME_DESCRIPTION),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_TYPE, description = PROVISIONED_PRODUCT_TYPE_DESC),
                    @Output(value = Outputs.PROVISIONING_ARTIFACT_ID, description = PROVISIONING_ARTIFACT_ID_DESC),
                    @Output(value = Outputs.UPDATE_TIME, description = UPDATE_TIME_DESC),
                    @Output(value = Outputs.RECORD_ID, description = RECORD_ID_DESC),
                    @Output(value = Outputs.RECORD_TYPE, description = RECORD_TYPE_DESC),
                    @Output(value = Outputs.RECORD_ERRORS, description = RECORD_ERRORS_DESC),
                    @Output(value = Outputs.RECORD_TAGS, description = RECORD_TAGS_DESC),
                    @Output(value = Outputs.STATUS, description = STATUS_DESC),


            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESCRIPTION),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESCRIPTION)
            }
    )
    public Map<String, String> execute(@Param(value = IDENTITY, required = true, description = IDENTITY_DESC) final String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true, description = CREDENTIAL_DESC) final String credential,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String execTimeout,
                                       @Param(value = ASYNC, description = ASYNC_DESC) String async,
                                       @Param(value = POLLING_INTERVAL, description = POLLING_INTERVAL_DESC) String pollingInterval,
                                       @Param(value = REGION, description = REGION_DESC) String region,
                                       @Param(value = ACCEPTED_LANGUAGE, description = PROVISIONED_PRODUCT_ACCEPTED_LANGUAGE_DESCRIPTION) String acceptedLanguage,
                                       @Param(value = PATH_ID, description = PATH_ID_DESC) String pathId,
                                       @Param(value = PRODUCT_ID, required = true, description = PRODUCT_ID_DESC) String productId,
                                       @Param(value = PROVISIONED_PRODUCT_ID, description = Descriptions.UpdateProvisionedProductDescriptions.PROVISIONED_PRODUCT_ID_DESCRIPTION) String provisionedProductId,
                                       @Param(value = PROVISIONED_PRODUCT_NAME, description = PROVISIONED_PRODUCT_NAME_DESCRIPTION) String provisionedProductName,
                                       @Param(value = PROVISIONING_ARTIFACT_ID, required = true, description = PROVISIONING_ARTIFACT_ID_DESC) String provisioningArtifactId,
                                       @Param(value = PROVISIONING_PARAMETERS, description = PROVISIONING_PARAMETERS_DESC) String provisioningParameters,
                                       @Param(value = PROVISIONING_USE_PREVIOUS_VALUE, description = PROVISIONING_USE_PREVIOUS_VALUE_DES) String usePreviousValue,
                                       @Param(value = DELIMITER, description = DELIMITER_DESC) String delimiter,
                                       @Param(value = UPDATE_TOKEN, description = UPDATE_TOKEN_DESCRIPTION) String updateToken)


    {

        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);
        final String acceptedLanguageVal = defaultIfEmpty(acceptedLanguage, DefaultValues.ACCEPTED_LANGUAGE);
        final String pollingIntervalVal = defaultIfEmpty(pollingInterval, DefaultValues.POLLING_INTERVAL_DEFAULT);
        final String regionVal = defaultIfEmpty(region, DefaultValues.REGION);
        final String asyncVal = defaultIfEmpty(async, DefaultValues.ASYNC);
        final String delimiterVal = defaultIfEmpty(delimiter, DefaultValues.COMMA);
        final String usePreviousVal = defaultIfEmpty(usePreviousValue, DefaultValues.PREVIOUS_VALUE);


        //Validate inputs
        Validator validator = new Validator()
                .validatePort(proxyPortVal, PROXY_PORT)
                .validateInt(connectTimeoutVal, CONNECT_TIMEOUT)
                .validateInt(execTimeoutVal, EXECUTION_TIMEOUT)
                .validateBoolean(asyncVal, ASYNC);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        //Variable conversions
        final Integer proxyPortImp = Integer.valueOf(proxyPortVal);
        final Integer connectTimeoutImp = Integer.valueOf(connectTimeoutVal);
        final Integer execTimeoutImp = Integer.valueOf(execTimeoutVal);
        final Boolean asyncImp = Boolean.valueOf(asyncVal);
        final Boolean usePreviousValueImp = Boolean.valueOf(usePreviousVal);
        final Long pollingIntervalImp = Long.valueOf(pollingIntervalVal);

        try {
            //The client
            final AWSServiceCatalog awsServiceCatalog = ServiceCatalogClientBuilder.getServiceCatalogClientBuilder(identity, credential,
                    proxyHost, proxyPortImp, proxyUsername, proxyPassword, connectTimeoutImp, execTimeoutImp, regionVal, asyncImp);
            //The result
            final UpdateProvisionedProductResult result = AmazonServiceCatalogService.updateProvisionedProduct(acceptedLanguageVal, pathId,
                    productId, provisionedProductId, toArrayOfUpdateParameters(provisioningParameters, delimiterVal, usePreviousValueImp),
                    provisionedProductName, provisioningArtifactId, updateToken, awsServiceCatalog);
            String updateStatus = result.getRecordDetail().getStatus();
            while (UPDATE_STATUSES.contains(updateStatus)) {
                Thread.sleep(pollingIntervalImp);
                updateStatus = getUpdatedProductStatus(result.getRecordDetail().getRecordId(), awsServiceCatalog);
            }
            if (updateStatus.equals(SUCCEEDED)) {

                return getSuccessResultMapUpdateProvisioningProduct(result);
            }
            final DescribeRecordResult recordResult = AmazonServiceCatalogService.describeRecord(result.getRecordDetail().getRecordId(), awsServiceCatalog);

            throw new RuntimeException(UPDATE_PROVISIONED_PRODUCT_FAILED_REASON + recordResult.getRecordDetail().getRecordErrors().toString());

        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
