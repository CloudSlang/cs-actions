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
