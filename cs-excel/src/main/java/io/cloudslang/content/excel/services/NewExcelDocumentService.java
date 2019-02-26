package io.cloudslang.content.excel.services;

import io.cloudslang.content.excel.entities.ExcelOperationException;
import io.cloudslang.content.excel.entities.NewExcelDocumentInputs;
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
import static io.cloudslang.content.excel.utils.Constants.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class NewExcelDocumentService {

    @NotNull
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
