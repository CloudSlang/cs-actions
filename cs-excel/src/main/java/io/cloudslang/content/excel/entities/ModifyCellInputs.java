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
 * Created by danielmanciu on 21.02.2019.
 */
public class ModifyCellInputs {
    private final ExcelCommonInputs commonInputs;

    private final String rowIndex;
    private final String columnIndex;
    private final String newValue;
    private final String columnDelimiter;

    @ConstructorProperties({"commonInputs", "rowIndex", "columnIndex", "newValue", "columnDelimiter"})
    private ModifyCellInputs(ExcelCommonInputs commonInputs, String rowIndex,
                             String columnIndex, String newValue, String columnDelimiter) {
        this.commonInputs = commonInputs;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.newValue = newValue;
        this.columnDelimiter = columnDelimiter;
    }

    @NotNull
    public static ModifyCellInputsBuilder builder() {
        return new ModifyCellInputsBuilder();
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
    public String getNewValue() {
        return newValue;
    }

    @NotNull
    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    @NotNull
    public ExcelCommonInputs getCommonInputs() {
        return this.commonInputs;
    }

    public static class ModifyCellInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String rowIndex = EMPTY;
        private String columnIndex = EMPTY;
        private String newValue = EMPTY;
        private String columnDelimiter = EMPTY;

        private ModifyCellInputsBuilder() {
        }

        @NotNull
        public ModifyCellInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public ModifyCellInputsBuilder rowIndex(@NotNull final String rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        @NotNull
        public ModifyCellInputsBuilder columnIndex(@NotNull final String columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }

        @NotNull
        public ModifyCellInputsBuilder newValue(@NotNull final String newValue) {
            this.newValue = newValue;
            return this;
        }

        @NotNull
        public ModifyCellInputsBuilder columnDelimiter(@NotNull final String columnDelimiter) {
            this.columnDelimiter = columnDelimiter;
            return this;
        }

        @NotNull
        public ModifyCellInputs build() {
            return new ModifyCellInputs(commonInputs, rowIndex, columnIndex, newValue, columnDelimiter);
        }
    }
}
