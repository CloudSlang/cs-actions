package io.cloudslang.content.utilities.actions;

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


import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.services.PdfParseService;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.PATHS_TO_PDF_FILES_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.InputsDescription.PATH_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OperationDescription.MERGE_PDF_OPERATION_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.OutputsDescription.*;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.FAILURE_DESC;
import static io.cloudslang.content.utilities.entities.constants.Descriptions.ResultsDescription.SUCCESS_DESC;
import static io.cloudslang.content.utilities.entities.constants.Inputs.PATHS_TO_PDF_FILES;
import static io.cloudslang.content.utilities.entities.constants.Inputs.PATH_TO_FILE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;


public class MergePdfFiles {

    /**
     * This operation merge multiple PDF files to a single PDF file.
     *
     * @param pathsTOPDFFiles The list of PDF files separated by comma containing the full path of each PDF file.
     * @param pathToFile      The full path to the new PDF file.
     * @return - a map containing the output of the operation. Keys present in the map are:
     * returnResult - The path to the created PDF file.
     * returnCode - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * exception - the exception message if the operation fails.
     */

    @Action(name = "Get Text from PDF",
            description = MERGE_PDF_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = MERGE_PDF_RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true, description = FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = PATHS_TO_PDF_FILES, required = true, description = PATHS_TO_PDF_FILES_DESC) String pathsTOPDFFiles,
            @Param(value = PATH_TO_FILE, required = true, description = PATH_DESC) String pathToFile) {
        try {
            return getSuccessResultsMap(PdfParseService.mergeFiles(pathToFile, pathsTOPDFFiles));
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}

