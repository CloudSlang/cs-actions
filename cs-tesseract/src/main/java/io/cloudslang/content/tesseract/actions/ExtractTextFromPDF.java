/*
 * Copyright 2022-2025 Open Text
 * This program and the accompanying materials
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
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.tesseract.services.PdfService.imageConvert;
import static io.cloudslang.content.tesseract.utils.Constants.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractText.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.ExtractTextFromPDF.EXTRACT_TEXT_FROM_PDF_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.InputsDescription.*;
import static io.cloudslang.content.tesseract.utils.Descriptions.OutputsDescription.TEXT_JSON_DESC;
import static io.cloudslang.content.tesseract.utils.Descriptions.OutputsDescription.TEXT_STRING_DESC;
import static io.cloudslang.content.tesseract.utils.Inputs.*;
import static io.cloudslang.content.tesseract.utils.InputsValidation.verifyExtractTextFromPDF;
import static io.cloudslang.content.tesseract.utils.Outputs.TEXT_JSON;
import static io.cloudslang.content.tesseract.utils.Outputs.TEXT_STRING;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ExtractTextFromPDF {
    /**
     * This operation converts a PDF file given as input and extracts the text using Google Tesseract library.
     *
     * @param filePath   The path of the file to be extracted. The file must be an image. Most of the common image formats
     *                   are supported.
     *                   Required
     * @param dataPath   The path to the tessdata folder that contains the Tesseract config files.
     *                   Required
     * @param language   The language that will be used by the Tesseract engine. This input is taken into consideration
     *                   only when specifying the dataPath input as well.
     *                   Default value: 'ENG'
     *                   Required
     * @param dpi        The DPI value when converting the PDF file to image.
     *                   Default value: 300
     *                   Optional
     * @param textBlocks If set to 'true' operation will return a json containing text blocks extracted from image.
     *                   Valid values: false, true
     *                   Default value: false
     *                   Optional
     * @param deskew     Improve text recognition if an image does not have a normal text orientation(skewed image).
     *                   If set to 'true' the image will be rotated to the correct text orientation.
     *                   Valid values: false, true
     *                   Default value: false
     *                   Optional
     * @param fromPage   The starting page number from where the text should be retrieved.
     *                   Optional
     * @param toPage     The last page number from where the text should be retrieved.
     *                   Optional
     * @param pageIndex  A comma separated list of page numbers from where the text should be retrieved.
     *                   Optional
     * @return a map containing the output of the operation. Keys present in the map are:
     * returnResult - This will contain the extracted text.
     * exception - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * textString - The extracted text from image.
     * textJson - A json containing extracted blocks of text from image.
     * returnCode - The returnCode of the operation: 0 for success, -1 for failure.
     */

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
            @Param(value = DATA_PATH, required = true, description = DATA_PATH_DESC) String dataPath,
            @Param(value = LANGUAGE, required = true, description = LANGUAGE_DESC) String language,
            @Param(value = DPI, description = DPI_DESC) String dpi,
            @Param(value = TEXT_BLOCKS, description = TEXT_BLOCKS_DESC) String textBlocks,
            @Param(value = DESKEW, description = DESKEW_DESC) String deskew,
            @Param(value = FROM_PAGE, description = FROM_PAGE_DESC) String fromPage,
            @Param(value = TO_PAGE, description = TO_PAGE_DESC) String toPage,
            @Param(value = PAGE_INDEX, description = PAGE_INDEX_DESC) String pageIndex) {

        dataPath = defaultIfEmpty(dataPath, EMPTY);
        language = defaultIfEmpty(language, ENG);
        dpi = defaultIfEmpty(dpi, DPI_SET);
        textBlocks = defaultIfEmpty(textBlocks, FALSE);
        deskew = defaultIfEmpty(deskew, FALSE);
        fromPage = defaultIfEmpty(fromPage, EMPTY);
        toPage = defaultIfEmpty(toPage, EMPTY);
        pageIndex = defaultIfEmpty(pageIndex, EMPTY);

        final List<String> exceptionMessages = verifyExtractTextFromPDF(filePath, dataPath, textBlocks, deskew, fromPage, toPage, pageIndex, dpi);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {


            final String resultText = imageConvert(filePath, dataPath, language, dpi, textBlocks, deskew, fromPage, toPage, pageIndex);
            final Map<String, String> result = getSuccessResultsMap(resultText);
            if (Boolean.parseBoolean(textBlocks)) {
                result.put(TEXT_JSON, resultText);
            } else {
                result.put(TEXT_STRING, resultText);
            }

            return result;
        } catch (IndexOutOfBoundsException e) {
            return getFailureResultsMap(EXCEPTION_EXCEEDS_PAGES);
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
