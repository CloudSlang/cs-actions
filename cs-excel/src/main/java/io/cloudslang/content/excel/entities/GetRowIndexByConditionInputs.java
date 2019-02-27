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

public class GetRowIndexByConditionInputs {
    private final ExcelCommonInputs commonInputs;
    private final String hasHeader;
    private final String firstRowIndex;
    private final String columnIndexToQuery;
    private final String operator;
    private final String value;


    @ConstructorProperties({"commonInputs", "hasHeader", "firstRowIndex", "columnIndexToQuery", "operator", "value"})
    private GetRowIndexByConditionInputs(ExcelCommonInputs commonInputs, String hasHeader, String firstRowIndex, String columnIndexToQuery, String operator, String value) {
        this.commonInputs = commonInputs;
        this.hasHeader = hasHeader;
        this.firstRowIndex = firstRowIndex;
        this.columnIndexToQuery = columnIndexToQuery;
        this.operator = operator;
        this.value = value;
    }

    @NotNull
    public static GetRowIndexByConditionInputsBuilder builder() {
        return new GetRowIndexByConditionInputsBuilder();
    }

    @NotNull
    public ExcelCommonInputs getCommonInputs() {
        return commonInputs;
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
    public String getColumnIndexToQuery() {
        return columnIndexToQuery;
    }

    @NotNull
    public String getOperator() {
        return operator;
    }

    @NotNull
    public String getValue() {
        return value;
    }

    public static class GetRowIndexByConditionInputsBuilder {
        private ExcelCommonInputs commonInputs;
        private String hasHeader;
        private String firstRowIndex;
        private String columnIndextoQuery;
        private String operator;
        private String value;


        private GetRowIndexByConditionInputsBuilder() {
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder commonInputs(@NotNull final ExcelCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder hasHeader(@NotNull final String hasHeader) {
            this.hasHeader = hasHeader;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder firstRowIndex(@NotNull final String firstRowIndex) {
            this.firstRowIndex = firstRowIndex;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder columnIndextoQuery(@NotNull final String columnIndextoQuery) {
            this.columnIndextoQuery = columnIndextoQuery;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder operator(@NotNull final String operator) {
            this.operator = operator;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputsBuilder value(@NotNull final String value) {
            this.value = value;
            return this;
        }

        @NotNull
        public GetRowIndexByConditionInputs build() {
            return new GetRowIndexByConditionInputs(commonInputs, hasHeader, firstRowIndex, columnIndextoQuery, operator, value);
        }
    }
}
