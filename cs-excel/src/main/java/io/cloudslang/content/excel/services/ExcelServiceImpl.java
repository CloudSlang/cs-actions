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

package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.ExcelOperationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.excel.utils.Constants.BAD_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLS;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSM;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSX;

/**
 * Created by danielmanciu on 18.02.2019.
 */
public class ExcelServiceImpl {

    public static Workbook getWorkbook(final String fileName) throws IOException,InvalidFormatException,ExcelOperationException {
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

    public static boolean isValidExcelFormat(final String format) throws ExcelOperationException {
        if (StringUtils.isBlank(format)) {
            throw new ExcelOperationException(BAD_EXCEL_FILE_MSG);
        } else
            return (format.equalsIgnoreCase(FORMAT_XLSX)
                    || format.equalsIgnoreCase(FORMAT_XLS)
                    || format.equalsIgnoreCase(FORMAT_XLSM));

    }

    public static void updateWorkbook(final Workbook workbook, final String fileName) throws IOException {
        try (FileOutputStream output = new FileOutputStream(fileName)) {
            workbook.write(output);
        } catch (IOException e) {
            throw e;
        }
    }

    public static String getFileFormat(final String excelFileName) {
        final int index = excelFileName.lastIndexOf(".");
        if (index > -1 && index < excelFileName.length()) {
            return excelFileName.substring(index + 1);
        }

        return null;
    }

    public static Workbook getExcelDoc(final String excelFileName) throws ExcelOperationException,InvalidFormatException,IOException{
        Workbook excelDoc = null;
            excelDoc = getWorkbook(excelFileName);
            if (excelDoc == null) {
                throw new ExcelOperationException("Could not open " + excelFileName);
            }
        return excelDoc;
    }

    public static Sheet getWorksheet(final Workbook excelDoc, final String sheetName) throws ExcelOperationException {
        final Sheet worksheet = excelDoc.getSheet(sheetName);
        if (worksheet == null) {
            throw new ExcelOperationException("Worksheet " + sheetName + " does not exist.");
        }
        return worksheet;
    }

    /**
     * Get the type of a cell, it can be num, date, time or string
     *
     * @param cell
     * @return
     */
    public static String getCellType(final Cell cell) {
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
    public static boolean compareNumericValue(double value1, double value2, final String operator) {
        boolean result = false;
        switch (operator) {
            case "==":
                if (value1 == value2) result = true;
                break;
            case "!=":
                if (value1 != value2) result = true;
                break;
            case "<":
                if (value1 < value2) result = true;
                break;
            case ">":
                if (value1 > value2) result = true;
                break;
            case "<=":
                if (value1 <= value2) result = true;
                break;
            case ">=":
                if (value1 >= value2) result = true;
                break;
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
    public static boolean compareStringValue(final String s1, final String s2, final String operator) {
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

    public static int getLastColumnIndex(final Sheet worksheet, final int firstRowIndex, final int lastRowIndex) {
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

    public static List<Integer> processIndex(final String index) {
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

    public static List<Integer> validateIndex(final List<Integer> indexList,
                                              final int firstIndex,
                                              final int lastIndex,
                                              final boolean isRow) throws Exception {
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

    /**
     * Constructs a list of indexes where the data will be added in the worksheet
     *
     * @param index           A list of indexes
     * @param worksheet       The worksheet where the data will be added
     * @param rowData         Data that will be added to the worksheet
     * @param rowDelimiter    rowData row delimiter
     * @param columnDelimiter rowData column delimiter
     * @param isRow           true - if the index list (param) contains row indexes
     * @return List of indexes where data will be added in the worksheet
     */
    public static List<Integer> processIndex(final String index, final Sheet worksheet, final String rowData, final String rowDelimiter,
                                             final String columnDelimiter, final boolean isRow, final boolean hasHeader) {
        final String[] rows = rowData.split(rowDelimiter);
        String[] indexArray = null;
        if (!StringUtils.isBlank(index)) {
            indexArray = index.split(",");
        }
        int sheetLastRowIndex = worksheet.getLastRowNum();
        if (sheetLastRowIndex > 0) {
            sheetLastRowIndex++;
        }
        final int dataRows = rows.length;
        final int dataColumns = rows[0].split(columnDelimiter).length;
        int headerOffset = 0;
        if (hasHeader) {
            headerOffset = 1;
        }
        if (isRow) {
            return processIndexWithOffset(indexArray, headerOffset, sheetLastRowIndex, sheetLastRowIndex + dataRows);
        } else {
            return processIndexWithOffset(indexArray, 0, 0, dataColumns);
        }
    }

    /**
     * Processes the index list with an offset
     *
     * @param indexArray List of indexes to be processed
     * @param offset     Index offset (Apache poi works with 0 index based excel files while Microsoft Excel file starts with index 1)
     * @param startIndex Start index for the default case (ex. append for rows)
     * @param endIndex   End index for the default case (ex. append for rows)
     * @return List of indexes where data will be added in the worksheet
     */
    private static List<Integer> processIndexWithOffset(final String[] indexArray, final int offset, final int startIndex, final int endIndex) {
        final List<Integer> indexList = new ArrayList<>();
        String[] range;
        if (indexArray != null) {
            for (String index : indexArray) {
                range = index.split(":");
                // adding every row/column in the range
                if (range.length > 1) {
                    for (int ind = Integer.parseInt(range[0].trim()); ind <= Integer.parseInt(range[1].trim()); ind++) {
                        indexList.add(ind + offset);
                    }
                } else {
                    indexList.add(Integer.parseInt(range[0].trim()) + offset);
                }
            }
        } else {
            // default case
            for (int i = startIndex; i < endIndex; i++) {
                if (startIndex == 0) {
                    indexList.add(i + offset);
                } else {
                    indexList.add(i);
                }
            }
        }
        return indexList;
    }
}
