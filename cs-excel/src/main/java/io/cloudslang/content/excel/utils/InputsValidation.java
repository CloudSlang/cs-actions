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
package io.cloudslang.content.excel.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.excel.utils.Constants.*;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.FIRST_ROW_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.HAS_HEADER;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.COLUMN_INDEX_TO_QUERY;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.OPERATOR;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNumeric;

public final class InputsValidation {

    @NotNull
    public static List<String> verifyDeleteCell(@NotNull final String excelFileName,
                                                @NotNull final String worksheetName,
                                                @NotNull final String rowIndex,
                                                @NotNull final String columnIndex){
        final List<String> exceptionMessages = verifyCommonInputs(excelFileName, worksheetName);
        addVerifyIndex(exceptionMessages, rowIndex, FIRST_ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, FIRST_ROW_INDEX);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetCellInputs(@NotNull final String excelFileName,
                                                   @NotNull final String worksheetName,
                                                   @NotNull final String hasHeader,
                                                   @NotNull final String firstRowIndex,
                                                   @NotNull final String rowIndex,
                                                   @NotNull final String columnIndex,
                                                   @NotNull final String rowDelimiter,
                                                   @NotNull final String columnDelimiter) {

        final List<String> exceptionMessages = verifyCommonInputs(excelFileName, worksheetName);

        addVerifyYesOrNo(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        addVerifyIndex(exceptionMessages, rowIndex, FIRST_ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, FIRST_ROW_INDEX);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyCommonInputs(@NotNull final String excelFileName,
                                                  @NotNull final String worksheetName) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetCellRowIndexbyCondition(@NotNull final String excelFileName,
                                                                @NotNull final String worksheetName,
                                                                @NotNull final String hasHeader,
                                                                @NotNull final String firstRowIndex,
                                                                @NotNull final String columnIndextoQuery,
                                                                @NotNull final String operator,
                                                                @NotNull final String value) {

        final List<String> exceptionMessages = verifyCommonInputs(excelFileName, worksheetName);

        addVerifyYesOrNo(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyOperator(exceptionMessages, operator, OPERATOR);
        addVerifyNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        addVerifyNumber(exceptionMessages, columnIndextoQuery, COLUMN_INDEX_TO_QUERY);

        return exceptionMessages;
    }

    @NotNull public static List<String> verifyNewExcelDocument(final String excelFileName,
                                                               final String worksheetNames,
                                                               final String delimiter){
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNonExistingFile(exceptionMessages,excelFileName,EXCEL_FILE_NAME);
        return exceptionMessages;
    }


    @NotNull
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }


    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE_PATH);
        } else if (!isEmpty(filePath) && !isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_FILE, filePath, EXCEL_FILE_NAME));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNonExistingFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE_PATH);
        } else if (!isEmpty(filePath) && isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_FILE_ALREADY_EXISTS, filePath, EXCEL_FILE_NAME));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyYesOrNo(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
            exceptions.add(String.format(EXCEPTION_INVALID_HAS_HEADER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyOperator(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        final List<String> operators = Arrays.asList("==", "<=", ">=", "!=", "<", ">");
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!operators.contains(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_OPERATOR, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyIndex(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if(input.isEmpty())
            return exceptions;

        final String[] indexList = input.split(",");

        for (String index : indexList) {
            final List<String> compositeIndex = Arrays.asList(index.split(":"));
            final int size = compositeIndex.size();

            if (size != 1 && size != 2) {
                exceptions.add(String.format(EXCEPTION_INVALID_INDEX, input, inputName));
            }

            if (size > 0) {
                final String indexValue = compositeIndex.get(0).trim();
                if (!isNumeric(indexValue)) {
                    exceptions.add(String.format(EXCEPTION_INVALID_INDEX_NOT_A_NUMBER, indexValue, input));
                }
            }

            if (size == 2) {
                final String indexValueRight = compositeIndex.get(1).trim();
                if (!isNumeric(indexValueRight) ) {
                    exceptions.add(String.format(EXCEPTION_INVALID_INDEX_NOT_A_NUMBER, indexValueRight, input));
                }
            }
        }

        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }
}

