/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.PASSWORD_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.PATH_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OperationDescription.GET_TEXT_FROM_PDF_OPERATION_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Inputs.PASSWORD;
import static io.cloudslang.content.utilities.entities.constants.Inputs.PATH_TO_FILE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class GetTextFromPdf {

    /**
     * This operation get text from a PDF file.
     *
     * @param pathToFile The full path to the PDF file.
     * @param password   The password for the PDF file.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * returnResult - The text from the PDF file.
     * returnCode - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception - the exception message if the operation fails.
     */

    @Action(name = "Get Text from PDF",
            description = GET_TEXT_FROM_PDF_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = TEXT_FROM_PDF_RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = PATH_TO_FILE, required = true, description = PATH_DESC) String pathToFile,
            @Param(value = PASSWORD, description = PASSWORD_DESC, encrypted = true) String password) {

        try {
            final Path path = Paths.get(pathToFile);
            final String pdfPassword = defaultIfEmpty(password, EMPTY);

            return getSuccessResultsMap(PdfParseService.getPdfContent(path, pdfPassword));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
