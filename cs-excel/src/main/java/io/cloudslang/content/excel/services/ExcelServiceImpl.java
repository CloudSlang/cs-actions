package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.ExcelOperationException;
import io.cloudslang.content.excel.entities.GetRowIndexByConditionInputs;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static io.cloudslang.content.excel.utils.Constants.BAD_CREATE_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.BAD_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLS;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSM;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSX;
import static io.cloudslang.content.excel.utils.Outputs.GetRowIndexByCondition.ROWS_COUNT;

public class ExcelServiceImpl {
    private static String inputFormat = null;

    public static Map<String, String> getRowIndexbyCondition(@NotNull final GetRowIndexByConditionInputs getRowIndexbyConditionInputs) {
        String resultString = "";
        Map<String, String> result;
        Sheet worksheet = null;
        Workbook excelDoc = null;

        try {
            excelDoc = getExcelDoc(getRowIndexbyConditionInputs.getCommonInputs().getExcelFileName());
            worksheet = getWorksheet(excelDoc, getRowIndexbyConditionInputs.getCommonInputs().getWorksheetName());
        } catch (Exception e) {

        }

        int firstRowIndex = Integer.parseInt(getRowIndexbyConditionInputs.getFirstRowIndex());
        int cIndex_int = Integer.parseInt(getRowIndexbyConditionInputs.getColumnIndexToQuery());
        String value = getRowIndexbyConditionInputs.getValue();
        String operator = getRowIndexbyConditionInputs.getOperator();

        getMergedCell(worksheet, firstRowIndex, cIndex_int);
        processFormulaColumn(worksheet, firstRowIndex, cIndex_int, excelDoc);

        int rowsCount = 0;
        try {
            resultString = getRowIndex(worksheet, firstRowIndex, value, cIndex_int, operator);
            rowsCount = resultString.split(",").length;
        } catch (Exception e) {

        }
        if (!StringUtils.isBlank(resultString)) {
            result = OutputUtilities.getSuccessResultsMap(resultString);
            result.put(ROWS_COUNT, String.valueOf(rowsCount));

        } else {
            result = OutputUtilities.getSuccessResultsMap("");
            result.put(ROWS_COUNT, String.valueOf(0));
        }
        return result;

    }

    protected static boolean isValidExcelFormat(String format) throws Exception {
        if (StringUtils.isBlank(format)) {
            throw new ExcelOperationException(BAD_EXCEL_FILE_MSG);
        } else if (format.equalsIgnoreCase(FORMAT_XLSX)
                || format.equalsIgnoreCase(FORMAT_XLS)
                || format.equalsIgnoreCase(FORMAT_XLSM)) {
            return true;
        }

        return false;
    }

