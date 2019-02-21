package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.DeleteCellInputs;
import io.cloudslang.content.excel.entities.ExcelOperationException;
import io.cloudslang.content.excel.entities.GetCellInputs;
import io.cloudslang.content.excel.entities.GetRowIndexByConditionInputs;
import io.cloudslang.content.excel.entities.NewExcelDocumentInputs;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import static io.cloudslang.content.excel.utils.Constants.BAD_CREATE_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.BAD_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_WORKSHEET_NAME_EMPTY;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLS;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSM;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSX;
import static io.cloudslang.content.excel.utils.Constants.YES;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.COLUMNS_COUNT;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.HEADER;
import static io.cloudslang.content.excel.utils.Outputs.GetRowIndexByCondition.ROWS_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ExcelServiceImpl {
    private static String inputFormat = null;

    public static Map<String, String> deleteCell(@NotNull final DeleteCellInputs deleteCellInputs) {
        Sheet worksheet;
        Workbook excelDoc;
        final String excelFileName = deleteCellInputs.getCommonInputs().getExcelFileName();

        try {
            excelDoc = getExcelDoc(excelFileName);
            worksheet = getWorksheet(excelDoc, deleteCellInputs.getCommonInputs().getWorksheetName());

            int firstRowIndex = worksheet.getFirstRowNum();
            final int firstColumnIndex = 0;
            final int lastRowIndex = worksheet.getLastRowNum();
            final int lastColumnIndex = getLastColumnIndex(worksheet, firstRowIndex, lastRowIndex);

            final String rowIndexDefault = firstRowIndex + ":" + lastRowIndex;
            final String columnIndexDefault = firstColumnIndex + ":" + lastColumnIndex;
            final String rowIndex = defaultIfEmpty(deleteCellInputs.getRowIndex(), rowIndexDefault);
            final String columnIndex = defaultIfEmpty(deleteCellInputs.getColumnIndex(), columnIndexDefault);

            final List<Integer> rowIndexList = validateIndex(processIndex(rowIndex), firstRowIndex, lastRowIndex, true);
            final List<Integer> columnIndexList = validateIndex(processIndex(columnIndex), firstColumnIndex, lastColumnIndex, false);

            if (rowIndexList.size() != 0 && columnIndexList.size() != 0) {
                int result_int = deleteCell(worksheet, rowIndexList, columnIndexList);
                //update formula cells
                FormulaEvaluator evaluator = excelDoc.getCreationHelper().createFormulaEvaluator();
                for (Row r : worksheet) {
                    for (Cell c : r) {
                        if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            evaluator.evaluateFormulaCell(c);
                        }
                    }
                }
                updateWorkbook(excelDoc, excelFileName);
                return OutputUtilities.getSuccessResultsMap(String.valueOf(result_int));

            }
            else {
                return OutputUtilities.getSuccessResultsMap(String.valueOf("0"));
            }
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }

    public static Map<String, String> newExcelDocument(@NotNull final NewExcelDocumentInputs newExcelDocumentInputs) {

        final FileOutputStream output;
        final String excelFileName = newExcelDocumentInputs.getExcelFileName();
        final Workbook excelDoc;
        try {
            excelDoc = getNewExcelDocument(excelFileName);
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(BAD_CREATE_EXCEL_FILE_MSG);
        }

        final String sheetnameDelimiter = newExcelDocumentInputs.getDelimiter();
        String sheetNames = newExcelDocumentInputs.getWorksheetNames();

        if (sheetNames.isEmpty()) {
            sheetNames = "Sheet1" + sheetnameDelimiter + "Sheet2" + sheetnameDelimiter + "Sheet3";
        }

        final StringTokenizer tokenizer = new StringTokenizer(sheetNames, sheetnameDelimiter);
        if (tokenizer.countTokens() == 0) {
            return OutputUtilities.getFailureResultsMap(EXCEPTION_WORKSHEET_NAME_EMPTY);
        }
        try {
            while (tokenizer.hasMoreTokens()) {
                String sheetName = tokenizer.nextToken();
                excelDoc.createSheet(sheetName);
            }
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(BAD_CREATE_EXCEL_FILE_MSG);
        }

        try {
            output = new FileOutputStream(excelFileName);
            excelDoc.write(output);
            output.close();
        } catch (Exception exception) {
            return getFailureResultsMap(exception.getMessage());
        }
        return OutputUtilities.getSuccessResultsMap(excelFileName + " created successfully");
    }

    @NotNull
    public static Map<String, String> getCell(@NotNull final GetCellInputs getCellInputs) {
        try {
            final Workbook excelDoc = getExcelDoc(getCellInputs.getCommonInputs().getExcelFileName());
            final Sheet worksheet = getWorksheet(excelDoc, getCellInputs.getCommonInputs().getWorksheetName());

            int firstRowIndex = Integer.parseInt(getCellInputs.getFirstRowIndex());
            final int firstColumnIndex = 0;
            final int lastRowIndex = worksheet.getLastRowNum();
            final int lastColumnIndex = getLastColumnIndex(worksheet, firstRowIndex, lastRowIndex);
            final String rowDelimiter = getCellInputs.getRowDelimiter();
            final String columnDelimiter = getCellInputs.getColumnDelimiter();
            final String hasHeader = getCellInputs.getHasHeader();

            if (hasHeader.equals(YES))
                firstRowIndex++;

            final String rowIndexDefault = firstRowIndex + ":" + lastRowIndex;
            final String columnIndexDefault = firstColumnIndex + ":" + lastColumnIndex;
            final String rowIndex = defaultIfEmpty(getCellInputs.getRowIndex(), rowIndexDefault);
            final String columnIndex = defaultIfEmpty(getCellInputs.getRowIndex(), columnIndexDefault);

            final List<Integer> rowIndexList = validateIndex(processIndex(rowIndex), firstRowIndex, lastRowIndex, true);
            final List<Integer> columnIndexList = validateIndex(processIndex(columnIndex), firstColumnIndex, lastColumnIndex, false);

            final String resultString = getCellFromWorksheet(excelDoc, worksheet, columnIndexList, rowIndexList, rowDelimiter, columnDelimiter);
            final Map<String, String> results = getSuccessResultsMap(resultString);

            if (hasHeader.equals(YES)) {
                final String headerString = getHeader(worksheet, firstRowIndex, columnIndexList, columnDelimiter);
                results.put(HEADER, headerString);
            }

            results.put(ROWS_COUNT, String.valueOf(rowIndexList.size()));
            results.put(COLUMNS_COUNT, String.valueOf(columnIndexList.size()));

            return results;
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }

    @NotNull
    public static Map<String, String> getRowIndexbyCondition(@NotNull final GetRowIndexByConditionInputs getRowIndexbyConditionInputs) {
        final Map<String, String> result;
        final Sheet worksheet;
        final Workbook excelDoc;

        try {
            excelDoc = getExcelDoc(getRowIndexbyConditionInputs.getCommonInputs().getExcelFileName());
            worksheet = getWorksheet(excelDoc, getRowIndexbyConditionInputs.getCommonInputs().getWorksheetName());
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }

        int firstRowIndex = Integer.parseInt(getRowIndexbyConditionInputs.getFirstRowIndex());

        if (getRowIndexbyConditionInputs.getHasHeader().equalsIgnoreCase("yes")) {
            firstRowIndex++;
        }
        int columnIndexInt = Integer.parseInt(getRowIndexbyConditionInputs.getColumnIndexToQuery());
        String value = getRowIndexbyConditionInputs.getValue();
        String operator = getRowIndexbyConditionInputs.getOperator();

        getMergedCell(worksheet, firstRowIndex, columnIndexInt);
        processFormulaColumn(excelDoc, worksheet, firstRowIndex, columnIndexInt);

        final String resultString;
        final int rowsCount;
        try {
            resultString = getRowIndex(worksheet, firstRowIndex, value, columnIndexInt, operator);
            rowsCount = resultString.split(",").length;
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
        if (!StringUtils.isBlank(resultString)) {
            result = getSuccessResultsMap(resultString);
            result.put(ROWS_COUNT, String.valueOf(rowsCount));

        } else {
            result = getSuccessResultsMap("");
            result.put(ROWS_COUNT, String.valueOf(0));
        }
        return result;
    }

    private static String getCellFromWorksheet(final Workbook excelDoc,
                                               final Sheet worksheet,
                                               final List<Integer> columnIndex,
                                               final List<Integer> rowIndex,
                                               final String rowDelimiter,
                                               final String columnDelimiter) {
        StringBuilder result = new StringBuilder();
        final DataFormatter formatter = new DataFormatter();

        for (int rIndex : rowIndex) {
            Row row = worksheet.getRow(rIndex);
            if (row == null) {
                row = worksheet.createRow(rIndex);
            }
            if (row != null) {
                for (int cIndex : columnIndex) {
                    Cell cell = row.getCell(cIndex);

                    if (cell == null) {
                        cell = row.createCell(cIndex);
                    }

                    String cellString = formatter.formatCellValue(cell);
                    FormulaEvaluator evaluator = excelDoc.getCreationHelper().createFormulaEvaluator();
                    if (cell != null) {
                        //fraction
                        if (cellString.indexOf("?/?") > 1 && cell.getCellType() == CellType.NUMERIC) {
                            result.append(cell.getNumericCellValue());
                        }

                        //Formula
                        else if (cell.getCellType() == CellType.FORMULA) {
                            CellValue cellValue = evaluator.evaluate(cell);
                            switch (cellValue.getCellType()) {
                                case BOOLEAN:
                                    result.append(cellValue.getBooleanValue());
                                    break;
                                case NUMERIC:
                                    result.append(cellValue.getNumberValue());
                                    break;
                                case STRING:
                                    result.append(cellValue.getStringValue());
                                    break;
                                case BLANK:
                                    break;
                                case ERROR:
                                    break;

                                // CellType.FORMULA will never happen
                                case FORMULA:
                                    break;
                            }

                        }
                        //string
                        else {
                            //Fix for QCIM1D248808
                            if (!cell.toString().isEmpty() && isNumericCell(cell)) {
                                double aCellValue = cell.getNumericCellValue();
                                cellString = round(Double.toString(aCellValue));
                            }
                            result.append(cellString);
                        }

                    }
                    result.append(columnDelimiter);
                }
                //get rid of last column delimiter
                int index = result.lastIndexOf(columnDelimiter);
                if (index > -1)
                    result = new StringBuilder(result.substring(0, index));
            }

            result.append(rowDelimiter);
        }

        int index = result.lastIndexOf(rowDelimiter);
        if (index > -1)
            result = new StringBuilder(result.substring(0, index));

        return result.toString();
    }

    /**
     * retrieves data from header row
     *
     * @param worksheet    an Excel worksheet
     * @param columnIndex  a list of column indexes
     * @param colDelimiter a column delimiter
     * @return a string of delimited header data
     */
    private static String getHeader(final Sheet worksheet,
                                    final int firstRowIndex,
                                    final List<Integer> columnIndex,
                                    final String colDelimiter) {
        StringBuilder result = new StringBuilder();
        int headerIndex = firstRowIndex - 1;
        final Row headerRow = worksheet.getRow(headerIndex);

        for (int cIndex : columnIndex) {
            final Cell cell = headerRow.getCell(cIndex);
            if (cell != null) {
                String cellString = headerRow.getCell(cIndex).toString();
                result.append(cellString);
            }
            result.append(colDelimiter);
        }

        //get rid of last column index
        final int index = result.lastIndexOf(colDelimiter);
        if (index > -1)
            result = new StringBuilder(result.substring(0, index));

        return result.toString();
    }

    private static boolean isNumericCell(final Cell cell) {
        try {
            cell.getNumericCellValue();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    private static String round(final String value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
        return bd.toString();
    }

    private static boolean isValidExcelFormat(String format) throws Exception {
        if (StringUtils.isBlank(format)) {
            throw new ExcelOperationException(BAD_EXCEL_FILE_MSG);
        } else
            return (format.equalsIgnoreCase(FORMAT_XLSX)
                    || format.equalsIgnoreCase(FORMAT_XLS)
                    || format.equalsIgnoreCase(FORMAT_XLSM));

    }

    private static Workbook getWorkbook(final String fileName) throws Exception {
        final String format = getFileFormat(fileName);
        if (!isValidExcelFormat(format)) {
            throw new InvalidFormatException(BAD_EXCEL_FILE_MSG);
        }

        FileInputStream input = null;
        final Workbook excelDoc;

        try {
            input = new FileInputStream(fileName);
            excelDoc = WorkbookFactory.create(input);
            input.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (input != null) {
                input.close();
            }
        }

        return excelDoc;
    }

    private static String getRowIndex(final Sheet worksheet,
                                      final int firstRow,
                                      final String input,
                                      final int columnIndex,
                                      final String operator) {
        String result = "";
        double cellValueNumeric;
        String cellFormat;

        double inputNumeric = processValueInput(input);

        for (int i = firstRow; i <= worksheet.getLastRowNum(); i++) {
            Row row = worksheet.getRow(i);
            if (row == null) {
                row = worksheet.createRow(i);
            }
            if (row != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                if (cell != null) {
                    CellType cellType = cell.getCellType();
                    if (cellType != CellType.ERROR) {
                        cellFormat = getCellType(cell);
                        //string comparison
                        if (cellFormat.equalsIgnoreCase("string") && inputFormat.equalsIgnoreCase("string")) {
                            DataFormatter aFormatter = new DataFormatter();
                            String aCellString = aFormatter.formatCellValue(cell);
                            if (compareStringValue(aCellString, input, operator)) {
                                result += i + ",";
                            }
                        }
                        //value input is empty, and the cell in the worksheet is in numeric type
                        else if (!cellFormat.equalsIgnoreCase(inputFormat))
                        //((cellType != CellType.STRING && inputFormat.equalsIgnoreCase("string"))||
                        //(cellType != CellType.NUMERIC && !inputFormat.equalsIgnoreCase("string")))
                        {
                            if (operator.equals("!=")) {
                                result += i + ",";
                            }
                        }

                        //numeric comparison
                        else if (cellType == CellType.NUMERIC && !inputFormat.equalsIgnoreCase("string")) {
                            cellValueNumeric = cell.getNumericCellValue();
                            //both are date or time
                            if ((cellFormat.equalsIgnoreCase("date") && inputFormat.equalsIgnoreCase("date")) ||
                                    (cellFormat.equalsIgnoreCase("time") && inputFormat.equalsIgnoreCase("time")) ||
                                    (cellFormat.equalsIgnoreCase("num") && inputFormat.equalsIgnoreCase("num"))) {
                                if (compareNumericValue(cellValueNumeric, inputNumeric, operator)) {
                                    result += i + ",";
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!result.isEmpty()) {
            final int index = result.lastIndexOf(',');
            result = result.substring(0, index);
        }

        return result;
    }

    private static double processValueInput(final String input) {
        double result = 0;

        //check if the input is in number format
        try {
            result = Double.parseDouble(input);
            inputFormat = "num";
        } catch (Exception e) {
        }

        //check if the input is in a percentage format
        if (StringUtils.isBlank(inputFormat)) {
            try {
                int pIndex = input.indexOf("%");
                if (pIndex > -1) {
                    result = percentToDouble(input);
                    inputFormat = "num";
                }
            } catch (Exception e) {

            }
        }

        //check if the input is in a date format(YYYY/MM/DD)
        if (StringUtils.isBlank(inputFormat)) {
            try {
                final Date date = DateUtil.parseYYYYMMDDDate(input);
                result = DateUtil.getExcelDate(date);
                inputFormat = "date";
            } catch (Exception e) {
            }
        }

        //check if the input is in a time format (HH:MM:SS)
        if (StringUtils.isBlank(inputFormat)) {
            try {
                result = DateUtil.convertTime(input);
                inputFormat = "time";
            } catch (Exception e) {
            }
        }
        //check if the input is in a datetime format(YYYY/MM/DD HH:MM:SS)
        if (StringUtils.isBlank(inputFormat)) {
            String[] temp = input.split(" ");
            if (temp.length == 2) {
                try {
                    final String dateString = temp[0];
                    final String timeString = temp[1];
                    final Date date = DateUtil.parseYYYYMMDDDate(dateString);
                    final Double dateDouble = DateUtil.getExcelDate(date);
                    final Double time = DateUtil.convertTime(timeString);
                    result = dateDouble + time;
                    inputFormat = "date";
                } catch (Exception e) {
                }
            }
        }

        if (StringUtils.isBlank(inputFormat)) {
            inputFormat = "string";
        }

        return result;
    }

    public static void updateWorkbook(final Workbook workbook, final String fileName) throws IOException {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(fileName);
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            throw e;
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    protected static Workbook getNewExcelDocument(final String excelFileName) throws Exception {
        final String format = getFileFormat(excelFileName);

        if (StringUtils.isBlank(format)) {
            throw new Exception(BAD_CREATE_EXCEL_FILE_MSG);
        }

        //QCCR139188 need to check if user inputs .xlsx, empty file name
        final int index = excelFileName.lastIndexOf(".");
        final int indexFSlash = excelFileName.lastIndexOf("/");
        final int indexBSlash = excelFileName.lastIndexOf("\\");
        final String fileName;

        //excelFileName contains folder path
        if (indexFSlash > -1) {
            fileName = excelFileName.substring(indexFSlash + 1, index);
        } else if (indexBSlash > -1) {
            fileName = excelFileName.substring(indexBSlash + 1, index);
        }
        //excelFileName doesn't contain folder path
        else {
            fileName = excelFileName.substring(0, index);
        }

        if (StringUtils.isBlank(fileName)) {
            throw new Exception("Excel file name cannot be empty.");
        }

        //if excelFileName doesn't contain '.'
        if (StringUtils.isBlank(format)) {
            throw new ExcelOperationException(BAD_EXCEL_FILE_MSG);
        } else {
            if (format.equalsIgnoreCase(FORMAT_XLSX))
                return new XSSFWorkbook();
            else if (format.equalsIgnoreCase(FORMAT_XLS))
                return new HSSFWorkbook();
        }
        return null;
    }

    private static String getFileFormat(final String excelFileName) {
        final int index = excelFileName.lastIndexOf(".");
        if (index > -1 && index < excelFileName.length()) {
            return excelFileName.substring(index + 1);
        }

        return null;
    }


    private static Workbook getExcelDoc(final String excelFileName) {
        Workbook excelDoc = null;
        try {
            excelDoc = getWorkbook(excelFileName);
            if (excelDoc == null) {
                throw new ExcelOperationException("Could not open " + excelFileName);
            }
        } catch (Exception e) {

        }
        return excelDoc;
    }

    private static Sheet getWorksheet(final Workbook excelDoc, final String sheetName) throws ExcelOperationException {
        final Sheet worksheet = excelDoc.getSheet(sheetName);
        if (worksheet == null) {
            throw new ExcelOperationException("Worksheet " + sheetName + " does not exist.");
        }
        return worksheet;
    }

    private static double percentToDouble(final String percent) throws Exception {
        final double result;
        final String[] number = percent.split("%");
        if (number.length == 1) {
            result = Double.parseDouble(number[0]) / 100;
        } else {
            throw new NumberFormatException();
        }
        return result;
    }

    public static void getMergedCell(final Sheet sheet, final int firstRowIndex, final int cIndex) {
        final int countMRegion = sheet.getNumMergedRegions();

        for (int i = 0; i < countMRegion; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            final int firstRow = range.getFirstRow();
            final int firstColumn = range.getFirstColumn();

            for (int j = firstRowIndex; j < sheet.getLastRowNum(); j++) {
                final boolean isInRange = range.isInRange(j, cIndex);

                Row row = sheet.getRow(j);
                if (row == null) {
                    row = sheet.createRow(j);
                }
                Cell cell = row.getCell(cIndex);
                if (cell == null) {
                    cell = row.createCell(cIndex);
                }
                if (isInRange)
                    if (!(j == firstRow && cIndex == firstColumn)) {
                        cell.setCellType(CellType.ERROR);
                    }
            }
        }
    }

    private static void processFormulaColumn(final Workbook excelDoc,
                                             final Sheet worksheet,
                                             final int firstRow,
                                             final int columnIndex) {

        final FormulaEvaluator evaluator = excelDoc.getCreationHelper().createFormulaEvaluator();
        for (int i = firstRow; i <= worksheet.getLastRowNum(); i++) {
            final Row row = worksheet.getRow(i);
            if (row != null) {
                final Cell cell = row.getCell(columnIndex);
                if (cell != null && (cell.getCellType() != CellType.BLANK)) {
                    //formula type
                    if (cell.getCellType() == CellType.FORMULA) {
                        CellValue cellValue = evaluator.evaluate(cell);

                        switch (cellValue.getCellType()) {
                            case BOOLEAN:
                                cell.setCellType(CellType.STRING);
                                break;
                            case NUMERIC:
                                cell.setCellType(CellType.NUMERIC);
                                break;
                            case STRING:
                                if (StringUtils.isBlank(cell.getStringCellValue())) {
                                    cell.setCellType(CellType.BLANK);
                                } else {
                                    cell.setCellType(CellType.STRING);
                                }
                                break;
                            case BLANK:

                                break;
                            case ERROR:
                                break;

                            // CELL_TYPE_FORMULA will never happen
                            case FORMULA:
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the type of a cell, it can be num, date, time or string
     *
     * @param cell
     * @return
     */
    private static String getCellType(final Cell cell) {
        final String result;
        final double cellValueNumeric;
        final CellType cellType = cell.getCellType();

        if (cellType == CellType.NUMERIC) {
            cellValueNumeric = cell.getNumericCellValue();

            //date cell, it can be date, time or datetime
            if (DateUtil.isCellDateFormatted(cell)) {
                //time cell
                if (cellValueNumeric < 1) {
                    result = "time";
                }
                //date cell
                else {
                    result = "date";
                }
            }
            //numeric cell
            else {
                result = "num";
            }
        }
        //String cell
        else {
            result = "string";
        }
        return result;
    }

    /**
     * compare the numeric values
     *
     * @param value1
     * @param value2
     * @param operator
     * @return true or false
     * @throws Exception
     */
    private static boolean compareNumericValue(double value1, double value2, String operator) {
        boolean result = false;
        if (operator.equals("==")) {
            if (value1 == value2) result = true;
        } else if (operator.equals("!=")) {
            if (value1 != value2) result = true;
        } else if (operator.equals("<")) {
            if (value1 < value2) result = true;
        } else if (operator.equals(">")) {
            if (value1 > value2) result = true;
        } else if (operator.equals("<=")) {
            if (value1 <= value2) result = true;
        } else if (operator.equals(">=")) {
            if (value1 >= value2) result = true;
        }

        return result;
    }

    /**
     * compare the string values
     *
     * @param s1
     * @param s2
     * @param operator a math operator
     * @return true or false
     * @throws Exception
     */
    private static boolean compareStringValue(String s1, String s2, String operator) {
        boolean result = false;
        if (operator.equals("==")) {
            if (s1.equals(s2))
                result = true;
        } else if (operator.equals("!=")) {
            if (!s1.equals(s2))
                result = true;
        }

        return result;
    }

    /**
     * get last column index
     */
    private static int getLastColumnIndex(Sheet worksheet, int firstRowIndex, int lastRowIndex) {
        //get the last column index in a sheet
        int tempLastColumnIndex;
        int lastColumnIndex = 0;
        for (int i = firstRowIndex; i <= lastRowIndex; i++) {
            final Row row = worksheet.getRow(i);
            if (row != null) {
                tempLastColumnIndex = row.getLastCellNum() - 1;
                if (tempLastColumnIndex > lastColumnIndex) {
                    lastColumnIndex = tempLastColumnIndex;
                }
            }
        }

        return lastColumnIndex;
    }

    private static List<Integer> processIndex(String index) {
        final List<Integer> result = new ArrayList<>();

        final String[] temp = index.split(",");
        String[] tempArray;

        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].trim();
            tempArray = temp[i].split(":");

            //not a range index
            if (tempArray.length == 1) {
                int tmp = Integer.parseInt(temp[i]);
                result.add(tmp);

            }
            // range index
            else if (tempArray.length == 2) {
                tempArray[0] = tempArray[0].trim();
                tempArray[1] = tempArray[1].trim();
                final int start = Integer.parseInt(tempArray[0]);
                final int end = Integer.parseInt(tempArray[1]);

                for (int j = start; j <= end; j++) {
                    if (!result.contains(j)) {
                        result.add(j);
                    }
                }

            }
        }

        return result;
    }

    private static List<Integer> validateIndex(List<Integer> indexList, int firstIndex, int lastIndex, boolean isRow) throws Exception {
        final List<Integer> resultList = new ArrayList<>();
        for (Integer index : indexList) {
            //trim the row or column index if it's above range
            if (index >= firstIndex && index <= lastIndex) {
                resultList.add(index);
            }
            if (index < 0) {
                if (isRow) {
                    throw new ExcelOperationException("The rowIndex input is not valid. " +
                            "The valid row index must be equal or greater than 0.");
                } else {
                    throw new ExcelOperationException("The columnIndex input is not valid. " +
                            "The valid column index must be equal or greater than 0.");
                }

            }

        }

        return resultList;
    }

    public static int deleteCell(Sheet worksheet, List<Integer> rowIndex, List<Integer> columnIndex) {
        int rowsDeleted = 0;

        for (Integer rIndex : rowIndex) {
            Row aRow = worksheet.getRow(rIndex);

            if (aRow != null) {
                for (Integer cIndex : columnIndex) {
                    Cell aCell = aRow.getCell(cIndex);
                    if (aCell != null) {
                        aRow.removeCell(aCell);
                    }
                }
                rowsDeleted++;
            }
        }

        return rowsDeleted;
    }


}
