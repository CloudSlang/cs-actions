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

/**
 * Created by alexandra boicu 20/2/2019
 */
public class DeleteCellInputs {
    private final ExcelCommonInputs commonInputs;
    private final String rowIndex;
    private final String columnIndex;

    @ConstructorProperties({"commonInputs", "rowIndex", "columnIndex"})
    private DeleteCellInputs(ExcelCommonInputs commonInputs, String rowIndex, String columnIndex) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static DeleteCellInputsBuilder builder() {
        return new DeleteCellInputsBuilder();
    }


    @NotNull
    public ExcelCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getRowIndex() {
        return rowIndex;
    }

    @NotNull
    public String getColumnIndex() {
        return columnIndex;
    }

    public static class DeleteCellInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String rowIndex;
        private String columnIndex;

        @NotNull
        public DeleteCellInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public DeleteCellInputsBuilder rowIndex(@NotNull final String rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        @NotNull
        public DeleteCellInputsBuilder columnIndex(@NotNull final String columnIndex) {
            this.columnIndex = columnIndex;
            return this;
        }

        public DeleteCellInputs build() {
            return new DeleteCellInputs(commonInputs, rowIndex, columnIndex);
        }
    }

}
