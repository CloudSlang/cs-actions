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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_EMPTY_FILE_PATH;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_BOOLEAN;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_FILE;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_INVALID_NUMBER;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_NULL_EMPTY;
import static io.cloudslang.content.excel.utils.Inputs.CommonInputs.EXCEL_FILE_NAME;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.FIRST_ROW_INDEX;
import static io.cloudslang.content.excel.utils.Inputs.GetCellInputs.HAS_HEADER;
import static io.cloudslang.content.excel.utils.Inputs.GetRowIndexByCondition.COLUMN_INDEX_TO_QUERY;
import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class InputsValidation {


    @NotNull
    public static List<String> verifyGetCellInputs(@Nullable final String excelFileName,
                                                   @Nullable final String worksheetName,
                                                   @Nullable final String hasHeader,
                                                   @Nullable final String firstRowIndex,
                                                   @Nullable final String rowIndex,
                                                   @Nullable final String columnIndex,
                                                   @Nullable final String rowDelimiter,
                                                   @Nullable final String columnDelimiter) {

        final List<String> exceptionMessages = verifyCommonInputs(excelFileName, worksheetName);

        addVerifyBoolean(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        return exceptionMessages;
    }


    @NotNull
    public static List<String> verifyCommonInputs(@Nullable final String excelFileName,
                                                  @Nullable final String worksheetName) {

        final List<String> exceptionMessages = new ArrayList<>();
        addVerifyFile(exceptionMessages, excelFileName, EXCEL_FILE_NAME);
        return exceptionMessages;
    }

    @NotNull
    public static List<String> verifyGetCellRowIndexbyCondition(@Nullable final String excelFileName,
                                                                @Nullable final String worksheetName,
                                                                @Nullable final String hasHeader,
                                                                @Nullable final String firstRowIndex,
                                                                @Nullable final String columnIndextoQuery,
                                                                @Nullable final String operator,
                                                                @Nullable final String value) {

        final List<String> exceptionMessages = verifyCommonInputs(excelFileName, worksheetName);
        addVerifyBoolean(exceptionMessages, hasHeader, HAS_HEADER);
        addVerifyNumber(exceptionMessages, firstRowIndex, FIRST_ROW_INDEX);
        addVerifyNumber(exceptionMessages, columnIndextoQuery, COLUMN_INDEX_TO_QUERY);


        return exceptionMessages;
    }


    @NotNull
    private static List<String> addVerifyNotNullOrEmpty(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        }
        return exceptions;
    }


    @NotNull
    private static List<String> addVerifyBoolean(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!isValid(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_BOOLEAN, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyNumber(@NotNull List<String> exceptions, @Nullable final String input, @NotNull final String inputName) {
        if (isEmpty(input)) {
            exceptions.add(String.format(EXCEPTION_NULL_EMPTY, inputName));
        } else if (!NumberUtilities.isValidInt(input)) {
            exceptions.add(String.format(EXCEPTION_INVALID_NUMBER, input, inputName));
        }
        return exceptions;
    }

    @NotNull
    private static List<String> addVerifyFile(@NotNull List<String> exceptions, @Nullable final String filePath, @NotNull final String inputName) {
        if (isEmpty(filePath)) {
            exceptions.add(EXCEPTION_EMPTY_FILE_PATH);
        } else if (!isEmpty(filePath) && !isValidFile(filePath)) {
            exceptions.add(String.format(EXCEPTION_INVALID_FILE, filePath, EXCEL_FILE_NAME));
        }
        return exceptions;
    }

    private static boolean isValidFile(@NotNull final String filePath) {
        return new File(filePath).exists();
    }
}

