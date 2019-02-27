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

/**
 * Created by danielmanciu on 18.02.2019.
 */
public class GetCellInputs {
    private final ExcelCommonInputs commonInputs;

    private final String hasHeader;
    private final String firstRowIndex;
    private final String rowIndex;
    private final String columnIndex;
    private final String rowDelimiter;
    private final String columnDelimiter;

    @ConstructorProperties({"hasHeader", "firstRowIndex", "rowIndex", "columnIndex", "rowDelimiter", "columnDelimiter"})
    private GetCellInputs(ExcelCommonInputs commonInputs, String hasHeader, String firstRowIndex, String rowIndex,
                          String columnIndex, String rowDelimiter, String columnDelimiter) {
        this.commonInputs = commonInputs;
        this.hasHeader = hasHeader;
        this.firstRowIndex = firstRowIndex;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.rowDelimiter = rowDelimiter;
        this.columnDelimiter = columnDelimiter;
    }

    @NotNull
    public static GetCellInputsBuilder builder() {
        return new GetCellInputsBuilder();
    }

    @NotNull
    public String getHasHeader() {
        return hasHeader;
    }

    @NotNull
    public String getFirstRowIndex() {
        return firstRowIndex;
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
    public ExcelCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class GetCellInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String hasHeader = EMPTY;
        private String firstRowIndex = EMPTY;
        private String rowIndex = EMPTY;
        private String columnIndex = EMPTY;
        private String rowDelimiter = EMPTY;
        private String columnDelimiter = EMPTY;

        private GetCellInputsBuilder() {
        }

        @NotNull
        public GetCellInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder hasHeader(@NotNull final String hasHeader) {
            this.hasHeader = hasHeader;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder firstRowIndex(@NotNull final String firstRowIndex) {
            this.firstRowIndex = firstRowIndex;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder rowIndex(@NotNull final String rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder columnIndex(@NotNull final String columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder rowDelimiter(@NotNull final String rowDelimiter) {
            this.rowDelimiter = rowDelimiter;
            return this;
        }

        @NotNull
        public GetCellInputsBuilder columnDelimiter(@NotNull final String columnDelimiter) {
            this.columnDelimiter = columnDelimiter;
            return this;
        }

        @NotNull
        public GetCellInputs build() {
            return new GetCellInputs(commonInputs, hasHeader, firstRowIndex, rowIndex, columnIndex, rowDelimiter, columnDelimiter);
        }
    }
}
