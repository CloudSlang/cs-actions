package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.AddExcelDataInputs;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.excel.services.ExcelServiceImpl.*;
import static io.cloudslang.content.excel.utils.Constants.BAD_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.ROW_DATA_REQD_MSG;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class AddExcelDataService {

    @NotNull
    public static Map<String, String> addExcelData(AddExcelDataInputs addExcelDataInputs) {
        boolean hasHeaderData = false;
        try {
            final String excelFileName = addExcelDataInputs.getCommonInputs().getExcelFileName();
            final String format = getFileFormat(excelFileName);
            final Workbook excelDoc;

            if (isValidExcelFormat(format)) {
                excelDoc = getWorkbook(excelFileName);
            } else {
                return getFailureResultsMap(BAD_EXCEL_FILE_MSG);
            }
            int rowsAdded;
            if (excelDoc == null) {
                return getFailureResultsMap("Could not open " + excelFileName);
            }
            final String sheetName = addExcelDataInputs.getCommonInputs().getWorksheetName();
            final Sheet worksheet = excelDoc.getSheet(sheetName);
            if (worksheet == null) {
                return getFailureResultsMap("Worksheet " + sheetName + " does not exist.");
            }
            String columnDelimiter = addExcelDataInputs.getColumnDelimiter();
            String rowDelimiter = addExcelDataInputs.getRowDelimiter();
            final String[] specialChar = {"\\", "?", "|", "*", "$", ".", "+", "(", ")", "{", "}", "[", "]"};
            for (int i = 0; i < specialChar.length; i++) {
                rowDelimiter = rowDelimiter.replace(specialChar[i], "\\" + specialChar[i]);
                columnDelimiter = columnDelimiter.replace(specialChar[i], "\\" + specialChar[i]);
            }
            final String headerData = addExcelDataInputs.getHeaderData();
            if (!StringUtils.isBlank(headerData)) {
                hasHeaderData = true;
                setHeaderRow(worksheet, headerData, columnDelimiter);
            }

            final String rowData = addExcelDataInputs.getRowData();
            final String rowIndex = addExcelDataInputs.getRowIndex();
            final String columnIndex = addExcelDataInputs.getColumnIndex();
            final String overwriteString = addExcelDataInputs.getOverwriteData();
            final List<Integer> rowIndexList = processIndex(rowIndex, worksheet, rowData, rowDelimiter, columnDelimiter, true, hasHeaderData);
            final List<Integer> columnIndexList = processIndex(columnIndex, worksheet, rowData, rowDelimiter, columnDelimiter, false, hasHeaderData);
            final boolean overwrite = Boolean.valueOf(overwriteString.toLowerCase());

            if (!overwrite)
                shiftRows(worksheet, rowIndexList);

            if (StringUtils.isBlank(rowData)) {
                return getFailureResultsMap(ROW_DATA_REQD_MSG);
            } else {
                rowsAdded = setDataRows(worksheet, rowData, rowDelimiter, columnDelimiter, rowIndexList, columnIndexList);
            }

            updateWorkbook(excelDoc, excelFileName);
            return getSuccessResultsMap(String.valueOf(rowsAdded));

        } catch (Exception e) {
            return getFailureResultsMap(e.getMessage());
        }
    }
    /**
     * Adds (inserts/appends) specified data to the worksheet.
     *
     * @param worksheet       Worksheet where the rowData will be added
     * @param rowData         Data to be added to the worksheet
     * @param rowDelimiter    Delimiter for rows in rowData
     * @param columnDelimiter Delimiter for column in rowData
     * @param rowIndexList    List of row indexes where data will be added in the worksheet
     * @param columnIndexList List of column indexes where data will be added in the worksheet
     * @return Number of rows that were added to the worksheet
     * @throws Exception Input list sizes doesn't match
     */
    private static int setDataRows(final Sheet worksheet, final String rowData, final String rowDelimiter, final String columnDelimiter,
                                   final List<Integer> rowIndexList, final List<Integer> columnIndexList) {
        final String[] rows = rowData.split(rowDelimiter);
        String[] columns;

        if (rows.length != rowIndexList.size())
            throw new IllegalArgumentException("Row index list size doesn't match rowData row count.");

        for (int i = 0; i < rowIndexList.size(); i++) {
            Row row = worksheet.getRow(rowIndexList.get(i));
            if (row == null) {
                row = worksheet.createRow(rowIndexList.get(i));
            }
            columns = rows[i].split(columnDelimiter);
            if (columns.length != columnIndexList.size())
                throw new IllegalArgumentException("Column index list size doesn't match rowData column count.");
            for (int j = 0; j < columnIndexList.size(); j++) {
                Cell cell = row.getCell(columnIndexList.get(j));
                if (cell == null) {
                    cell = row.createCell(columnIndexList.get(j));
                }
                try {
                    double numberValue = Double.parseDouble(columns[j].trim());
                    cell.setCellValue(numberValue);
                }
                //for non-numeric value
                catch (NumberFormatException e) {
                    cell.setCellValue(columns[j].trim());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid row data");
                }
            }
        }
        return rowIndexList.size();
    }

    /**
     * Inserts rows at the specified indexes in the worksheet
     *
     * @param worksheet    Worksheet where rows will be inserted
     * @param rowIndexList List of row indexes where rows will be inserted
     */
    public static void shiftRows(final Sheet worksheet, final List<Integer> rowIndexList) {
        int insertPoint;
        int nRows;
        int i = 0;
        while (i < rowIndexList.size()) {
            insertPoint = rowIndexList.get(i);
            nRows = 1;
            while (i < rowIndexList.size() - 1 && (insertPoint + nRows == rowIndexList.get(i + 1))) {
                nRows++;
                i++;
            }
            if (insertPoint > worksheet.getLastRowNum()) {
                for (int j = insertPoint; j < insertPoint + nRows; j++) {
                    worksheet.createRow(j);
                }
            } else {
                worksheet.shiftRows(insertPoint, worksheet.getLastRowNum(), nRows, false, true);
            }
            i++;
        }
    }

    public static int setDataRows(final Sheet worksheet,
                                  final String rowData,
                                  final String rowDelimiter,
                                  final String columnDelimiter,
                                  final int startRowIndex,
                                  final int startColumnIndex) {
		/*StringTokenizer dataTokens = new StringTokenizer(rowData, rowDelimiter);
		int rowIndex = startRowIndex;

		while (dataTokens.hasMoreTokens())
		{
			int columnIndex = startColumnIndex;
			Row dataRow = worksheet.getRow(rowIndex);
			if (dataRow == null)
				dataRow = worksheet.createRow(rowIndex);

			StringTokenizer rowToken = new StringTokenizer(dataTokens.nextToken(), columnDelimiter);
			while (rowToken.hasMoreTokens())
			{
				Cell cell = dataRow.getCell(columnIndex);
				if (cell == null)
					cell = dataRow.createCell(columnIndex);

				cell.setCellValue(rowToken.nextToken());
				columnIndex++;
			}

			rowIndex++;
		}

		return (rowIndex-startRowIndex);*/
        //QCCR 139182 allow user to enter an empty row in the middle

        String[] tmpRow = rowData.split(rowDelimiter);

        int rowIndex = startRowIndex;

        for (int i = 0; i < tmpRow.length; i++) {
            int columnIndex = startColumnIndex;
            Row row = worksheet.getRow(rowIndex);
            if (row == null) {
                row = worksheet.createRow(rowIndex);
            }

            String[] tmpCol = tmpRow[i].split(columnDelimiter);
            for (int j = 0; j < tmpCol.length; j++) {
                Cell cell = row.getCell(columnIndex);
                if (cell == null) {
                    cell = row.createCell(columnIndex);
                }
                try {
                    double value_num = Double.parseDouble(tmpCol[j].trim());
                    cell.setCellValue(value_num);
                }
                //for non-numeric value
                catch (Exception e) {
                    cell.setCellValue(tmpCol[j].trim());
                }
                columnIndex++;
            }
            rowIndex++;
        }
        return tmpRow.length;
    }

    public static void setHeaderRow(final Sheet worksheet, final String headerData, final String delimiter) {
		/*StringTokenizer headerTokens = new StringTokenizer(headerData, delimiter);
		Row headerRow = worksheet.createRow(0);
		int columnIndex = 0;
		while (headerTokens.hasMoreTokens())
		{
			Cell cell = headerRow.createCell(columnIndex);
			cell.setCellValue(headerTokens.nextToken());
			columnIndex++;
		}*/
        final Row headerRow = worksheet.createRow(0);

        final String[] tmp = headerData.split(delimiter);
        for (int i = 0; i < tmp.length; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null) {
                cell = headerRow.createCell(i);
            }
            try {
                double valueNumeric = Double.parseDouble(tmp[i].trim());
                cell.setCellValue(valueNumeric);
            }
            //for non-numeric value
            catch (Exception e) {
                cell.setCellValue(tmp[i].trim());
            }
        }
    }

}
