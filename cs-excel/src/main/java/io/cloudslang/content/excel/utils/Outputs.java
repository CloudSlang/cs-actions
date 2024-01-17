/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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

import io.cloudslang.content.constants.OutputNames;

public class Outputs extends OutputNames {
    public static class GetCellOutputs {
        public static final String HEADER = "header";
        public static final String ROWS_COUNT = "rowsCount";
        public static final String COLUMNS_COUNT = "columnsCount";
    }

    public static final class GetRowIndexByCondition {
        public static final String ROWS_COUNT = "rowsCount";

    }

}
