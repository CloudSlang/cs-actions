package io.cloudslang.content.tesseract.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.tesseract.services.ConfigService.copyConfigFiles;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.DATA_PATH_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.DATA_PATH_INP_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.TesseractSetup.CONFIGURATION_FILES_FOR_TESSERACT_OCR;
import static io.cloudslang.content.tesseract.utils.Descriptions.TesseractSetup.TESSERACT_SETUP_DESC;
import static io.cloudslang.content.tesseract.utils.Inputs.DATA_PATH;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class TesseractSetup {

    @Action(name = "Tesseract Setup",

            description = TESSERACT_SETUP_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = DATA_PATH, description = DATA_PATH_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = DATA_PATH, required = true, description = DATA_PATH_INP_DESC) String dataPath) {

        dataPath = defaultIfEmpty(dataPath, EMPTY);

        try {
            final String configPath = copyConfigFiles(dataPath);
            final Map<String, String> result = getSuccessResultsMap(CONFIGURATION_FILES_FOR_TESSERACT_OCR);
            result.put(DATA_PATH, configPath);

            return result;
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
