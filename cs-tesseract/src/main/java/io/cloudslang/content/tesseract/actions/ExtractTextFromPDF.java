/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.tesseract.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.tesseract.services.PdfService.imageConvert;
import static io.cloudslang.content.tesseract.utils.Constants.DPI_SET;
import static io.cloudslang.content.tesseract.utils.Constants.ENG;
import static io.cloudslang.content.tesseract.utils.Constants.FALSE;
import static io.cloudslang.content.tesseract.utils.Constants.NEW_LINE;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.FAILURE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.RETURN_RESULT_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.SUCCESS_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractTextFromPDF.EXTRACT_TEXT_FROM_PDF_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.DATA_PATH_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.DESKEW_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.DPI_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.LANGUAGE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.PDF_FILE_PATH_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.TEXT_BLOCKS_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.OutputsDescription.TEXT_JSON_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.OutputsDescription.TEXT_STRING_DESC;
import static io.cloudslang.content.tesseract.utils.Inputs.DATA_PATH;
import static io.cloudslang.content.tesseract.utils.Inputs.DESKEW;
import static io.cloudslang.content.tesseract.utils.Inputs.DPI;
import static io.cloudslang.content.tesseract.utils.Inputs.FILE_PATH;
import static io.cloudslang.content.tesseract.utils.Inputs.LANGUAGE;
import static io.cloudslang.content.tesseract.utils.Inputs.TEXT_BLOCKS;
import static io.cloudslang.content.tesseract.utils.InputsValidation.verifyExtractTextFromPDF;
import static io.cloudslang.content.tesseract.utils.Outputs.TEXT_JSON;
import static io.cloudslang.content.tesseract.utils.Outputs.TEXT_STRING;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ExtractTextFromPDF {

    @Action(name = "Extract text from PDF",
            description = EXTRACT_TEXT_FROM_PDF_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = TEXT_STRING, description = TEXT_STRING_DESC),
                    @Output(value = TEXT_JSON, description = TEXT_JSON_DESC),
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
            @Param(value = DPI, description = DPI_DESC) String dpi,
            @Param(value = TEXT_BLOCKS, description = TEXT_BLOCKS_DESC) String textBlocks,
            @Param(value = DESKEW, description = DESKEW_DESC) String deskew) {

        dataPath = defaultIfEmpty(dataPath, EMPTY);
        language = defaultIfEmpty(language, ENG);
        dpi = defaultIfEmpty(dpi, DPI_SET);
        textBlocks = defaultIfEmpty(textBlocks, FALSE);
        deskew = defaultIfEmpty(deskew, FALSE);

        final List<String> exceptionMessages = verifyExtractTextFromPDF(filePath, dataPath, dpi, textBlocks, deskew);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }
        try {
            final String resultText = imageConvert(filePath, dataPath, language, dpi, textBlocks, deskew);
            final Map<String, String> result = getSuccessResultsMap(resultText);
            if (Boolean.parseBoolean(textBlocks)) {
                result.put(TEXT_JSON, resultText);
            } else {
                result.put(TEXT_STRING, resultText);
            }

            return result;
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
