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
import io.cloudslang.content.excel.entities.CreateExcelFileInputs;
import io.cloudslang.content.utils.OutputUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.StringTokenizer;

import static io.cloudslang.content.excel.services.ExcelServiceImpl.getFileFormat;
import static io.cloudslang.content.excel.utils.Constants.BAD_CREATE_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.BAD_EXCEL_FILE_MSG;
import static io.cloudslang.content.excel.utils.Constants.EXCEPTION_WORKSHEET_NAME_EMPTY;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLS;
import static io.cloudslang.content.excel.utils.Constants.FORMAT_XLSX;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class CreateExcelFileService {

    @NotNull
    public static Map<String, String> newExcelDocument(@NotNull final CreateExcelFileInputs newExcelDocumentInputs) {
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

        try (final FileOutputStream output =  new FileOutputStream(excelFileName) ){
            excelDoc.write(output);
        } catch (Exception exception) {
            return getFailureResultsMap(exception.getMessage());
        }

        return OutputUtilities.getSuccessResultsMap(excelFileName + " created successfully");
    }

    private static Workbook getNewExcelDocument(final String excelFileName) throws Exception {
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
}
