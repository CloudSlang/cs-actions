/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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




package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.services.PdfParseService;
import io.cloudslang.content.utils.BooleanUtilities;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OperationDescription.FIND_TEXT_IN_PDF_OPERATION_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Inputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.String.format;
import static org.apache.commons.lang3.BooleanUtils.toBoolean;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by marisca on 7/11/2017.
 */
public class FindTextInPdf {

    /**
     * This operation checks if a text input is found in a PDF file.
     *
     * @param text       The text to be searched for in the PDF file.
     * @param ignoreCase Whether to ignore if characters of the text are lowercase or uppercase.
     *                   Valid values: "true", "false".
     *                   Default Value: "false"
     * @param pathToFile The full path to the PDF file.
     * @param password   The password for the PDF file.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * returnResult - The number of occurrences of the text in the PDF file.
     * returnCode - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception - the exception message if the operation fails.
     */

    @Action(name = "Find Text in PDF",
            description = FIND_TEXT_IN_PDF_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = FIND_TEXT_IN_PDF_RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = TEXT, required = true, description = INITIAL_VALUE_DESC) String text,
            @Param(value = IGNORE_CASE, description = IGNORE_CASE_DESC) String ignoreCase,
            @Param(value = PATH_TO_FILE, required = true, description = DEFAULT_VALUE_DESC) String pathToFile,
            @Param(value = PASSWORD, description = PASSWORD_DESC, encrypted = true) String password) {

        try {
            final Path path = Paths.get(pathToFile);
            final String pdfPassword = defaultIfEmpty(password, EMPTY);
            final String pdfContent = PdfParseService.getPdfContent(path, pdfPassword).trim().replace(System.lineSeparator(), EMPTY);
            final boolean validIgnoreCase = BooleanUtilities.isValid(ignoreCase);
            if (!validIgnoreCase) {
                throw new RuntimeException(format("Invalid boolean value for ignoreCase parameter: %s", ignoreCase));
            }

            return getSuccessResultsMap(PdfParseService.getOccurrences(pdfContent, text, toBoolean(ignoreCase)));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
