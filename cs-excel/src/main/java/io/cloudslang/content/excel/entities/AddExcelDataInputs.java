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
package io.cloudslang.content.excel.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AddExcelDataInputs {
    private final ExcelCommonInputs commonInputs;

    private final String headerData;
    private final String rowData;
    private final String rowIndex;
    private final String columnIndex;
    private final String rowDelimiter;
    private final String columnDelimiter;
    private final String overwriteData;

    @ConstructorProperties({"commonInputs", "headerData", "rowData", "rowIndex", "columnIndex", "rowDelimiter", "columnDelimiter", "overwriteData"})
    private AddExcelDataInputs(ExcelCommonInputs commonInputs, String headerData, String rowData, String rowIndex,
                               String columnIndex, String rowDelimiter, String columnDelimiter, String overwriteData) {
        this.commonInputs = commonInputs;
        this.headerData = headerData;
        this.rowData = rowData;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.rowDelimiter = rowDelimiter;
        this.columnDelimiter = columnDelimiter;
        this.overwriteData = overwriteData;
    }

    @NotNull
    public static AddExcelDataInputsBuilder builder() {
        return new AddExcelDataInputsBuilder();
    }

    @NotNull
    public String getHeaderData() {
        return headerData;
    }

    @NotNull
    public String getRowData() {
        return rowData;
    }

    @NotNull
    public String getRowIndex() {
        return rowIndex;
    }

    @NotNull
    public String getColumnIndex() {
        return columnIndex;
    }

    @NotNull
    public String getRowDelimiter() {
        return rowDelimiter;
    }

    @NotNull
    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    @NotNull
    public String getOverwriteData() {
        return overwriteData;
    }

    @NotNull
    public ExcelCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class AddExcelDataInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String headerData = EMPTY;
        private String rowData = EMPTY;
        private String rowIndex = EMPTY;
        private String columnIndex = EMPTY;
        private String rowDelimiter = EMPTY;
        private String columnDelimiter = EMPTY;
        private String overwriteData = EMPTY;

        private AddExcelDataInputsBuilder() {
        }

        @NotNull
        public AddExcelDataInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder headerData(@NotNull final String headerData) {
            this.headerData = headerData;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder rowData(@NotNull final String rowData) {
            this.rowData = rowData;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder rowIndex(@NotNull final String rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder columnIndex(@NotNull final String columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder rowDelimiter(@NotNull final String rowDelimiter) {
            this.rowDelimiter = rowDelimiter;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder columnDelimiter(@NotNull final String columnDelimiter) {
            this.columnDelimiter = columnDelimiter;
            return this;
        }

        @NotNull
        public AddExcelDataInputsBuilder overwriteData(@NotNull final String overwriteData) {
            this.overwriteData = overwriteData;
            return this;
        }

        @NotNull
        public AddExcelDataInputs build() {
            return new AddExcelDataInputs(commonInputs, headerData, rowData, rowIndex, columnIndex, rowDelimiter, columnDelimiter, overwriteData);
        }
    }
}
