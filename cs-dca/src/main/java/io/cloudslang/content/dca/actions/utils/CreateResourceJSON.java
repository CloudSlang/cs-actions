package io.cloudslang.content.dca.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.dca.models.DcaResourceModel;
import io.cloudslang.content.dca.utils.Validator;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaBaseResourceModels;
import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaDeploymentParameterModels;
import static io.cloudslang.content.dca.utils.Utilities.splitToList;
import static io.cloudslang.content.utils.CollectionUtilities.toList;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.join;

public class CreateResourceJSON {

    @Action(name = "Deploy Template",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(
            @Param("typeUuid") String typeUuid,
            @Param("deploySequence") String deploySequence,
            @Param("baseResourceUuidList") String baseResourceUuidList,
            @Param("baseResourceCiTypeList") String baseResourceCiTypeList,
            @Param("baseResourceTypeUuidList") String baseResourceTypeUuidList,
            @Param("deploymentParameterNameList") String deploymentParameterNameList,
            @Param("deploymentParameterValueList") String deploymentParameterValueList,
            @Param("delimiter") String delimiter
    ) {
        final List<String> baseResourceUuids = splitToList(baseResourceUuidList, delimiter);
        final List<String> baseResourceCiTypes = splitToList(baseResourceCiTypeList, delimiter);
        final List<String> baseResourceTypeUuids = splitToList(baseResourceTypeUuidList, delimiter);

        final List<String> deploymentParameterNames = splitToList(deploymentParameterNameList, delimiter);
        final List<String> deploymentParameterValues = splitToList(deploymentParameterValueList, delimiter);

        final Validator validator = new Validator();
        validator.validateInt(deploySequence);
        validator.validateSameLength(baseResourceUuids, baseResourceCiTypes, baseResourceTypeUuids);
        validator.validateSameLength(deploymentParameterNames, deploymentParameterValues);

        if (!validator.getValidationErrorList().isEmpty()) {
            return getFailureResultsMap(join(validator.getValidationErrorList(), lineSeparator()));
        }

        final DcaResourceModel dcaResourceModel = new DcaResourceModel(typeUuid, Integer.valueOf(deploySequence));

        dcaResourceModel.addBaseResources(getDcaBaseResourceModels(baseResourceUuids, baseResourceCiTypes,
                baseResourceTypeUuids));

        dcaResourceModel.addDeploymentParameters(getDcaDeploymentParameterModels(deploymentParameterNames,
                deploymentParameterValues));

        try {
            return getSuccessResultsMap(dcaResourceModel.toJson());
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }

}
