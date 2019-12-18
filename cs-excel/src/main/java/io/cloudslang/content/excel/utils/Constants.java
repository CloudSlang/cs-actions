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

public class Constants {
    public static final String BOOLEAN_FALSE = "false";
    public static final String NEW_LINE = "\n";
    public static final String EXCEPTION_EMPTY_FILE_NAME ="Excel file name cannot be empty.";
    public static final String EXCEPTION_INVALID_ROW_DATA ="Invalid row data";
    public static final String EXCEPTION_INVALID_COLUMN_INDEX_SIZE ="Column index list size doesn't match rowData column count.";
    public static final String EXCEPTION_INVALID_ROW_INDEX_SIZE ="Row index list size doesn't match rowData row count.";
    static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    static final String EXCEPTION_INVALID_HAS_HEADER = "The %s for %s input is not valid.The valid values are yes/no.";
    static final String EXCEPTION_INVALID_OPERATOR = "The %s for %s input is not a valid operator.";
    static final String EXCEPTION_EMPTY_FILE_PATH = "The excelFilName is required.";
    static final String EXCEPTION_INVALID_FILE = "The value '%s' for %s input is not a valid file path.";
    static final String EXCEPTION_INVALID_INDEX = "The value '%s' for %s input is not a valid index.";
    static final String EXCEPTION_NEGATIVE_INDEX = "The value '%s' for %s input cannot be a negative number.";
    static final String EXCEPTION_INVALID_INDEX_NOT_A_NUMBER = "The value '%s' for %s input is not a valid number.";
    static final String EXCEPTION_EMPTY = "The %s input can't be empty.";
    static final String EXCEPTION_FILE_ALREADY_EXISTS = "File already exists";
    public static final String EXCEPTION_WORKSHEET_NAME_EMPTY = "The %s can't be null or empty.";
    public static final String ROW_DATA_REQD_MSG = "Data that should be added or modified in the document is not given.";
    public static final String DEFAULT_WORKSHEET = "Sheet1";
    public static final String YES = "yes";
    public static final String ZERO = "0";
    public static final String DEFAULT_OPERATOR = "==";
    public static final String DEFAULT_ROW_DELIMITER = "|";
    public static final String DEFAULT_COLUMN_DELIMITER = ",";
    public static final String DEFAULT_DELIMITER_WORKSHEET_NAMES = ",";
    public static String FORMAT_XLSX = "XLSX";
    public static String FORMAT_XLS = "XLS";
    public static final String BAD_CREATE_EXCEL_FILE_MSG = "Invalid file for Excel documents. Expecting file name with extension "
            + FORMAT_XLS + " or " + FORMAT_XLSX + ".";
    public static String FORMAT_XLSM = "XLSM";
    public static final String BAD_EXCEL_FILE_MSG = "Invalid file for Excel documents. Expecting file name with extension "
            + FORMAT_XLS + " , " + FORMAT_XLSX + " or " + FORMAT_XLSM + ".";
}
