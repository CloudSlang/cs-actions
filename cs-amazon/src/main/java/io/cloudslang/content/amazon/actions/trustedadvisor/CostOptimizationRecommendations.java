package io.cloudslang.content.amazon.actions.trustedadvisor;

import com.amazonaws.services.support.AWSSupport;
import com.amazonaws.services.support.model.DescribeTrustedAdvisorCheckResultResult;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.validators.Validator;
import io.cloudslang.content.amazon.factory.AmazonAWSAdvisoryClientBuilder;
import io.cloudslang.content.amazon.services.AWSAdvisorService;
import io.cloudslang.content.constants.DefaultValues;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.CostOptimizationRecommendationsAction.CHECK_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.CostOptimizationRecommendationsAction.COST_OPTIMIZATION_RECOMMENDATIONS_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DBInstanceCommon.ACCESS_KEY_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DBInstanceCommon.ACCESS_KEY_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ASYNC;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CostOptimizationRecommendationsInputs.CHECK_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CostOptimizationRecommendationsInputs.COST_OPTIMIZATION_RECOMMENDATIONS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.DBInstanceCommonInputs.ACCESS_KEY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.DBInstanceCommonInputs.ACCESS_KEY_ID;
import static io.cloudslang.content.amazon.utils.OutputsUtil.getSuccessResultMapDescribedRecommendations;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class CostOptimizationRecommendations {
    @Action(name = COST_OPTIMIZATION_RECOMMENDATIONS, description = COST_OPTIMIZATION_RECOMMENDATIONS_DESC,
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
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyID,
                                       @Param(value = ACCESS_KEY, required = true, encrypted = true, description = ACCESS_KEY_DESC) final String accessKey,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = CHECK_ID, required = true, description = CHECK_ID_DESC) String checkID,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String execTimeout
    ) {


        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);



        Validator validator = new Validator()
                .validatePort(proxyPortVal, PROXY_PORT)
                .validateInt(connectTimeoutVal, CONNECT_TIMEOUT)
                .validateInt(execTimeoutVal, EXECUTION_TIMEOUT);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        final Integer proxyPortImp = Integer.valueOf(proxyPortVal);
        final Integer connectTimeoutImp = Integer.valueOf(connectTimeoutVal);
        final Integer execTimeoutImp = Integer.valueOf(execTimeoutVal);

        try {

            final AWSSupport awsSupport = AmazonAWSAdvisoryClientBuilder.getAWSAdvisorSupportClientBuilder(accessKeyID, accessKey,
                    proxyHost, proxyPortImp, proxyUsername, proxyPassword, connectTimeoutImp, execTimeoutImp, region);

            final DescribeTrustedAdvisorCheckResultResult result = AWSAdvisorService.getRecommendationDetails(
                    checkID, awsSupport);

            return getSuccessResultMapDescribedRecommendations(result);


        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}