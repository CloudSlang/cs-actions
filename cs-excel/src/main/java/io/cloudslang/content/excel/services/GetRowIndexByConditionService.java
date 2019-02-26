package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.GetRowIndexByConditionInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Map;

import static io.cloudslang.content.excel.services.ExcelServiceImpl.*;
import static io.cloudslang.content.excel.utils.Outputs.GetRowIndexByCondition.ROWS_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class GetRowIndexByConditionService {
    private static String inputFormat = null;

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

    public static double processValueInput(final String input) {
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


}
