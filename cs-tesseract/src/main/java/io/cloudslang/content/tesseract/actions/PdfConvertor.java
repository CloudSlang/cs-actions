package io.cloudslang.content.tesseract.actions;

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
import static io.cloudslang.content.tesseract.services.OcrService.extractText;
import static io.cloudslang.content.tesseract.utils.Constants.DPI_SET;
import static io.cloudslang.content.tesseract.utils.Constants.ENG;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.PdfConvert.PDF_CONVERT_DESC;
import static io.cloudslang.content.tesseract.utils.Inputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class PdfConvertor {

    @Action(name = "PDF convertor to image file",
            description = PDF_CONVERT_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = FILE_PATH, required = true, description = PDF_FILE_PATH_DESC) String filePath,
            @Param(value = DATA_PATH, description = DATA_PATH_DESC) String dataPath,
            @Param(value = LANGUAGE, description = LANGUAGE_DESC) String language,
            @Param(value = DPI, description = DPI_DESC) String dpi
    ) {
        try {
            dataPath = defaultIfEmpty(dataPath, EMPTY);
            language = defaultIfEmpty(language, ENG);
            dpi = defaultIfEmpty(dpi, DPI_SET);

            return getSuccessResultsMap(extractText(filePath, dataPath, language));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
