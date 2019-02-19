package io.cloudslang.content.excel.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.excel.entities.ExcelCommonInputs;
import io.cloudslang.content.excel.entities.GetRowIndexByConditionInputs;
import io.cloudslang.content.excel.services.ExcelServiceImpl;
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
import static io.cloudslang.content.excel.utils.Constants.DEFAULT_OPERATOR;
import static io.cloudslang.content.excel.utils.Constants.DEFAULT_WORKSHEET;
import static io.cloudslang.content.excel.utils.Constants.NEW_LINE;
import static io.cloudslang.content.excel.utils.Constants.YES;
import static io.cloudslang.content.excel.utils.Constants.ZERO;
import static io.cloudslang.content.excel.utils.Descriptions.Common.EXCEL_FILE_NAME_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.Common.WORKSHEET_NAME_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.COLUMN_INDEX_TO_QUERY_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.EXCEPTION_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.FAILURE_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.FIRST_ROW_INDEX_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.HAS_HEADER_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.OPERATOR_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.RETURN_RESULT_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.ROWS_COUNT_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.SUCCESS_DESC;
import static io.cloudslang.content.excel.utils.Descriptions.GetRowIndexbyCondition.VALUE_DESC;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.WORKSHEET_NAME;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.COLUMN_INDEX_TO_QUERY;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.FIRST_ROW_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.HAS_HEADER;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.OPERATOR;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.VALUE;
import static io.cloudslang.content.excel.utils.InputsValidation.verifyGetCellRowIndexbyCondition;
import static io.cloudslang.content.excel.utils.Outputs.GetRowIndexByCondition.ROWS_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetRowIndexByCondition {
    @Action(name = "Get Row Index by Condition",
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = ROWS_COUNT, description = ROWS_COUNT_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = EXCEL_FILE_NAME, required = true, description = EXCEL_FILE_NAME_DESC) String excelFileName,
                                       @Param(value = WORKSHEET_NAME, description = WORKSHEET_NAME_DESC) String worksheetName,
                                       @Param(value = HAS_HEADER, description = HAS_HEADER_DESC) String hasHeader,
                                       @Param(value = FIRST_ROW_INDEX, description = FIRST_ROW_INDEX_DESC) String firstRowIndex,
                                       @Param(value = COLUMN_INDEX_TO_QUERY, required = true, description = COLUMN_INDEX_TO_QUERY_DESC) String columnIndextoQuery,
                                       @Param(value = OPERATOR, description = OPERATOR_DESC) String operator,
                                       @Param(value = VALUE, description = VALUE_DESC) String value) {
        excelFileName = defaultIfEmpty(excelFileName, EMPTY);
        worksheetName = defaultIfEmpty(worksheetName, DEFAULT_WORKSHEET);
        hasHeader = defaultIfEmpty(hasHeader, YES);
        firstRowIndex = defaultIfEmpty(firstRowIndex, ZERO);
        columnIndextoQuery = defaultIfEmpty(columnIndextoQuery, EMPTY);
        operator = defaultIfEmpty(operator, DEFAULT_OPERATOR);
        value = defaultIfEmpty(value, EMPTY);

        final List<String> exceptionMessages = verifyGetCellRowIndexbyCondition(excelFileName, worksheetName, hasHeader, firstRowIndex, columnIndextoQuery, operator, value);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final Map<String, String> result = ExcelServiceImpl.getRowIndexbyCondition(GetRowIndexByConditionInputs.builder()
                    .commonInputs(ExcelCommonInputs.builder()
                            .excelFileName(excelFileName)
                            .worksheetName(worksheetName)
                            .build())
                    .firstRowIndex(firstRowIndex)
                    .columnIndextoQuery(columnIndextoQuery)
                    .hasHeader(hasHeader)
                    .operator(operator)
                    .value(value)
                    .build());
            return result;
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
