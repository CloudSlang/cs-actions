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
public class ExcelCommonInputs {
    private final String excelFileName;
    private final String worksheetName;

    @ConstructorProperties({"excelFileName", "worksheetName"})
    private ExcelCommonInputs(String excelFileName, String worksheetName) {
        this.excelFileName = excelFileName;
        this.worksheetName = worksheetName;
    }

    @NotNull
    public static ExcelCommonInputsBuilder builder() {
        return new ExcelCommonInputsBuilder();
    }

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

        ExcelCommonInputsBuilder() {
        }

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
