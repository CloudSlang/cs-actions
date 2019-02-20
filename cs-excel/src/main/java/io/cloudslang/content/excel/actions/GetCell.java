package io.cloudslang.content.excel.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.excel.entities.ExcelCommonInputs;
import io.cloudslang.content.excel.entities.GetCellInputs;
import io.cloudslang.content.excel.entities.GetRowIndexByConditionInputs;
import io.cloudslang.content.excel.services.ExcelServiceImpl;
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
import static io.cloudslang.content.excel.utils.InputsValidation.verifyGetCellRowIndexbyCondition;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetCell {

    @Action(name = "Get cell data with specified row and column index in an Excel worksheet",
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
                                       @Param(value = WORKSHEET_NAME, required = true, description = WORKSHEET_NAME_DESC) String worksheetName,
                                       @Param(value = HAS_HEADER, required = true, description = HAS_HEADER_DESC) String hasHeader,
                                       @Param(value = FIRST_ROW_INDEX, required = true, description = FIRST_ROW_INDEX_DESC) String firstRowIndex,
                                       @Param(value = ROW_INDEX, required = true, description = ROW_INDEX_DESC) String rowIndex,
                                       @Param(value = COLUMN_INDEX, required = true, description = COLUMN_INDEX_DESC) String columnIndex,
                                       @Param(value = ROW_DELIMITER, required = true, description = ROW_DELIMITER_DESC) String rowDelimiter,
                                       @Param(value = COLUMN_DELIMITER, required = true, description = COLUMN_DELIMITER_DESC) String columnDelimiter) {

        excelFileName = defaultIfEmpty(excelFileName, EMPTY);
        worksheetName = defaultIfEmpty(worksheetName, DEFAULT_WORKSHEET);
        hasHeader = defaultIfEmpty(hasHeader, YES);
        firstRowIndex = defaultIfEmpty(firstRowIndex, ZERO);
        rowIndex = defaultIfEmpty(rowIndex, EMPTY); //its default depends on the document so it will be set later
        columnIndex = defaultIfEmpty(columnIndex, EMPTY); //its default depends on the document so it will be set later
        rowDelimiter = defaultIfEmpty(rowDelimiter, DEFAULT_ROW_DELIMITER);
        columnDelimiter = defaultIfEmpty(columnDelimiter, DEFAULT_COLUMN_DELIMITER);

        final List<String> exceptionMessages = verifyGetCellInputs(excelFileName, worksheetName, hasHeader,
                firstRowIndex, rowIndex, columnIndex, rowDelimiter, columnDelimiter);


        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = ExcelServiceImpl.getCell(GetCellInputs.builder()
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
                    .build());
            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }

    }


}
