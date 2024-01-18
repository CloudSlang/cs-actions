/*
 * Copyright 2020-2024 Open Text
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

package io.cloudslang.content.filesystem.constants;

public final class Constants {

    public static final String SIZE = "size";
    public static final String IS_DIRECTORY_SUCCESS = "'%s' is a directory";
    public static final String EQUALS = "The file's size is equal to the threshold(%d)";
    public static final String GREATER_THAN = "The file's size is greater than the threshold(%d)";
    public static final String LESS_THAN = "The file's size is less than the threshold(%d)";
    public static final String RENAME_OPERATION_SUCCEEDED = "Rename operation succeeded.";

    private Constants() {
    }
    public static final String GET_MODIFIED_DATE = "Get Modified Date";
    public static final String GET_MODIFIED_DATE_SUCCESS = "The last modified date of '%s' was retrieved successfully";
    public static final String LESS = "0";
    public static final String GREATER = "2";
    public static final String EQUALS_VALUE = "1";
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    public static final String GET_CHILDREN = "Get Children";
    public static final String RETURN_CODE_SUCCESS = "0";

    public static final String MD5_SUM = "MD5 Sum";
    public static final String MD5 = "MD5";
    public static final String EQUALS_VALUE_RETURN_RESULT = "The file's checksum is equal to the compareTo input (%s)";
    public static final String NOT_EQUAL_VALUE_RETURN_RESULT = "The file's checksum is not equal to the compareTo input (%s)";

}
