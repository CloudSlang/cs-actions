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

package io.cloudslang.content.excel.utils;

import io.cloudslang.content.constants.InputNames;

public class Inputs extends InputNames {
    public static class CommonInputs {
        public static final String EXCEL_FILE_NAME = "excelFileName";
        public static final String WORKSHEET_NAME = "worksheetName";
    }

    public static class GetCellInputs {
        public static final String HAS_HEADER = "hasHeader";
        public static final String FIRST_ROW_INDEX = "firstRowIndex";
        public static final String ROW_INDEX = "rowIndex";
        public static final String COLUMN_INDEX = "columnIndex";
        public static final String ROW_DELIMITER = "rowDelimiter";
        public static final String COLUMN_DELIMITER = "columnDelimiter";
    }

    public static class GetRowIndexByCondition {
        public static final String HAS_HEADER = "hasHeader";
        public static final String FIRST_ROW_INDEX = "firstRowIndex";
        public static final String COLUMN_INDEX_TO_QUERY = "columnIndextoQuery";
        public static final String OPERATOR = "operator";
        public static final String VALUE = "value";
    }

    public static class CreateExcelFile {
        public static final String DELIMITER = "delimiter";
        public static final String WORKSHEET_NAMES = "worksheetNames";
    }

    public static class DeleteCell {
        public static final String ROW_INDEX = "rowIndex";
        public static final String COLUMN_INDEX = "columnIndex";
    }

    public static class ModifyCell {
        public static final String ROW_INDEX = "rowIndex";
        public static final String COLUMN_INDEX = "columnIndex";
        public static final String NEW_VALUE = "newValue";
        public static final String COLUMN_DELIMITER = "columnDelimiter";
    }

    public static class AddCell {
        public static final String HEADER_DATA = "headerData";
        public static final String ROW_DATA = "rowData";
        public static final String ROW_INDEX = "rowIndex";
        public static final String COLUMN_INDEX = "columnIndex";
        public static final String ROW_DELIMITER = "rowDelimiter";
        public static final String COLUMN_DELIMITER = "columnDelimiter";
        public static final String OVERWRITE_DATA = "overwriteData";
    }
}
