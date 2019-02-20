package io.cloudslang.content.excel.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

/**
 * Created by alexandra boicu 20/2/2019
 */
public class NewExcelDocumentInputs {
    private final String excelFileName;
    private final String worksheetNames;
    private final String delimiter;

    @ConstructorProperties({"commonInputs", "delimiter"})
    private NewExcelDocumentInputs(String excelFileName, String worksheetNames, String delimiter) {
        this.worksheetNames = worksheetNames;
        this.excelFileName = excelFileName;
        this.delimiter = delimiter;
    }

    @NotNull
    public static NewExcelDocumentInputsBuilder builder() {
        return new NewExcelDocumentInputsBuilder();
    }

    @NotNull
    public String getWorksheetNames() {
        return worksheetNames;
    }

    @NotNull
    public String getExcelFileName() {
        return excelFileName;
    }

    @NotNull
    public String getDelimiter() {
        return delimiter;
    }

    public static class NewExcelDocumentInputsBuilder {
        private String excelFileName;
        private String worksheetNames;
        private String delimiter;

        private NewExcelDocumentInputsBuilder() {
        }

        @NotNull
        public NewExcelDocumentInputsBuilder excelFileName(@NotNull final String excelFileName) {
            this.excelFileName = excelFileName;
            return this;
        }

        @NotNull
        public NewExcelDocumentInputsBuilder worksheetNames(@NotNull final String worksheetNames) {
            this.worksheetNames = worksheetNames;
            return this;
        }

        @NotNull
        public NewExcelDocumentInputsBuilder delimiter(@NotNull final String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public NewExcelDocumentInputs build() {
            return new NewExcelDocumentInputs(excelFileName, worksheetNames, delimiter);
        }
    }
}
