package io.cloudslang.content.excel.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by danielmanciu on 18.02.2019.
 */
public class ExcelCommonInputs {
    private final String excelFileName;
    private final String worksheetName;

    @ConstructorProperties({"excelFileName", "worksheetName"})
    private ExcelCommonInputs(String excelFileName, String worksheetName) {
        this.excelFileName = excelFileName;
        this.worksheetName = worksheetName;
    }

    @NotNull
    public static ExcelCommonInputsBuilder builder() { return new ExcelCommonInputsBuilder(); }

    @NotNull
    public String getExcelFileName() {
        return excelFileName;
    }

    @NotNull
    public String getWorksheetName() {
        return worksheetName;
    }

    public static class ExcelCommonInputsBuilder {
        private String excelFileName = EMPTY;
        private String worksheetName = EMPTY;

        ExcelCommonInputsBuilder() { }

        @NotNull
        public ExcelCommonInputsBuilder excelFileName(@NotNull final String excelFileName) {
            this.excelFileName = excelFileName;
            return this;
        }

        @NotNull
        public ExcelCommonInputsBuilder worksheetName(@NotNull final String worksheetName) {
            this.worksheetName = worksheetName;
            return this;
        }

        @NotNull
        public ExcelCommonInputs build() {
            return new ExcelCommonInputs(excelFileName, worksheetName);
        }
    }
}
