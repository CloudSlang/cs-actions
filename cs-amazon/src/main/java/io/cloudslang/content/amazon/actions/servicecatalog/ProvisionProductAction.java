package io.cloudslang.content.amazon.actions.servicecatalog;

import com.amazonaws.services.servicecatalog.AWSServiceCatalog;
import com.amazonaws.services.servicecatalog.model.ProvisionProductResult;
import com.amazonaws.services.servicecatalog.model.RecordError;
import com.amazonaws.services.servicecatalog.model.RecordTag;
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
import io.cloudslang.content.utils.OutputUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ServiceCatalogInputs.*;
import static io.cloudslang.content.amazon.utils.ServiceCatalogUtil.toArrayOfProvisioningParameters;
import static io.cloudslang.content.amazon.utils.ServiceCatalogUtil.toArrayOfTags;
import static io.cloudslang.content.amazon.utils.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.utils.constants.Descriptions.ProvisionProductAction.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class ProvisionProductAction {

    @Action(name = "Provision Product",
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = Outputs.CREATED_TIME, description = CREATED_TIME_DESC),
                    @Output(value = Outputs.PATH_ID, description = PATH_ID_DESC),
                    @Output(value = Outputs.PRODUCT_ID, description = PRODUCT_ID_DESC),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_ID, description = PROVISIONED_PRODUCT_ID_DESC),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_NAME, description = PROVISIONED_PRODUCT_NAME_DESC),
                    @Output(value = Outputs.PROVISIONED_PRODUCT_TYPE, description = PROVISIONED_PRODUCT_TYPE_DESC),
                    @Output(value = Outputs.PROVISIONED_ARTIFACT_ID, description = PROVISIONED_ARTIFACT_ID_DESC),
                    @Output(value = Outputs.STATUS, description = STATUS_DESC),
                    @Output(value = Outputs.STACK_ID, description = STACK_ID_DESC),
                    @Output(value = Outputs.STACK_NAME, description = STACK_NAME_DESC),
                    @Output(value = Outputs.STACK_OUTPUTS, description = STACK_OUTPUTS_DESC),
                    @Output(value = Outputs.STACK_RESOURCES, description = STACK_RESOURCES_DESC)

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
                                       @Param(value = PRODUCT_ID, required = true, description = PRODUCT_ID_DESC) String productId,
                                       @Param(value = PROVISIONED_PRODUCT_NAME, required = true, description = PROVISIONED_PRODUCT_NAME_DESC) String provisionedProductName,
                                       @Param(value = PROVISIONING_ARTIFACT_ID, required = true, description = PROVISIONING_ARTIFACT_ID_DESC) String provisioningArtifactId,
                                       @Param(value = PARAM_KEY_NAME, description = PROVISIONING_PARAMETERS_DESC) String paramKeyName,
                                       @Param(value = PARAM_SSH_LOCATION, description = PROVISIONING_PARAMETERS_DESC) String paramSshLocation,
                                       @Param(value = PARAM_INSTANCE_TYPE, description = PROVISIONING_PARAMETERS_DESC) String paramInstanceType,
                                       @Param(value = TAGS, description = TAGS_DESC) String tags,
                                       @Param(value = PROVISION_TOKEN, description = PROVISION_TOKEN_DESC) String provisionTokens,
                                       @Param(value = ACCEPT_LANGUAGE, description = ACCEPT_LANGUAGE_DESC) String acceptLanguage,
                                       @Param(value = NOTIFICATION_ARNS, description = NOTIFICATION_ARNS_DESC) String notificationArns,
                                       @Param(value = PATH_ID, description = PATH_ID_DESC) String pathId,
                                       @Param(value = REGION, description = REGION_DESC) String region) {
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

            final AWSServiceCatalog awsServiceCatalog = ServiceCatalogClientBuilder.getServiceCatalogClientBuilder(identity, credential,
                    proxyHost, proxyPortImp, proxyUsername, proxyUsername, connectTimeoutImp, execTimeoutImp, region, asyncImp);
            final ProvisionProductResult result = AmazonServiceCatalogService.provisionProduct(provisionedProductName,
                    productId, provisionTokens, provisioningArtifactId, toArrayOfTags(tags), acceptLanguage, notificationArns, pathId, awsServiceCatalog);

            System.out.println(result.toString());

            Map<String, String> results = getSuccessResultsMap(result.toString());

            results.put("createdTime", result.getRecordDetail().getCreatedTime().toString());
            results.put("pathId", result.getRecordDetail().getPathId());
            results.put("productId", result.getRecordDetail().getProductId());
            results.put("provisionedProductId", result.getRecordDetail().getProvisionedProductId());
            results.put("provisionedProductName", result.getRecordDetail().getProvisionedProductName());
            results.put("provisionedProductType", result.getRecordDetail().getProvisionedProductType());
            results.put("provisioningArtifactId", result.getRecordDetail().getProvisioningArtifactId());
            results.put("recordErrors", result.getRecordDetail().getRecordErrors().toString());
            results.put("recordId", result.getRecordDetail().getRecordId());
            results.put("recordTags", result.getRecordDetail().getRecordTags().toString());
            results.put("recordType", result.getRecordDetail().getRecordType());
            results.put("status", result.getRecordDetail().getStatus());
            results.put("updatedTime", result.getRecordDetail().getUpdatedTime().toString());

            return results;

        } catch (Exception e) {

            return getFailureResultsMap(e);

        }

    }

    public static void main(String[] args) {

        //List<String> notification = new ArrayList<>(Arrays.asList("));
        //List<String> notification = new ArrayList<>(Arrays.asList(""));
        String notification = "arn:aws2:cloudformation2:us-east-2:1stack:z";
        // {"arn","aws","cloudformation","us-east-1"};
        ProvisionProductAction provisionProductAction = new ProvisionProductAction();
        Map<String, String> map = provisionProductAction.execute(
                "AKIAIU3ZPEUPUMY6YQJQ",
                "2If2OS3s6jXqcz3fb1wbAaRbdlOzC49slKWAg1H9",
                "web-proxy.corp.hpecorp.net",
                "8080",
                "",
                "",
                "2000000",
                "20000000",
                null,
                "prod-6ohw45brkm67i",
                "TestWithoutReg",
                "pa-bdebkntsiqoda",
                "KeyName=revnic",
                "",
                null,
                null,
                "arn",
                null,
                "us-east-1");

    }
}
