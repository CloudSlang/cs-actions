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
import io.cloudslang.content.excel.entities.ExcelCommonInputs;
import io.cloudslang.content.excel.entities.GetCellInputs;
import io.cloudslang.content.excel.services.GetCellService;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.excel.utils.Constants.*;
import static io.cloudslang.content.excel.utils.Descriptions.Common.*;
import static io.cloudslang.content.excel.utils.Descriptions.GetCell.*;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.WORKSHEET_NAME;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.*;
import static io.cloudslang.content.excel.utils.InputsValidation.verifyGetCellInputs;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

/**
 * Created by danielmanciu on 18.02.2019.
 */
public class GetCell {

    @Action(name = "Get Cell",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = HEADER, description = HEADER_DESC),
                    @Output(value = ROWS_COUNT, description = ROWS_COUNT_DESC),
                    @Output(value = COLUMNS_COUNT, description = COLUMNS_COUNT_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = EXCEL_FILE_NAME, required = true, description = EXCEL_FILE_NAME_DESC) String excelFileName,
                                       @Param(value = WORKSHEET_NAME, description = WORKSHEET_NAME_DESC) String worksheetName,
                                       @Param(value = HAS_HEADER, description = HAS_HEADER_DESC) String hasHeader,
                                       @Param(value = FIRST_ROW_INDEX, description = FIRST_ROW_INDEX_DESC) String firstRowIndex,
                                       @Param(value = ROW_INDEX, description = ROW_INDEX_DESC) String rowIndex,
                                       @Param(value = COLUMN_INDEX, description = COLUMN_INDEX_DESC) String columnIndex,
                                       @Param(value = ROW_DELIMITER, description = ROW_DELIMITER_DESC) String rowDelimiter,
                                       @Param(value = COLUMN_DELIMITER, description = COLUMN_DELIMITER_DESC) String columnDelimiter,
                                       @Param(value = ENABLING_ROUNDING_FUNCTION, description = ENABLING_ROUNDING_FUNCTION_DESC) String enablingRoundingFunction) {

        excelFileName = defaultIfEmpty(excelFileName, EMPTY);
        worksheetName = defaultIfEmpty(worksheetName, DEFAULT_WORKSHEET);
        hasHeader = defaultIfEmpty(hasHeader, YES);
        firstRowIndex = defaultIfEmpty(firstRowIndex, ZERO);
        rowIndex = defaultIfEmpty(rowIndex, EMPTY); //its default depends on the document so it will be set later
        columnIndex = defaultIfEmpty(columnIndex, EMPTY); //its default depends on the document so it will be set later
        rowDelimiter = defaultIfEmpty(rowDelimiter, DEFAULT_ROW_DELIMITER);
        columnDelimiter = defaultIfEmpty(columnDelimiter, DEFAULT_COLUMN_DELIMITER);
        enablingRoundingFunction = defaultIfEmpty(enablingRoundingFunction, DEFAULT_ENABLING_ROUNDING_FUNCTION);

        final List<String> exceptionMessages = verifyGetCellInputs(excelFileName, hasHeader, firstRowIndex, rowIndex,
                columnIndex, enablingRoundingFunction);


        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            return GetCellService.getCell(GetCellInputs.builder()
                    .commonInputs(ExcelCommonInputs.builder()
                            .excelFileName(excelFileName)
                            .worksheetName(worksheetName)
                            .build())
                    .hasHeader(hasHeader)
                    .firstRowIndex(firstRowIndex)
                    .rowIndex(rowIndex)
                    .columnIndex(columnIndex)
                    .rowDelimiter(rowDelimiter)
                    .columnDelimiter(columnDelimiter)
                    .enablingRoundingFunction(enablingRoundingFunction)
                    .build());

        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }

    }


}
