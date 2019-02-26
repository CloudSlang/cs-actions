package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.ExcelOperationException;
import io.cloudslang.content.excel.entities.ModifyCellInputs;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.excel.services.ExcelServiceImpl.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ModifyCellService {
    private static boolean incompleted;

    @NotNull
    public static Map<String, String> modifyCell(@NotNull final ModifyCellInputs modifyCellInputs) {
        try {
            final String excelFileName = modifyCellInputs.getCommonInputs().getExcelFileName();
            final Workbook excelDoc = getExcelDoc(excelFileName);
            final Sheet worksheet = getWorksheet(excelDoc, modifyCellInputs.getCommonInputs().getWorksheetName());

            final int firstRowIndex = worksheet.getFirstRowNum();
            final int lastRowIndex = worksheet.getLastRowNum();
            final int firstColumnIndex = 0;
            final int lastColumnIndex = getLastColumnIndex(worksheet, firstRowIndex, lastRowIndex);
            final String columnDelimiter = modifyCellInputs.getColumnDelimiter();
            final String newValue = modifyCellInputs.getNewValue();

            final String rowIndexDefault = firstRowIndex + ":" + lastRowIndex;
            final String columnIndexDefault = firstColumnIndex + ":" + lastColumnIndex;
            final String rowIndex = defaultIfEmpty(modifyCellInputs.getRowIndex(), rowIndexDefault);
            final String columnIndex = defaultIfEmpty(modifyCellInputs.getColumnIndex(), columnIndexDefault);

            final List<Integer> rowIndexList = validateIndex(processIndex(rowIndex), firstRowIndex, lastRowIndex, true);
            final List<Integer> columnIndexList = validateIndex(processIndex(columnIndex), firstColumnIndex, lastColumnIndex, false);

            final List<String> dataList = getDataList(newValue, columnIndexList, columnDelimiter);

            incompleted = false;
            final int modifyCellDataResult = modifyCellData(worksheet, rowIndexList, columnIndexList, dataList);

            if (modifyCellDataResult != 0) {
                //update formula cells
                final FormulaEvaluator evaluator = excelDoc.getCreationHelper().createFormulaEvaluator();
                for (Row row : worksheet) {
                    for (Cell cell : row) {
                        if (cell.getCellType() == CellType.FORMULA) {
                            evaluator.evaluateFormulaCell(cell);
                        }
                    }
                }
                updateWorkbook(excelDoc, excelFileName);
            }

            if (modifyCellDataResult == rowIndexList.size() && !incompleted) {
                return getSuccessResultsMap(String.valueOf(modifyCellDataResult));
            } else {
                return getFailureResultsMap(String.valueOf(modifyCellDataResult));
            }
        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }

    private static int modifyCellData(final Sheet worksheet,
                                      final List<Integer> rowIndexList,
                                      final List<Integer> columnIndexList,
                                      final List<String> dataList) {
        int rowCount = 0;

        for (Integer rowIndex : rowIndexList) {
            boolean isModified = false;
            int i = 0;
            Row row = worksheet.getRow(rowIndex);
            //if the specified row does not exist
            if (row == null) {
                row = worksheet.createRow(rowIndex);

            }
            for (Integer columnIndex : columnIndexList) {
                Cell cell = row.getCell(columnIndex);
                //if the specified cell does not exist
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                //the cell is a merged cell, cannot modify it
                if (isMergedCell(worksheet, rowIndex, columnIndex)) {
                    i++;
                    incompleted = true;
                } else {
                    //if the cell needs to be modified is in formula type,
                    if (cell.getCellType() == CellType.FORMULA) {
                        cell.setCellType(CellType.STRING);
                    }
                    try {
                        double valueNumeric = Double.parseDouble(dataList.get(i).trim());
                        cell.setCellValue(valueNumeric);
                    }
                    //for non-numeric value
                    catch (Exception e) {
                        try {
                            Date date = new Date(dataList.get(i).trim());
                            cell.setCellValue(date);
                        } catch (Exception e1) {
                            cell.setCellValue(dataList.get(i).trim());
                        }
                    }
                    i++;
                    isModified = true;
                }
            }
            if (isModified) rowCount++;
        }

        return rowCount;
    }

    public static boolean isMergedCell(final Sheet worksheet, final int rowIndex, final int columnIndex) {
        int countMRegion = worksheet.getNumMergedRegions();

        for (int i = 0; i < countMRegion; i++) {
            CellRangeAddress range = worksheet.getMergedRegion(i);
            int firstRow = range.getFirstRow();
            int firstColumn = range.getFirstColumn();

            boolean isInRange = range.isInRange(rowIndex, columnIndex);

            if (isInRange) {
                if (!(rowIndex == firstRow && columnIndex == firstColumn && isInRange)) {
                    return true;
                }
            }

        }
        return false;
    }

    private static List<String> getDataList(final String newValue, final List<Integer> columnIndexList, final String columnDelimiter) throws Exception {
        final List<String> dataList = Arrays.asList(newValue.split(columnDelimiter));

        if (dataList.size() != columnIndexList.size()) {
            throw new ExcelOperationException("The data input is not valid. " +
                    "The size of data input should be the same as size of columnIndex input, which is " + columnIndexList.size() + ".");
        }

        return dataList;
    }


}
