package io.cloudslang.content.amazon.actions.servicecatalog;

import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.model.ProvisionProductResult;
import com.amazonaws.services.servicecatalog.model.ProvisioningParameter;
import com.amazonaws.services.servicecatalog.model.Tag;
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
import io.cloudslang.content.constants.DefaultValues;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ServiceCatalogInputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ProvisionProductAction {
    @Action(name = "Provision Product",
            outputs = {
                    @Output(value = Outputs.RETURN_CODE),
                    @Output(value = Outputs.RETURN_RESULT),
                    @Output(value = Outputs.EXCEPTION),
                    @Output(value = Outputs.CREATED_TIME),
                    @Output(value = Outputs.PATH_ID),
                    @Output(value = Outputs.PRODUCT_ID),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_ID),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_NAME),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_TYPE),
                    @Output(value = Outputs.PROVISIONED_ARTIFACT_ID),
                    @Output(value = Outputs.STATUS),
                    @Output(value = Outputs.STACK_ID),
                    @Output(value = Outputs.STACK_NAME),
                    @Output(value = Outputs.STACK_OUTPUTS),
                    @Output(value = Outputs.STACK_RESOURCES)

            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = IDENTITY, required = true) final String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) final String credential,
                                       @Param(value = PROXY_HOST) final String proxyHost,
                                       @Param(value = PROXY_PORT) final String proxyPort,
                                       @Param(value = PROXY_USERNAME) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) final String proxyPassword,
                                       @Param(value = CONNECT_TIMEOUT) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT) String execTimeout,
                                       @Param(value = ASYNC) String async,
                                       @Param(value = PRODUCT_ID, required = true) String productId,
                                       @Param(value = PROVISIONED_PRODUCT_NAME, required = true) String provisionedProductName,
                                       @Param(value = PROVISIONING_ARTIFACT_ID, required = true) String provisioningArtifactId,
                                       @Param(value = PROVISIONING_PARAMETERS) String provisioningParameters,
                                       @Param(value = TAGS) String tags,
                                       @Param(value = PROVISION_TOKEN) String provisionTokens,
                                       @Param(value = ACCEPT_LANGUAGE) String acceptLanguage,
                                       @Param(value = NOTIFICATION_ARNS) String notificationArns,
                                       @Param(value = PATH_ID) String pathId,
                                       @Param(value = REGION) String region) {
        //Assign default values to inputs
        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);
        final String asyncVal = defaultIfEmpty(async, DefaultValues.ASYNC);

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

        try {
            List<ProvisioningParameter> params = null;
            List<Tag> tagss = null;
            final AWSServiceCatalog awsServiceCatalog = ServiceCatalogClientBuilder.getServiceCatalogClientBuilder(identity, credential,
                    proxyHost, proxyPortImp, proxyUsername, proxyUsername, connectTimeoutImp, execTimeoutImp, region, asyncImp);
            final ProvisionProductResult result = AmazonServiceCatalogService.provisionProduct(provisionedProductName, params, productId, provisionTokens, provisioningArtifactId, tagss,
                    acceptLanguage, notificationArns, pathId, awsServiceCatalog);
        } catch (Exception e) {

        }



        return null;
    }

}
