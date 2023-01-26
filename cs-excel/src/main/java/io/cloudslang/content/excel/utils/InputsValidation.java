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
package io.cloudslang.content.excel.utils;

import io.cloudslang.content.utils.NumberUtilities;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_EMPTY;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_EMPTY_FILE_PATH;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_FILE_ALREADY_EXISTS;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_BOOLEAN;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_FILE;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_HAS_HEADER;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_INDEX;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_INDEX_NOT_A_NUMBER;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_NUMBER;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_OPERATOR;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_NEGATIVE_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.AddCell.COLUMN_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.AddCell.OVERWRITE_DATA;
import static io.cloudslang.content.excel.utils.Inputs.AddCell.ROW_DATA;
import static io.cloudslang.content.excel.utils.Inputs.AddCell.ROW_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.*;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.COLUMN_INDEX_TO_QUERY;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.OPERATOR;
import static io.cloudslang.content.excel.utils.Inputs.ModifyCell.NEW_VALUE;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNumeric;

/**
 * Created by danielmanciu on 18.02.2019.
 */
public final class InputsValidation {

    @NotNull
    public static List<String> verifyAddExcelData(@NotNull final String excelFileName,
                                                  @NotNull final String rowData,
                                                  @NotNull final String rowIndex,
                                                  @NotNull final String columnIndex,
                                                  @NotNull final String overwriteData) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        addVerifyIndex(exceptionMessages, rowIndex, ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, COLUMN_INDEX);
        addVerifyBoolean(exceptionMessages, overwriteData, OVERWRITE_DATA);
        addVerifyNonEmpty(exceptionMessages, rowData, ROW_DATA);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyDeleteCell(@NotNull final String excelFileName,
                                                @NotNull final String rowIndex,
                                                @NotNull final String columnIndex) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        addVerifyIndex(exceptionMessages, rowIndex, ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, COLUMN_INDEX);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetCellInputs(@NotNull final String excelFileName,
                                                   @NotNull final String hasHeader,
                                                   @NotNull final String firstRowIndex,
                                                   @NotNull final String rowIndex,
                                                   @NotNull final String columnIndex,
                                                   @NotNull final String enablingRoundingFunction) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);

        addVerifyYesOrNo(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyPositiveNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        addVerifyIndex(exceptionMessages, rowIndex, ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, COLUMN_INDEX);
        addVerifyBoolean(exceptionMessages, enablingRoundingFunction, ENABLING_ROUNDING_FUNCTION);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyModifyCellInputs(@NotNull final String excelFileName,
                                                      @NotNull final String rowIndex,
                                                      @NotNull final String columnIndex,
                                                      @NotNull final String newValue) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        addVerifyIndex(exceptionMessages, rowIndex, ROW_INDEX);
        addVerifyIndex(exceptionMessages, columnIndex, COLUMN_INDEX);
        addVerifyNonEmpty(exceptionMessages, newValue, NEW_VALUE);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetCellRowIndexbyCondition(@NotNull final String excelFileName,
                                                                @NotNull final String hasHeader,
                                                                @NotNull final String firstRowIndex,
                                                                @NotNull final String columnIndextoQuery,
                                                                @NotNull final String operator) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        addVerifyYesOrNo(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyOperator(exceptionMessages, operator, OPERATOR);
        addVerifyPositiveNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        addVerifyPositiveNumber(exceptionMessages, columnIndextoQuery, COLUMN_INDEX_TO_QUERY);

        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyNewExcelDocument(final String excelFileName) {
        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyNonExistingFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        return exceptionMessages;
    }

    @NotNull
    private static List<String> addVerifyNonEmpty(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_EMPTY, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyPositiveNumber(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        } else if(Integer.parseInt(input)< 0) {
            exceptions.add(String.format(EXCEPTION_NEGATIVE_INDEX, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE_PATH);
        } else if (!isEmpty(filePath) && !isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_FILE, filePath, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNonExistingFile(@NotNull List<String> exceptions, @NotNull final String filePath, @NotNull final String inputName) {
        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE_PATH);
        } else if (!isEmpty(filePath) && isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_FILE_ALREADY_EXISTS, filePath, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyYesOrNo(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_EMPTY, inputName));
        } else if (!input.equalsIgnoreCase("yes") && !input.equalsIgnoreCase("no")) {
            exceptions.add(String.format(EXCEPTION_INVALID_HAS_HEADER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyOperator(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        final List<String> operators = Arrays.asList("==", "<=", ">=", "!=", "<", ">");
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_EMPTY, inputName));
        } else if (!operators.contains(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_OPERATOR, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyIndex(@NotNull List<String> exceptions, @NotNull final String input, @NotNull final String inputName) {
        if (input.isEmpty())
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
                    exceptions.add(String.format(EXCEPTION_INVALID_INDEX_NOT_A_NUMBER, indexValue, inputName));
                }
            }

            if (size == 2) {
                final String indexValueRight = compositeIndex.get(1).trim();
                if (!isNumeric(indexValueRight)) {
                    exceptions.add(String.format(EXCEPTION_INVALID_INDEX_NOT_A_NUMBER, indexValueRight, inputName));
                }
            }
        }

        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }
}

