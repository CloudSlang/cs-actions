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
package io.cloudslang.content.excel.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.excel.entities.NewExcelDocumentInputs;
import io.cloudslang.content.excel.services.NewExcelDocumentService;
import io.cloudslang.content.utils.OutputUtilities;
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
import static io.cloudslang.content.excel.utils.Constants.DEFAULT_DELIMITER_WORKSHEET_NAMES;
import static io.cloudslang.content.excel.utils.Constants.NEW_LINE;
import static io.cloudslang.content.excel.utils.Descriptions.Common.EXCEL_FILE_NAME_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.DELIMITER_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.EXCEPTION_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.FAILURE_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.RETURN_RESULT_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.SUCCESS_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.NewExcelDocument.WORKSHEET_NAMES_DESC;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.NewExcelDocument.DELIMITER;
import static io.cloudslang.content.excel.utils.Inputs.NewExcelDocument.WORKSHEET_NAMES;
import static io.cloudslang.content.excel.utils.InputsValidation.verifyNewExcelDocument;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by alexandra boicu 20/2/2019
 */
public class NewExcelDocument {

    @Action(name = "New Excel Document",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = EXCEL_FILE_NAME, required = true, description = EXCEL_FILE_NAME_DESC) String excelFileName,
                                       @Param(value = WORKSHEET_NAMES, description = WORKSHEET_NAMES_DESC) String worksheetNames,
                                       @Param(value = DELIMITER, description = DELIMITER_DESC) String delimiter) {

        excelFileName = defaultIfEmpty(excelFileName, EMPTY);
        worksheetNames = defaultIfEmpty(worksheetNames, EMPTY);
        delimiter = defaultIfEmpty(delimiter, DEFAULT_DELIMITER_WORKSHEET_NAMES);

        final List<String> exceptionMessages = verifyNewExcelDocument(excelFileName);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = NewExcelDocumentService.newExcelDocument(NewExcelDocumentInputs.builder()
                    .excelFileName(excelFileName)
                    .worksheetNames(worksheetNames)
                    .delimiter(delimiter)
                    .build());

            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
