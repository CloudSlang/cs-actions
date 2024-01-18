/*
 * Copyright 2022-2024 Open Text
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

package io.cloudslang.content.database.utils;

public class Constants {
    public static final String MINUS_1 = "-1";
    public static final String ZERO = "0";
    public static final String NEW_LINE = "\n";
    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String DEFAULT_TIMEOUT = "90";
    public static final String DBMS_OUTPUT_TEXT = "dbms_output";
    public static final String ORACLE_URL = "jdbc:oracle:thin:@";
    public static final String SUCCESS_MESSAGE = "Command completed successfully.";
    public static final String JKS = "JKS";
    public static final String TEMP_PATH = System.getProperty("java.io.tmpdir") + "content-java/";
    public static final String KEY_COLUMNS = "%s - Columns";
    public static final String CONCUR_READ_ONLY = "CONCUR_READ_ONLY";
    public static final String CONCUR_UPDATABLE = "CONCUR_UPDATABLE";
    public static final String TYPE_FORWARD_ONLY = "TYPE_FORWARD_ONLY";
    public static final String TYPE_SCROLL_INSENSITIVE = "TYPE_SCROLL_INSENSITIVE";
    public static final String TYPE_SCROLL_SENSITIVE = "TYPE_SCROLL_SENSITIVE";


    static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid value. Valid values are: true, false";
    public static final String INVALID_RESULT_SET_TYPE = "The result set is invalid";
    public static final String INVALID_RESULT_SET_CONCURRENCY = "The result set concurrency is invalid";
    public static final String EXCEPTION_INVALID_NUMBER = "%s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_PATH = "%s for %s input is not a valid path.";
    static final String EXCEPTION_INVALID_FILE = "The value '%s' for %s input is not a valid file path.";


    public static final String EXCEPTION_NEGATIVE_VALUE = "%s is not a valid timeout value for input %s. The value must be bigger or equal to 0.";

}
