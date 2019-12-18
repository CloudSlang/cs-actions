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

import io.cloudslang.content.excel.entities.GetCellInputs;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.excel.services.ExcelServiceImpl.getExcelDoc;
import static io.cloudslang.content.excel.services.ExcelServiceImpl.getLastColumnIndex;
import static io.cloudslang.content.excel.services.ExcelServiceImpl.getWorksheet;
import static io.cloudslang.content.excel.services.ExcelServiceImpl.processIndex;
import static io.cloudslang.content.excel.services.ExcelServiceImpl.validateIndex;
import static io.cloudslang.content.excel.utils.Constants.YES;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.COLUMNS_COUNT;
import static io.cloudslang.content.excel.utils.Outputs.GetCellOutputs.HEADER;
import static io.cloudslang.content.excel.utils.Outputs.GetRowIndexByCondition.ROWS_COUNT;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetCellService {

    @NotNull
    public static Map<String, String> getCell(@NotNull final GetCellInputs getCellInputs) {
        try {
            final Workbook excelDoc = getExcelDoc(getCellInputs.getCommonInputs().getExcelFileName());
            final Sheet worksheet = getWorksheet(excelDoc, getCellInputs.getCommonInputs().getWorksheetName());

            int firstRowIndex = Integer.parseInt(getCellInputs.getFirstRowIndex());
            final int lastRowIndex = worksheet.getLastRowNum();
            final int firstColumnIndex = 0;
            final int lastColumnIndex = getLastColumnIndex(worksheet, firstRowIndex, lastRowIndex);
            final String rowDelimiter = getCellInputs.getRowDelimiter();
            final String columnDelimiter = getCellInputs.getColumnDelimiter();
            final String hasHeader = getCellInputs.getHasHeader();

            if (hasHeader.equals(YES))
                firstRowIndex++;

            final String rowIndexDefault = firstRowIndex + ":" + lastRowIndex;
            final String columnIndexDefault = firstColumnIndex + ":" + lastColumnIndex;
            final String rowIndex = defaultIfEmpty(getCellInputs.getRowIndex(), rowIndexDefault);
            final String columnIndex = defaultIfEmpty(getCellInputs.getColumnIndex(), columnIndexDefault);

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
        if(headerRow == null)
            return EMPTY;
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

}
