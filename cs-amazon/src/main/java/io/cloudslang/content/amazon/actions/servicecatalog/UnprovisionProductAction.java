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
import com.amazonaws.services.servicecatalog.model.TerminateProvisionedProductResult;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.validators.Validator;
import io.cloudslang.content.amazon.factory.ServiceCatalogClientBuilder;
import io.cloudslang.content.amazon.services.AmazonServiceCatalogService;
import io.cloudslang.content.amazon.utils.DefaultValues;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.ACCEPT_LANGUAGE_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.REGION_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.UnprovisionProductAction.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ServiceCatalogInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Outputs.PROVISIONED_PRODUCT_ID;
import static io.cloudslang.content.amazon.utils.DefaultValues.IGNORE_ERRORS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class UnprovisionProductAction {

    @Action(name = "Unprovision Product", description = UNPROVISION_PRODUCT_DESCRIPTION,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
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
                                       @Param(value = REGION, description = REGION_DESC) String region,
                                       @Param(value = PROVISIONED_PRODUCT_ID, description = PROVISIONED_PRODUCT_ID_DESC) String provisionedProductId,
                                       @Param(value = PROVISIONED_PRODUCT_NAME, description = PROVISIONED_PRODUCT_NAME_DESC) String provisionedProductName,
                                       @Param(value = ACCEPT_LANGUAGE, description = ACCEPT_LANGUAGE_DESC) String acceptLanguage,
                                       @Param(value = IGNORE_ERRORS, description = IGNORE_ERRORS_DESC) String ignoreErrors,
                                       @Param(value = TERMINATE_TOKEN, description = TERMINATE_TOKEN_DESC) String terminateToken
    ) {
        //Assign default values to inputs
        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);
        final String asyncVal = defaultIfEmpty(async, DefaultValues.ASYNC);
        final String acceptLanguageVal = defaultIfEmpty(acceptLanguage, DefaultValues.ACCEPTED_LANGUAGE);
        final String ignoreErrorsVal = defaultIfEmpty(ignoreErrors, IGNORE_ERRORS);
        final String regionVal = defaultIfEmpty(region, DefaultValues.REGION);

        //Validate inputs
        Validator validator = new Validator()
                .validatePort(proxyPortVal, PROXY_PORT)
                .validateInt(connectTimeoutVal, CONNECT_TIMEOUT)
                .validateInt(execTimeoutVal, EXECUTION_TIMEOUT)
                .validateBoolean(asyncVal, ASYNC)
                .validateBoolean(ignoreErrorsVal, IGNORE_ERRORS);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        //Variable conversions
        final Integer proxyPortImp = Integer.valueOf(proxyPortVal);
        final Integer connectTimeoutImp = Integer.valueOf(connectTimeoutVal);
        final Integer execTimeoutImp = Integer.valueOf(execTimeoutVal);
        final Boolean asyncImp = Boolean.valueOf(asyncVal);
        final Boolean ignoreErrorsImp = Boolean.valueOf(ignoreErrorsVal);

        try {

            final AWSServiceCatalog awsServiceCatalog = ServiceCatalogClientBuilder.getServiceCatalogClientBuilder(identity, credential,
                    proxyHost, proxyPortImp, proxyUsername, proxyPassword, connectTimeoutImp, execTimeoutImp, regionVal, asyncImp);

            final TerminateProvisionedProductResult result = AmazonServiceCatalogService.terminateProvisionedProduct(acceptLanguageVal, ignoreErrorsImp, provisionedProductId, provisionedProductName, terminateToken, awsServiceCatalog);

            return getSuccessResultsMap(result.toString());
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