    private static Workbook getWorkbook(String fileName) throws Exception {
        String format = getFileFormat(fileName);
        if (!isValidExcelFormat(format)) {
            throw new InvalidFormatException(BAD_EXCEL_FILE_MSG);
        }

        FileInputStream input = null;
        Workbook excelDoc = null;

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

    private static String getRowIndex(Sheet ws, int firstRow, String input, int cIndx, String operator) throws Exception {
        String result = "";
        double cellValue_num;
        String cellFormat = null;

        double input_num = processValueInput(input);

        for (int i = firstRow; i <= ws.getLastRowNum(); i++) {
            Row aRow = ws.getRow(i);
            if (aRow == null) {
                aRow = ws.createRow(i);
            }
            if (aRow != null) {
                Cell aCell = aRow.getCell(cIndx);
                if (aCell == null) {
                    aCell = aRow.createCell(cIndx);
                }
                if (aCell != null) {
                    int cellType = aCell.getCellType();
                    if (cellType != Cell.CELL_TYPE_ERROR) {
                        cellFormat = getCellType(aCell);
                        //string comparison
                        if (cellFormat.equalsIgnoreCase("string") && inputFormat.equalsIgnoreCase("string")) {
                            DataFormatter aFormatter = new DataFormatter();
                            String aCellString = aFormatter.formatCellValue(aCell);
                            if (valueCompare_string(aCellString, input, operator)) {
                                result += i + ",";
                            }
                        }
                        //value input is empty, and the cell in the worksheet is in numeric type
                        else if (!cellFormat.equalsIgnoreCase(inputFormat))
                        //((cellType != Cell.CELL_TYPE_STRING && inputFormat.equalsIgnoreCase("string"))||
                        //(cellType != Cell.CELL_TYPE_NUMERIC && !inputFormat.equalsIgnoreCase("string")))
                        {
                            if (operator.equals("!=")) {
                                result += i + ",";
                            }
                        }

                        //numeric comparison
                        else if (cellType == Cell.CELL_TYPE_NUMERIC && !inputFormat.equalsIgnoreCase("string")) {
                            cellValue_num = aCell.getNumericCellValue();
                            //both are date or time
                            if ((cellFormat.equalsIgnoreCase("date") && inputFormat.equalsIgnoreCase("date")) ||
                                    (cellFormat.equalsIgnoreCase("time") && inputFormat.equalsIgnoreCase("time")) ||
                                    (cellFormat.equalsIgnoreCase("num") && inputFormat.equalsIgnoreCase("num"))) {
                                if (valueCompare_num(cellValue_num, input_num, operator)) {
                                    result += i + ",";
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!result.isEmpty()) {
            int indx = result.lastIndexOf(',');
            result = result.substring(0, indx);
        }

        return result;
    }

    private static double processValueInput(String input) {
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
                Date aDate = DateUtil.parseYYYYMMDDDate(input);
                result = DateUtil.getExcelDate(aDate);
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
                    String dateString = temp[0];
                    String timeString = temp[1];
                    Date aDate = DateUtil.parseYYYYMMDDDate(dateString);
                    Double date = DateUtil.getExcelDate(aDate);
                    Double time = DateUtil.convertTime(timeString);
                    result = date + time;
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

    public static void updateWorkbook(Workbook workbook, String fileName) throws IOException {
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

    protected Workbook getNewExcelDocument(String excelFileName) throws Exception {
        String format = getFileFormat(excelFileName);

        if (StringUtils.isBlank(format)) {
            throw new Exception(BAD_CREATE_EXCEL_FILE_MSG);
        }

        //QCCR139188 need to check if user inputs .xlsx, empty file name
        int index = excelFileName.lastIndexOf(".");
        int indexFSlash = excelFileName.lastIndexOf("/");
        int indexBSlash = excelFileName.lastIndexOf("\\");
        String fileName = null;

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

    protected static String getFileFormat(String excelFileName) {
        int index = excelFileName.lastIndexOf(".");
        if (index > -1 && index < excelFileName.length()) {
            return excelFileName.substring(index + 1);
        }

        return null;
    }


    private static Workbook getExcelDoc(String excelFileName) {
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

    private static Sheet getWorksheet(Workbook excelDoc, String sheetName) throws ExcelOperationException {
        Sheet worksheet = excelDoc.getSheet(sheetName);
        if (worksheet == null) {
            throw new ExcelOperationException("Worksheet " + sheetName + " does not exist.");
        }
        return worksheet;
    }

    private static double percentToDouble(String percent) throws Exception {
        double result = 0;
        String[] number = percent.split("%");
        if (number.length == 1) {
            result = Double.parseDouble(number[0]) / 100;
        } else {
            throw new NumberFormatException();
        }
        return result;
    }

    public static void getMergedCell(Sheet sheet, int firstRowIndex, int cIndex) {
        int countMRegion = sheet.getNumMergedRegions();

        for (int i = 0; i < countMRegion; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int fRow = range.getFirstRow();
            int fCol = range.getFirstColumn();

            for (int j = firstRowIndex; j < sheet.getLastRowNum(); j++) {
                boolean isInRange = range.isInRange(j, cIndex);

                Row aRow = sheet.getRow(j);
                if (aRow == null) {
                    aRow = sheet.createRow(j);
                }
                Cell aCell = aRow.getCell(cIndex);
                if (aCell == null) {
                    aCell = aRow.createCell(cIndex);
                }
                if (isInRange)
                    if (!(j == fRow && cIndex == fCol)) {
                        aCell.setCellType(Cell.CELL_TYPE_ERROR);
                    }
            }
        }
    }

    private static void processFormulaColumn(Sheet ws, int firstRow, int cIndx, Workbook excelDoc) {

        FormulaEvaluator evaluator = excelDoc.getCreationHelper().createFormulaEvaluator();
        for (int i = firstRow; i <= ws.getLastRowNum(); i++) {
            Row aRow = ws.getRow(i);
            if (aRow != null) {
                Cell aCell = aRow.getCell(cIndx);
                if (aCell != null && (aCell.getCellType() != Cell.CELL_TYPE_BLANK)) {
                    //formula type
                    if (aCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                        CellValue cellValue = evaluator.evaluate(aCell);

                        switch (cellValue.getCellType()) {
                            case Cell.CELL_TYPE_BOOLEAN:
                                aCell.setCellType(Cell.CELL_TYPE_STRING);
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                aCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if (StringUtils.isBlank(aCell.getStringCellValue())) {
                                    aCell.setCellType(Cell.CELL_TYPE_BLANK);
                                } else {
                                    aCell.setCellType(Cell.CELL_TYPE_STRING);
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:

                                break;
                            case Cell.CELL_TYPE_ERROR:
                                break;

                            // CELL_TYPE_FORMULA will never happen
                            case Cell.CELL_TYPE_FORMULA:
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
     * @param aCell
     * @return
     */
    private static String getCellType(Cell aCell) {
        String result = "";
        double cellValue_num;
        int cellType = aCell.getCellType();

        if (cellType == Cell.CELL_TYPE_NUMERIC) {
            cellValue_num = aCell.getNumericCellValue();

            //date cell, it can be date, time or datetime
            if (DateUtil.isCellDateFormatted(aCell)) {
                //time cell
                if (cellValue_num < 1) {
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
     * @param a
     * @param b
     * @param operator
     * @return true or false
     * @throws Exception
     */
    private static boolean valueCompare_num(double a, double b, String operator) {
        boolean result = false;
        if (operator.equals("==")) {
            if (a == b) result = true;
        } else if (operator.equals("!=")) {
            if (a != b) result = true;
        } else if (operator.equals("<")) {
            if (a < b) result = true;
        } else if (operator.equals(">")) {
            if (a > b) result = true;
        } else if (operator.equals("<=")) {
            if (a <= b) result = true;
        } else if (operator.equals(">=")) {
            if (a >= b) result = true;
        }

        return result;
    }

    /**
     * compare the string values
     *
     * @param a
     * @param b
     * @param operator a math operator
     * @return true or false
     * @throws Exception
     */
    private static boolean valueCompare_string(String a, String b, String operator) {
        boolean result = false;
        if (operator.equals("==")) {
            if (a.equals(b)) result = true;
        } else if (operator.equals("!=")) {
            if (!a.equals(b)) result = true;
        }

        return result;
    }
}
