package io.cloudslang.content.excel.utils;

public class Constants {
    public static String FORMAT_XLSX = "XLSX";
    public static String FORMAT_XLS = "XLS";
    public static String FORMAT_XLSM = "XLSM";
    public static final String BAD_EXCEL_FILE_MSG = "Invalid file for Excel documents. Expecting file name with extension "
            + FORMAT_XLS + " , " + FORMAT_XLSX + " or " + FORMAT_XLSM + ".";
    public static final String BAD_CREATE_EXCEL_FILE_MSG = "Invalid file for Excel documents. Expecting file name with extension "
            + FORMAT_XLS + " or " + FORMAT_XLSX + ".";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String NEW_LINE = "\n";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_HAS_HEADER = "The %s for %s input is not valid.The valid values are yes/no.";
    public static final String EXCEPTION_INVALID_OPERATOR = "The %s for %s input is not a valid operator.";
    public static final String EXCEPTION_EMPTY_FILE_PATH = "The excelFilName is required.";
    public static final String EXCEPTION_INVALID_FILE = "The value '%s' for %s input is not a valid file path.";
    public static final String EXCEPTION_INVALID_INDEX = "The value '%s' for %s input is not a valid index.";
    public static final String EXCEPTION_INVALID_INDEX_NOT_A_NUMBER = "The value '%s' in %s input is not a number.";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_FILE_ALREADY_EXISTS = "File already exists";
    public static final String EXCEPTION_INDEX_EMPTY = "Index can`t be empty";
    public static final String EXCEPTION_WORKSHEET_NAME_EMPTY = "The %s can't be null or empty.";
    public static final String DEFAULT_WORKSHEET = "Sheet1";
    public static final String YES = "yes";
    public static final String ZERO = "0";
    public static final String DEFAULT_OPERATOR = "==";
    public static final String DEFAULT_ROW_DELIMITER = "|";
    public static final String DEFAULT_COLUMN_DELIMITER = ",";
    public static final String DEFAULT_DELIMITER_WORKSHEET_NAMES = ",";
}
